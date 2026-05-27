package com.example.vacinaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vacinaapp.VacinaDBHelper
import com.example.vacinaapp.VacinaModel

private val Teal     = Color(0xFF008B8B)
private val TealDark = Color(0xFF006D6D)
private val BgScreen = Color(0xFFF4F8F8)
private val BgIcon   = Color(0xFFE8F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesVacinaScreen(navController: NavController, vacinaId: Int) {

    val context = LocalContext.current
    val db      = VacinaDBHelper(context, null)

    var vacina by remember { mutableStateOf<VacinaModel?>(null) }
    var mostrarDialogDelete by remember { mutableStateOf(false) }

    // Carrega vacina do banco pelo id
    LaunchedEffect(vacinaId) {
        vacina = db.getVacinaById(vacinaId)
    }

    // Dialog de confirmação de exclusão
    if (mostrarDialogDelete) {
        AlertDialog(
            onDismissRequest = { mostrarDialogDelete = false },
            title = { Text("Excluir vacina") },
            text = { Text("Tem certeza que deseja excluir este registro?") },
            confirmButton = {
                TextButton(onClick = {
                    db.deleteVacina(vacinaId)
                    mostrarDialogDelete = false
                    navController.popBackStack("home", inclusive = false)
                }) {
                    Text("Excluir", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogDelete = false }) {
                    Text("Cancelar", color = Teal)
                }
            }
        )
    }

    Scaffold(
        containerColor = BgScreen,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhes da Vacina",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarDialogDelete = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Teal)
            )
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgScreen)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("cadastro/$vacinaId") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Teal
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Teal)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Editar registro", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        }

    ) { paddingValues ->

        vacina?.let { v ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(28.dp))

                // ── Ícone hero ────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(BgIcon),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.triplice_viral),
                        contentDescription = "Vacina",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(v.nome, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1A1A1A))

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .border(1.5.dp, Teal, RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Registrada", fontSize = 13.sp, color = TealDark, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("✓", fontSize = 13.sp, color = Teal, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                        LinhaInfo(titulo = "Vacina",            valor = v.nome,                            icone = { IconVacina() })
                        Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                        LinhaInfo(titulo = "Data de Aplicação", valor = v.data,                            icone = { IconCalendario() })
                        Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                        LinhaInfo(titulo = "Lote",              valor = v.lote,                            icone = { IconLote() })
                        Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                        LinhaInfo(titulo = "Local",             valor = v.local.ifBlank { "—" },           icone = { IconLocal() })
                        Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                        LinhaInfo(titulo = "Observações",       valor = v.obs.ifBlank { "—" },             icone = { IconObs() }, isLast = true)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        } ?: run {
            // Vacina não encontrada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Registro não encontrado.", color = Color(0xFF999999), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun LinhaInfo(
    titulo: String,
    valor: String,
    icone: @Composable () -> Unit,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
            Text(text = titulo, fontSize = 12.sp, color = Color(0xFF999999))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
        }
    }
}

// ── Ícones via painterResource (nomes de imagem mantidos) ─────────────────────

@Composable
fun IconVacina() {
    Image(
        painter = painterResource(id = R.drawable.vacina),
        contentDescription = "Vacina",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun IconCalendario() {
    Image(
        painter = painterResource(id = R.drawable.calendario),
        contentDescription = "Calendário",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun IconLote() {
    Image(
        painter = painterResource(id = R.drawable.lote),
        contentDescription = "Lote",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun IconLocal() {
    Image(
        painter = painterResource(id = R.drawable.local),
        contentDescription = "Local",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun IconObs() {
    Image(
        painter = painterResource(id = R.drawable.observacoes),
        contentDescription = "Observações",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}