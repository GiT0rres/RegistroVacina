package com.example.vacinaapp

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vacinaapp.db.UsuarioDBHelper

private val Teal     = Color(0xFF008B8B)
private val TealDark = Color(0xFF006D6D)
private val BgScreen = Color(0xFFF4F8F8)
private val BgIcon   = Color(0xFFE8F5F5)

@Composable
fun PerfilScreen(navController: NavController) {

    val context = LocalContext.current
    val db      = remember { UsuarioDBHelper(context, null) }

    // Lê o usuário da sessão
    val usuarioLogado = remember { SessionManager.getUsuario(context) }

    // Estado carregado do banco
    var nomeExibido  by remember { mutableStateOf("") }
    var fotoUriStr   by remember { mutableStateOf<String?>(null) }
    var totalVacinas by remember { mutableStateOf(0) }

    // Recarrega dados toda vez que a tela fica visível
    LaunchedEffect(usuarioLogado) {
        val dados = db.buscarUsuario(usuarioLogado)
        nomeExibido = dados?.get(UsuarioDBHelper.COL_NOME) ?: usuarioLogado
        fotoUriStr  = dados?.get(UsuarioDBHelper.COL_FOTO_URI)

        val vacinaDb = VacinaDBHelper(context, null)
        totalVacinas = vacinaDb.getAllVacinas().size
    }

    val fotoUri: Uri? = fotoUriStr?.let { Uri.parse(it) }

    val bitmap = remember(fotoUri) {
        fotoUri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } catch (_: Exception) { null }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val uriStr = it.toString()
            db.atualizarFotoUri(usuarioLogado, uriStr)
            fotoUriStr = uriStr
        }
    }

    var mostrarDialogLogout by remember { mutableStateOf(false) }

    if (mostrarDialogLogout) {
        AlertDialog(
            onDismissRequest = { mostrarDialogLogout = false },
            title = { Text("Sair da conta", fontWeight = FontWeight.Bold) },
            text  = { Text("Deseja realmente sair?") },
            confirmButton = {
                Button(
                    onClick = {
                        SessionManager.limparSessao(context)
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Sair", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogLogout = false }) {
                    Text("Cancelar", color = Teal)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(containerColor = BgScreen) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(Brush.verticalGradient(listOf(TealDark, Teal))),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Meu Perfil",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "@$usuarioLogado",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 13.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(y = (-52).dp)
                    .size(104.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(4.dp, Color.White, CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BgIcon),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto Perfil",
                            tint = Teal,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Teal)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar foto",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height((-36).dp))


            Text(
                text = nomeExibido,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BgIcon)
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(text = "Usuário", fontSize = 12.sp, color = Teal, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Teal),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalVacinas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Vacinas registradas",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    PerfilLinhaInfo(
                        rotulo = "Nome completo",
                        valor  = nomeExibido.ifBlank { "—" },
                        icone  = { IconPerfilUser() }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    PerfilLinhaInfo(
                        rotulo = "Usuário",
                        valor  = "@$usuarioLogado",
                        icone  = { IconPerfilIdade() }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    PerfilLinhaInfo(
                        rotulo = "Foto de perfil",
                        valor  = if (fotoUriStr != null) "Foto personalizada ✓" else "Toque no avatar para adicionar",
                        icone  = { IconPerfilSangue() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick  = { navController.navigate("editarPerfil") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape  = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Perfil", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedButton(
                onClick  = { mostrarDialogLogout = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape  = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFD32F2F)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sair da conta", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}



@Composable
fun PerfilLinhaInfo(rotulo: String, valor: String, icone: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BgIcon),
            contentAlignment = Alignment.Center
        ) { icone() }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(text = rotulo, fontSize = 11.sp, color = Color(0xFF999999))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
        }
    }
}



@Composable fun IconPerfilUser() {
    Image(painter = painterResource(id = R.drawable.nome), contentDescription = null)
}

@Composable fun IconPerfilIdade() {
    Image(painter = painterResource(id = R.drawable.idade), contentDescription = null)
}

@Composable fun IconPerfilSangue() {
    Image(painter = painterResource(id = R.drawable.sangue), contentDescription = null)
}

@Composable fun IconPerfilVacina() {
    Image(painter = painterResource(id = R.drawable.ic_perfil_vacina), contentDescription = null)
}