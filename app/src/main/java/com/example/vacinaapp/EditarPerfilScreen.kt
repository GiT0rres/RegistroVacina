package com.example.vacinaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vacinaapp.db.UsuarioDBHelper

private val Teal        = Color(0xFF008B8B)
private val TealDark    = Color(0xFF006D6D)
private val BgScreen    = Color(0xFFF4F8F8)
private val BgIcon      = Color(0xFFE8F5F5)
private val BorderColor = Color(0xFFDDDDDD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(navController: NavController) {

    val context       = LocalContext.current
    val db            = remember { UsuarioDBHelper(context, null) }
    val usuarioLogado = remember { SessionManager.getUsuario(context) }

    // Carrega dados atuais do banco
    var nome             by remember { mutableStateOf("") }
    var novaSenha        by remember { mutableStateOf("") }
    var confirmarSenha   by remember { mutableStateOf("") }
    var senhaVisivel     by remember { mutableStateOf(false) }
    var confirmarVisivel by remember { mutableStateOf(false) }

    var erroNome      by remember { mutableStateOf("") }
    var erroSenha     by remember { mutableStateOf("") }
    var erroConfirmar by remember { mutableStateOf("") }
    var sucessoSalvo  by remember { mutableStateOf(false) }

    LaunchedEffect(usuarioLogado) {
        val dados = db.buscarUsuario(usuarioLogado)
        nome = dados?.get(UsuarioDBHelper.COL_NOME) ?: ""
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor      = BorderColor,
        focusedBorderColor        = Teal,
        cursorColor               = Teal,
        focusedLeadingIconColor   = Teal,
        unfocusedLeadingIconColor = Color(0xFFAAAAAA),
        unfocusedLabelColor       = Color(0xFF888888),
        focusedLabelColor         = Teal
    )

    if (sucessoSalvo) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Perfil atualizado!", fontWeight = FontWeight.Bold, color = TealDark)
            },
            text = { Text("Suas informações foram salvas com sucesso.") },
            confirmButton = {
                Button(
                    onClick = {
                        sucessoSalvo = false
                        // Atualiza o nome na sessão e volta
                        SessionManager.salvarSessao(context, usuarioLogado, nome)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                ) { Text("OK", color = Color.White) }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = BgScreen,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar Perfil",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Teal)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // Validações
                        var valido = true
                        erroNome      = ""
                        erroSenha     = ""
                        erroConfirmar = ""

                        if (nome.isBlank()) {
                            erroNome = "Informe seu nome completo"
                            valido = false
                        }
                        if (novaSenha.isNotBlank()) {
                            if (novaSenha.length < 4) {
                                erroSenha = "Mínimo 4 caracteres"
                                valido = false
                            }
                            if (confirmarSenha != novaSenha) {
                                erroConfirmar = "As senhas não coincidem"
                                valido = false
                            }
                        }

                        if (valido) {
                            db.atualizarPerfil(
                                usuario   = usuarioLogado,
                                novoNome  = nome,
                                novaSenha = novaSenha.ifBlank { null },
                                fotoUri   = null   // foto é gerenciada direto na PerfilScreen
                            )
                            sucessoSalvo = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape  = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salvar alterações", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick  = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = Teal, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(28.dp))


            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(BgIcon, Color(0xFFB2DFDF)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Teal,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Altere seus dados abaixo",
                fontSize = 13.sp,
                color = Color(0xFF888888)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                SecaoTitulo("Informações pessoais")
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // ── Nome ──────────────────────────────────────────
                        OutlinedTextField(
                            value           = nome,
                            onValueChange   = { nome = it; erroNome = "" },
                            label           = { Text("Nome completo") },
                            leadingIcon     = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            isError         = erroNome.isNotEmpty(),
                            singleLine      = true,
                            modifier        = Modifier.fillMaxWidth(),
                            shape           = RoundedCornerShape(12.dp),
                            colors          = fieldColors
                        )
                        if (erroNome.isNotEmpty()) {
                            ErroTextoEditar(erroNome)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Usuário (somente leitura)
                        OutlinedTextField(
                            value         = "@$usuarioLogado",
                            onValueChange = {},
                            label         = { Text("Usuário") },
                            readOnly      = true,
                            singleLine    = true,
                            modifier      = Modifier.fillMaxWidth(),
                            shape         = RoundedCornerShape(12.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor  = BorderColor,
                                disabledLabelColor   = Color(0xFF888888),
                                disabledTextColor    = Color(0xFFAAAAAA)
                            ),
                            enabled       = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Seção: Segurança ──────────────────────────────────────
                SecaoTitulo("Segurança")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Deixe em branco para manter a senha atual.",
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // ── Nova Senha ────────────────────────────────────
                        OutlinedTextField(
                            value         = novaSenha,
                            onValueChange = { novaSenha = it; erroSenha = "" },
                            label         = { Text("Nova senha") },
                            leadingIcon   = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            visualTransformation = if (senhaVisivel)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon  = {
                                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (senhaVisivel)
                                                android.R.drawable.ic_menu_view
                                            else
                                                android.R.drawable.ic_secure
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            isError       = erroSenha.isNotEmpty(),
                            singleLine    = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier      = Modifier.fillMaxWidth(),
                            shape         = RoundedCornerShape(12.dp),
                            colors        = fieldColors
                        )
                        if (erroSenha.isNotEmpty()) ErroTextoEditar(erroSenha)

                        Spacer(modifier = Modifier.height(8.dp))


                        OutlinedTextField(
                            value         = confirmarSenha,
                            onValueChange = { confirmarSenha = it; erroConfirmar = "" },
                            label         = { Text("Confirmar nova senha") },
                            leadingIcon   = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            visualTransformation = if (confirmarVisivel)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon  = {
                                IconButton(onClick = { confirmarVisivel = !confirmarVisivel }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (confirmarVisivel)
                                                android.R.drawable.ic_menu_view
                                            else
                                                android.R.drawable.ic_secure
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            isError       = erroConfirmar.isNotEmpty(),
                            singleLine    = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier      = Modifier.fillMaxWidth(),
                            shape         = RoundedCornerShape(12.dp),
                            colors        = fieldColors
                        )
                        if (erroConfirmar.isNotEmpty()) ErroTextoEditar(erroConfirmar)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SecaoTitulo(texto: String) {
    Text(
        text = texto,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = TealDark
    )
}

@Composable
private fun ErroTextoEditar(mensagem: String) {
    Text(
        text = mensagem,
        color = Color.Red,
        fontSize = 11.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp)
    )
}