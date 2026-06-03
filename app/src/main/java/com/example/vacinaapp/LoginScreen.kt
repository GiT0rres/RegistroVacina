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

private val Teal        = Color(0xFF008B8B)
private val TealDark    = Color(0xFF006D6D)
private val BgScreen    = Color(0xFFF4F8F8)
private val BorderColor = Color(0xFFDDDDDD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db      = remember { UsuarioDBHelper(context, null) }

    var usuario        by remember { mutableStateOf("") }
    var senha          by remember { mutableStateOf("") }
    var senhaVisivel   by remember { mutableStateOf(false) }
    var erroUsuario    by remember { mutableStateOf(false) }
    var erroSenha      by remember { mutableStateOf(false) }
    var erroCredencial by remember { mutableStateOf(false) }
    var visivel        by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(100)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgScreen)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
                .background(Brush.verticalGradient(colors = listOf(TealDark, Teal)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(52.dp))

            AnimatedVisibility(
                visible = visivel,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -20 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "VacinaApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Seu histórico de vacinas",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))


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
                            text = "Entrar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TealDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Faça login para acessar suas vacinas",
                            fontSize = 13.sp,
                            color = Color(0xFF888888),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(28.dp))


                        OutlinedTextField(
                            value = usuario,
                            onValueChange = {
                                usuario = it
                                erroUsuario = false
                                erroCredencial = false
                            },
                            placeholder = { Text("Usuário", color = Color(0xFFBBBBBB), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            isError = erroUsuario || erroCredencial,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors
                        )
                        if (erroUsuario) {
                            Text(
                                "Informe o usuário",
                                color = Color.Red,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp, top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = senha,
                            onValueChange = {
                                senha = it
                                erroSenha = false
                                erroCredencial = false
                            },
                            placeholder = { Text("Senha", color = Color(0xFFBBBBBB), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = if (senhaVisivel) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            isError = erroSenha || erroCredencial,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors
                        )
                        if (erroSenha) {
                            Text(
                                "Informe a senha",
                                color = Color.Red,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp, top = 2.dp)
                            )
                        }

                        if (erroCredencial) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                            ) {
                                Text(
                                    text = "Usuário ou senha incorretos.",
                                    color = Color(0xFFB71C1C),
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))


                        Button(
                            onClick = {
                                erroUsuario = usuario.isBlank()
                                erroSenha   = senha.isBlank()

                                if (!erroUsuario && !erroSenha) {
                                    // ✅ Corrigido: validarLogin (era verificarLogin)
                                    if (db.validarLogin(usuario, senha)) {

                                        // ✅ Salva sessão para o PerfilScreen ler
                                        val dados = db.buscarUsuario(usuario)
                                        val nome  = dados?.get(UsuarioDBHelper.COL_NOME) ?: usuario
                                        SessionManager.salvarSessao(context, usuario, nome)

                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        erroCredencial = true
                                    }
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
                            Text("Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Não tem conta? ",
                                fontSize = 13.sp,
                                color = Color(0xFF888888)
                            )
                            TextButton(
                                onClick = { navController.navigate("cadastroUsuario") },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = "Cadastre-se",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Teal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "VacinaApp © 2025",
                fontSize = 11.sp,
                color = Color(0xFFAAAAAA)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}