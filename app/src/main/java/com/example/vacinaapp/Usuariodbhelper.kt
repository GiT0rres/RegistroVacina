package com.example.vacinaapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuarioDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME    = "vacina.db"
        private const val DATABASE_VERSION = 2   // ← bumped para migration

        const val TABLE_USUARIOS = "usuarios"
        const val COL_ID         = "id"
        const val COL_NOME       = "nome"
        const val COL_USUARIO    = "usuario"
        const val COL_SENHA      = "senha"
        const val COL_FOTO_URI   = "foto_uri"    // ← novo: URI da foto da galeria
    }

    // ── Criação ───────────────────────────────────────────────────────────────

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_USUARIOS (
                $COL_ID       INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOME     TEXT NOT NULL,
                $COL_USUARIO  TEXT NOT NULL UNIQUE,
                $COL_SENHA    TEXT NOT NULL,
                $COL_FOTO_URI TEXT
            )
        """.trimIndent())
    }

    // ── Migração: adiciona coluna foto_uri se vier do banco v1 ────────────────

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_USUARIOS ADD COLUMN $COL_FOTO_URI TEXT")
        }
    }

    // ── Insert ────────────────────────────────────────────────────────────────

    fun addUsuario(nome: String, usuario: String, senha: String): Long {
        val db = writableDatabase
        val resultado = db.insert(TABLE_USUARIOS, null, ContentValues().apply {
            put(COL_NOME,    nome)
            put(COL_USUARIO, usuario)
            put(COL_SENHA,   senha)
        })
        db.close()
        return resultado
    }

    // ── Checks ────────────────────────────────────────────────────────────────

    fun usuarioExiste(usuario: String): Boolean {
        val db     = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_ID FROM $TABLE_USUARIOS WHERE $COL_USUARIO = ?",
            arrayOf(usuario)
        )
        val existe = cursor.moveToFirst()
        cursor.close(); db.close()
        return existe
    }

    fun validarLogin(usuario: String, senha: String): Boolean {
        val db     = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_ID FROM $TABLE_USUARIOS WHERE $COL_USUARIO = ? AND $COL_SENHA = ?",
            arrayOf(usuario, senha)
        )
        val valido = cursor.moveToFirst()
        cursor.close(); db.close()
        return valido
    }

    // ── Buscar ────────────────────────────────────────────────────────────────

    fun buscarUsuario(usuario: String): Map<String, String?>? {
        val db     = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USUARIOS WHERE $COL_USUARIO = ?",
            arrayOf(usuario)
        )
        return if (cursor.moveToFirst()) {
            val dados = mapOf(
                COL_ID       to cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)),
                COL_NOME     to cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)),
                COL_USUARIO  to cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO)),
                COL_FOTO_URI to cursor.getString(cursor.getColumnIndexOrThrow(COL_FOTO_URI))
                // senha omitida por segurança
            )
            cursor.close(); db.close()
            dados
        } else {
            cursor.close(); db.close()
            null
        }
    }

    // ── Updates ───────────────────────────────────────────────────────────────

    fun atualizarNome(usuario: String, novoNome: String): Boolean {
        val db = writableDatabase
        val rows = db.update(
            TABLE_USUARIOS,
            ContentValues().apply { put(COL_NOME, novoNome) },
            "$COL_USUARIO = ?", arrayOf(usuario)
        )
        db.close()
        return rows > 0
    }

    fun atualizarSenha(usuario: String, novaSenha: String): Boolean {
        val db = writableDatabase
        val rows = db.update(
            TABLE_USUARIOS,
            ContentValues().apply { put(COL_SENHA, novaSenha) },
            "$COL_USUARIO = ?", arrayOf(usuario)
        )
        db.close()
        return rows > 0
    }

    fun atualizarFotoUri(usuario: String, fotoUri: String): Boolean {
        val db = writableDatabase
        val rows = db.update(
            TABLE_USUARIOS,
            ContentValues().apply { put(COL_FOTO_URI, fotoUri) },
            "$COL_USUARIO = ?", arrayOf(usuario)
        )
        db.close()
        return rows > 0
    }

    // ── Update combinado (nome + senha + foto de uma vez) ─────────────────────

    fun atualizarPerfil(
        usuario: String,
        novoNome: String,
        novaSenha: String?,
        fotoUri: String?
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOME, novoNome)
            if (!novaSenha.isNullOrBlank()) put(COL_SENHA, novaSenha)
            if (fotoUri != null)            put(COL_FOTO_URI, fotoUri)
        }
        val rows = db.update(TABLE_USUARIOS, values, "$COL_USUARIO = ?", arrayOf(usuario))
        db.close()
        return rows > 0
    }
}