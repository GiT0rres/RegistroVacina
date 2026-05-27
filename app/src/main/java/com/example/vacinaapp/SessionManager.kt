package com.example.vacinaapp

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREFS_NAME   = "vacina_session"
    private const val KEY_USUARIO  = "usuario_logado"
    private const val KEY_NOME     = "nome_logado"

    fun salvarSessao(context: Context, usuario: String, nome: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_USUARIO, usuario)
            .putString(KEY_NOME, nome)
            .apply()
    }

    fun getUsuario(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USUARIO, "") ?: ""

    fun getNome(context: Context): String =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NOME, "") ?: ""

    fun limparSessao(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().clear().apply()
    }
}