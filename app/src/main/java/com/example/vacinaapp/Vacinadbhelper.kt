package com.example.vacinaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VacinaDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOME_COL  + " TEXT, "
                + DATA_COL  + " TEXT, "
                + LOTE_COL  + " TEXT, "
                + LOCAL_COL + " TEXT, "
                + OBS_COL   + " TEXT"
                + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }



    fun addVacina(nome: String, data: String, lote: String, local: String, obs: String) {
        val values = ContentValues()
        values.put(NOME_COL,  nome)
        values.put(DATA_COL,  data)
        values.put(LOTE_COL,  lote)
        values.put(LOCAL_COL, local)
        values.put(OBS_COL,   obs)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getAllVacinas(): List<VacinaModel> {
        val vacinas = mutableListOf<VacinaModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                vacinas.add(
                    VacinaModel(
                        id    = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL)),
                        nome  = cursor.getString(cursor.getColumnIndexOrThrow(NOME_COL)),
                        data  = cursor.getString(cursor.getColumnIndexOrThrow(DATA_COL)),
                        lote  = cursor.getString(cursor.getColumnIndexOrThrow(LOTE_COL)),
                        local = cursor.getString(cursor.getColumnIndexOrThrow(LOCAL_COL)),
                        obs   = cursor.getString(cursor.getColumnIndexOrThrow(OBS_COL))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return vacinas
    }



    fun getVacinaById(id: Int): VacinaModel? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $ID_COL = ?",
            arrayOf(id.toString())
        )
        var vacina: VacinaModel? = null
        if (cursor.moveToFirst()) {
            vacina = VacinaModel(
                id    = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COL)),
                nome  = cursor.getString(cursor.getColumnIndexOrThrow(NOME_COL)),
                data  = cursor.getString(cursor.getColumnIndexOrThrow(DATA_COL)),
                lote  = cursor.getString(cursor.getColumnIndexOrThrow(LOTE_COL)),
                local = cursor.getString(cursor.getColumnIndexOrThrow(LOCAL_COL)),
                obs   = cursor.getString(cursor.getColumnIndexOrThrow(OBS_COL))
            )
        }
        cursor.close()
        db.close()
        return vacina
    }



    fun updateVacina(id: Int, nome: String, data: String, lote: String, local: String, obs: String) {
        val values = ContentValues()
        values.put(NOME_COL,  nome)
        values.put(DATA_COL,  data)
        values.put(LOTE_COL,  lote)
        values.put(LOCAL_COL, local)
        values.put(OBS_COL,   obs)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }


    fun deleteVacina(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }



    companion object {
        private val DATABASE_NAME    = "VacinaDB"
        private val DATABASE_VERSION = 1

        val TABLE_NAME = "vacinas"
        val ID_COL     = "id"
        val NOME_COL   = "nome"
        val DATA_COL   = "data"
        val LOTE_COL   = "lote"
        val LOCAL_COL  = "local"
        val OBS_COL    = "obs"
    }
}