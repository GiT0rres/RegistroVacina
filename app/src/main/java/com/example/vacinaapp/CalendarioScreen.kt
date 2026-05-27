package com.example.vacinaapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

private val Teal     = Color(0xFF008B8B)
private val TealDark = Color(0xFF006D6D)
private val BgScreen = Color(0xFFF4F8F8)
private val BgIcon   = Color(0xFFE8F5F5)

data class ProximaVacina(
    val nome: String,
    val data: String,
    val mes: String,
    val dia: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreen(navController: NavHostController) {

    val vacinas = listOf(
        ProximaVacina("Febre Amarela",    "12/06/2026", "JUN", "12"),
        ProximaVacina("Influenza",        "28/06/2026", "JUN", "28"),
        ProximaVacina("COVID-19 Reforço", "15/07/2026", "JUL", "15"),
        ProximaVacina("Hepatite B",       "03/08/2026", "AGO", "03")
    )

    Scaffold(
        containerColor = BgScreen,

        topBar = {
            TopAppBar(
                title = {
                    Text("Próximas Vacinas", fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Teal)
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(Teal),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // res/drawable/ic_calendario_hero.png
                    Image(
                        painter = painterResource(id = R.drawable.calendario2),
                        contentDescription = "Calendário",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Calendário de Vacinação",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Próximas doses agendadas",
                fontSize = 13.sp,
                color = Color(0xFF888888),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(vacinas) { vacina ->
                    CalendarioCard(vacina = vacina)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun CalendarioCard(vacina: ProximaVacina) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(BgIcon)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = vacina.mes, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Teal)
                Text(text = vacina.dia, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TealDark)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = vacina.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1A1A))
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = vacina.data, fontSize = 12.sp, color = Color(0xFF888888))
            }

            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(BgIcon),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null, tint = Teal, modifier = Modifier.size(18.dp))
            }
        }
    }
}