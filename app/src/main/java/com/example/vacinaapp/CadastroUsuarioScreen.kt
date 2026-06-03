package com.example.vacinaapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vacinaapp.db.UsuarioDBHelper
import kotlinx.coroutines.delay


private val Teal        = Color(0xFF008B8B)
private val TealDark    = Color(0xFF006D6D)
private val TealLight   = Color(0xFFE8F5F5)
private val BgScreen    = Color(0xFFF4F8F8)
private val BorderColor = Color(0xFFDDDDDD)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroUsuarioScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db      = remember { UsuarioDBHelper(context, null) }

    var nome             by remember { mutableStateOf("") }
    var usuario          by remember { mutableStateOf("") }
    var senha            by remember { mutableStateOf("") }
    var confirmarSenha   by remember { mutableStateOf("") }
    var senhaVisivel     by remember { mutableStateOf(false) }
    var confirmarVisivel by remember { mutableStateOf(false) }
    var visivel          by remember { mutableStateOf(false) }

    var erroNome        by remember { mutableStateOf("") }
    var erroUsuario     by remember { mutableStateOf("") }
    var erroSenha       by remember { mutableStateOf("") }
    var erroConfirmar   by remember { mutableStateOf("") }
    var sucessoCadastro by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visivel = true
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor       = BorderColor,
        focusedBorderColor         = Teal,
        cursorColor                = Teal,
        focusedLeadingIconColor    = Teal,
        unfocusedLeadingIconColor  = Color(0xFFAAAAAA),
        focusedTrailingIconColor   = Teal,
        unfocusedTrailingIconColor = Color(0xFFAAAAAA)
    )

    if (sucessoCadastro) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    "Cadastro realizado!",
                    fontWeight = FontWeight.Bold,
                    color = TealDark
                )
            },
            text = {
                Text("Seu cadastro foi criado com sucesso. Faça login para continuar.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        sucessoCadastro = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                ) {
                    Text("Ir para o login", color = Color.White)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgScreen)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
                .background(
                    Brush.verticalGradient(colors = listOf(TealDark, Teal))
                )
        )

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            Spacer(modifier = Modifier.height(14.dp))

            AnimatedVisibility(
                visible = visivel,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -20 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Criar conta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Registre-se para começar",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = visivel,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 60 })
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Dados da conta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TealDark
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Preencha todos os campos obrigatórios",
                            fontSize = 12.sp,
                            color = Color(0xFF888888),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        CampoLabel("Nome completo")
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it; erroNome = "" },
                            placeholder = {
                                Text(
                                    "Seu nome",
                                    color = Color(0xFFBBBBBB),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            isError = erroNome.isNotEmpty(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors
                        )
                        if (erroNome.isNotEmpty()) ErroTexto(erroNome)

                        Spacer(modifier = Modifier.height(16.dp))


                        CampoLabel("Usuário")
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = usuario,
                            onValueChange = { usuario = it; erroUsuario = "" },
                            placeholder = {
                                Text(
                                    "Nome de usuário",
                                    color = Color(0xFFBBBBBB),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            isError = erroUsuario.isNotEmpty(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors
                        )
                        if (erroUsuario.isNotEmpty()) ErroTexto(erroUsuario)

                        Spacer(modifier = Modifier.height(16.dp))


                        CampoLabel("Senha")
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = senha,
                            onValueChange = { senha = it; erroSenha = "" },
                            placeholder = {
                                Text(
                                    "Mínimo 4 caracteres",
                                    color = Color(0xFFBBBBBB),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            visualTransformation = if (senhaVisivel)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (senhaVisivel)
                                                android.R.drawable.ic_menu_view
                                            else
                                                android.R.drawable.ic_secure
                                        ),
                                        contentDescription = if (senhaVisivel)
                                            "Ocultar senha"
                                        else
                                            "Mostrar senha",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            isError = erroSenha.isNotEmpty(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors
                        )
                        if (erroSenha.isNotEmpty()) ErroTexto(erroSenha)

                        Spacer(modifier = Modifier.height(16.dp))


                        CampoLabel("Confirmar senha")
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = confirmarSenha,
                            onValueChange = { confirmarSenha = it; erroConfirmar = "" },
                            placeholder = {
                                Text(
                                    "Repita a senha",
                                    color = Color(0xFFBBBBBB),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            visualTransformation = if (confirmarVisivel)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { confirmarVisivel = !confirmarVisivel }) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (confirmarVisivel)
                                                android.R.drawable.ic_menu_view
                                            else
                                                android.R.drawable.ic_secure
                                        ),
                                        contentDescription = if (confirmarVisivel)
                                            "Ocultar senha"
                                        else
                                            "Mostrar senha",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            isError = erroConfirmar.isNotEmpty(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors
                        )
                        if (erroConfirmar.isNotEmpty()) ErroTexto(erroConfirmar)

                        Spacer(modifier = Modifier.height(28.dp))


                        Button(
                            onClick = {
                                var valido = true

                                if (nome.isBlank()) {
                                    erroNome = "Informe seu nome completo"
                                    valido = false
                                }
                                if (usuario.isBlank()) {
                                    erroUsuario = "Informe um nome de usuário"
                                    valido = false
                                } else if (usuario.length < 3) {
                                    erroUsuario = "Mínimo 3 caracteres"
                                    valido = false
                                } else if (db.usuarioExiste(usuario)) {
                                    erroUsuario = "Esse usuário já está em uso"
                                    valido = false
                                }
                                if (senha.isBlank()) {
                                    erroSenha = "Informe uma senha"
                                    valido = false
                                } else if (senha.length < 4) {
                                    erroSenha = "Mínimo 4 caracteres"
                                    valido = false
                                }
                                if (confirmarSenha.isBlank()) {
                                    erroConfirmar = "Confirme sua senha"
                                    valido = false
                                } else if (senha != confirmarSenha) {
                                    erroConfirmar = "As senhas não coincidem"
                                    valido = false
                                }

                                if (valido) {
                                    // ✅ Salva no banco de dados
                                    db.addUsuario(nome, usuario, senha)
                                    sucessoCadastro = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Teal,
                                contentColor   = Color.White
                            )
                        ) {
                            Text(
                                "Cadastrar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Já tem uma conta? ",
                                fontSize = 13.sp,
                                color = Color(0xFF888888)
                            )
                            TextButton(
                                onClick = { navController.popBackStack() },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "Entrar",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
private fun CampoLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ErroTexto(mensagem: String) {
    Text(
        text = mensagem,
        color = Color.Red,
        fontSize = 11.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp)
    )
}