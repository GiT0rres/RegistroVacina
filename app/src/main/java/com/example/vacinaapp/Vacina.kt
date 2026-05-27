package com.example.vacinaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
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
import androidx.navigation.NavHostController
import com.example.vacinaapp.VacinaDBHelper
import com.example.vacinaapp.VacinaModel

// ── Cores do tema ─────────────────────────────────────────────────────────────

private val Teal     = Color(0xFF008B8B)
private val TealDark = Color(0xFF006D6D)
private val BgScreen = Color(0xFFF4F8F8)

// ── Tela principal ────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(navController: NavHostController) {

    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var vacinas by remember { mutableStateOf<List<VacinaModel>>(emptyList()) }

    // Carrega dados do banco sempre que a tela é recomposta
    LaunchedEffect(Unit) {
        val db = VacinaDBHelper(context, null)
        vacinas = db.getAllVacinas()
    }

    val vacinasFiltradas = vacinas.filter {
        it.nome.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = BgScreen,

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cadastro/-1") },
                containerColor = Color.White,
                contentColor = Teal,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        },

        bottomBar = {
            NavigationBar(containerColor = Color.White) {

                // Vacinas (ativo)
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.lote),
                                contentDescription = "Vacinas",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    },
                    label = { Text("Vacinas", fontSize = 11.sp, color = Teal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Teal,
                        indicatorColor = Color.Transparent
                    )
                )

                // Calendário
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("calendario") },
                    icon = {
                        Icon(Icons.Outlined.DateRange, contentDescription = "Calendário", tint = Color(0xFF999999))
                    },
                    label = { Text("Calendário", fontSize = 11.sp, color = Color(0xFF999999)) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )

                // Perfil
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("perfil") },
                    icon = {
                        Icon(Icons.Outlined.Person, contentDescription = "Perfil", tint = Color(0xFF999999))
                    },
                    label = { Text("Perfil", fontSize = 11.sp, color = Color(0xFF999999)) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Header teal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Teal)
            )

            // Card principal sobreposto ao header
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        // Título com ícone de escudo
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_perfil_vacina),
                                contentDescription = "Escudo Vacina",
                                modifier = Modifier.size(28.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Minhas Vacinas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TealDark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo de busca
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFAAAAAA))
                            },
                            placeholder = {
                                Text("Buscar vacina...", color = Color(0xFFBBBBBB), fontSize = 14.sp)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFDDDDDD),
                                focusedBorderColor = Teal
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (vacinasFiltradas.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isEmpty()) "Nenhuma vacina cadastrada."
                                    else "Nenhuma vacina encontrada.",
                                    fontSize = 14.sp,
                                    color = Color(0xFF999999)
                                )
                            }
                        } else {
                            // Lista de vacinas vindas do banco
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(vacinasFiltradas) { vacina ->
                                    VacinaCard(vacina = vacina) {
                                        navController.navigate("detalhes/${vacina.id}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Card individual de vacina ─────────────────────────────────────────────────

@Composable
fun VacinaCard(vacina: VacinaModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_perfil_vacina),
                    contentDescription = vacina.nome,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = vacina.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF222222))
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Data: ${vacina.data}", fontSize = 12.sp, color = Color(0xFF666666))
                Text(text = "Lote: ${vacina.lote}", fontSize = 12.sp, color = Color(0xFF666666))
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFCCCCCC),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}