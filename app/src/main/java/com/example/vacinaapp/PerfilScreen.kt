package com.example.vacinaapp

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val Teal = Color(0xFF008B8B)
private val BgScreen = Color(0xFFF4F8F8)
private val BgIcon = Color(0xFFE8F5F5)

@Composable
fun PerfilScreen(navController: NavController) {

    val context = LocalContext.current

    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        fotoUri = uri
    }

    val bitmap = remember(fotoUri) {
        fotoUri?.let {
            try {
                context.contentResolver
                    .openInputStream(it)
                    ?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
            } catch (_: Exception) {
                null
            }
        }
    }

    Scaffold(
        containerColor = BgScreen
    ) { paddingValues ->

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
                    .height(160.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 28.dp,
                            bottomEnd = 28.dp
                        )
                    )
                    .background(Teal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Meu Perfil",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Box(
                modifier = Modifier
                    .offset(y = (-48).dp)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(BgIcon)
                    .border(3.dp, Color.White, CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto Perfil",
                        tint = Teal,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height((-32).dp))

            Text(
                text = "Giovanna Alves",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BgIcon)
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Usuária",
                    fontSize = 12.sp,
                    color = Teal,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column {
                    PerfilLinhaInfo(
                        rotulo = "Nome",
                        valor = "Giovanna Alves",
                        icone = { IconPerfilUser() }
                    )

                    HorizontalDivider()

                    PerfilLinhaInfo(
                        rotulo = "Idade",
                        valor = "20 anos",
                        icone = { IconPerfilIdade() }
                    )

                    HorizontalDivider()

                    PerfilLinhaInfo(
                        rotulo = "Tipo sanguíneo",
                        valor = "O+",
                        icone = { IconPerfilSangue() }
                    )

                    HorizontalDivider()

                    PerfilLinhaInfo(
                        rotulo = "Última vacina",
                        valor = "Influenza",
                        icone = { IconPerfilVacina() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("editarPerfil")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Editar Perfil")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PerfilLinhaInfo(
    rotulo: String,
    valor: String,
    icone: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BgIcon),
            contentAlignment = Alignment.Center
        ) {
            icone()
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = rotulo,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = valor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun IconPerfilUser() {
    Image(
        painter = painterResource(id = R.drawable.nome),
        contentDescription = null
    )
}

@Composable
fun IconPerfilIdade() {
    Image(
        painter = painterResource(id = R.drawable.idade),
        contentDescription = null
    )
}

@Composable
fun IconPerfilSangue() {
    Image(
        painter = painterResource(id = R.drawable.sangue),
        contentDescription = null
    )
}

@Composable
fun IconPerfilVacina() {
    Image(
        painter = painterResource(id = R.drawable.ic_perfil_vacina),
        contentDescription = null
    )
}