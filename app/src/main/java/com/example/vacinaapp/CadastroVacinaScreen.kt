package com.example.vacinaapp

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vacinaapp.VacinaDBHelper

private val Teal             = Color(0xFF008B8B)
private val TealDark         = Color(0xFF006D6D)
private val BgScreen         = Color(0xFFFFFFFF)
private val BgIcon           = Color(0xFFE8F5F5)
private val LabelColor       = Color(0xFF1A1A1A)
private val BorderColor      = Color(0xFFDDDDDD)
private val PlaceholderColor = Color(0xFFBBBBBB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroVacinaScreen(navController: NavController, vacinaId: Int = -1) {

    val context = LocalContext.current
    val db      = VacinaDBHelper(context, null)

    val vacinaExistente = remember(vacinaId) {
        if (vacinaId != -1) db.getVacinaById(vacinaId) else null
    }

    var nomeVacina       by remember { mutableStateOf(vacinaExistente?.nome  ?: "") }
    var data             by remember { mutableStateOf(vacinaExistente?.data  ?: "") }
    var lote             by remember { mutableStateOf(vacinaExistente?.lote  ?: "") }
    var local            by remember { mutableStateOf(vacinaExistente?.local ?: "") }
    var observacoes      by remember { mutableStateOf(vacinaExistente?.obs   ?: "") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var erroNome         by remember { mutableStateOf(false) }
    var erroData         by remember { mutableStateOf(false) }
    var erroLote         by remember { mutableStateOf(false) }

    val vacinasDisponiveis = listOf(
        "Hepatite B", "Febre Amarela", "Tríplice Viral (SCR)",
        "Influenza", "COVID-19", "Tétano", "HPV"
    )

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = BorderColor,
        focusedBorderColor = Teal,
        unfocusedLabelColor = Color(0xFF888888),
        focusedLabelColor = Teal,
        cursorColor = Teal
    )

    Scaffold(
        containerColor = BgScreen,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (vacinaId == -1) "Adicionar Vacina" else "Editar Vacina",
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
                        erroNome = nomeVacina.isBlank()
                        erroData = data.isBlank()
                        erroLote = lote.isBlank()

                        if (!erroNome && !erroData && !erroLote) {
                            if (vacinaId == -1) {
                                db.addVacina(
                                    nome  = nomeVacina,
                                    data  = data,
                                    lote  = lote,
                                    local = local,
                                    obs   = observacoes
                                )
                            } else {
                                db.updateVacina(
                                    id    = vacinaId,
                                    nome  = nomeVacina,
                                    data  = data,
                                    lote  = lote,
                                    local = local,
                                    obs   = observacoes
                                )
                            }
                            navController.popBackStack("home", inclusive = false)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salvar", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { navController.popBackStack() },
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

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(BgIcon)
                )
                Image(
                    painter = painterResource(id = R.drawable.triplice_viral),
                    contentDescription = "Seringa",
                    modifier = Modifier.size(36.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // ── Nome da Vacina (ExposedDropdownMenuBox) ───────────────
                // Usa ExposedDropdownMenuBox que foi feito exatamente para
                // este caso: campo somente leitura + dropdown
                FieldLabel("Nome da Vacina")
                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = nomeVacina,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text("Selecione a vacina", color = PlaceholderColor, fontSize = 14.sp)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                        },
                        isError = erroNome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(), // ancora o dropdown no campo
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        vacinasDisponiveis.forEach { vacina ->
                            DropdownMenuItem(
                                text = { Text(vacina) },
                                onClick = {
                                    nomeVacina = vacina
                                    dropdownExpanded = false
                                    erroNome = false
                                }
                            )
                        }
                    }
                }

                if (erroNome) {
                    Text("Selecione o nome da vacina", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Data de Aplicação ─────────────────────────────────────
                FieldLabel("Data de Aplicação")
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = data,
                    onValueChange = { new ->
                        val digits = new.filter { it.isDigit() }.take(8)
                        data = buildString {
                            digits.forEachIndexed { i, c ->
                                if (i == 2 || i == 4) append('/')
                                append(c)
                            }
                        }
                        erroData = false
                    },
                    placeholder = { Text("dd/mm/aaaa", color = PlaceholderColor, fontSize = 14.sp) },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFF666666))
                    },
                    isError = erroData,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors
                )
                if (erroData) {
                    Text("Informe a data de aplicação", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                FieldLabel("Lote")
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = lote,
                    onValueChange = { lote = it; erroLote = false },
                    placeholder = { Text("Informe o lote", color = PlaceholderColor, fontSize = 14.sp) },
                    isError = erroLote,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors
                )
                if (erroLote) {
                    Text("Informe o lote", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                FieldLabel("Local", opcional = true)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = local,
                    onValueChange = { local = it },
                    placeholder = { Text("Informe o local", color = PlaceholderColor, fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors
                )

                Spacer(modifier = Modifier.height(16.dp))


                FieldLabel("Observações", opcional = true)
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = observacoes,
                        onValueChange = { if (it.length <= 150) observacoes = it },
                        placeholder = { Text("Digite aqui...", color = PlaceholderColor, fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors
                    )
                    Text(
                        text = "${observacoes.length}/150",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 12.dp, bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FieldLabel(text: String, opcional: Boolean = false) {
    Row {
        Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = LabelColor)
        if (opcional) {
            Text(text = " (opcional)", fontSize = 14.sp, color = Color(0xFF888888))
        }
    }
}