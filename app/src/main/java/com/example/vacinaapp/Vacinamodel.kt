package com.example.vacinaapp

data class VacinaModel(
    val id: Int = 0,
    val nome: String,
    val data: String,
    val lote: String,
    val local: String,
    val obs: String
)