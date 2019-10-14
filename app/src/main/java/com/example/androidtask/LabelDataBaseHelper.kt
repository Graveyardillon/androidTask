package com.example.androidtask

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.androidtask.storage.DBContract

class LabelDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    @Throws(SQLiteException::class)
    fun insertLabel(label: LabelModel) : Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.LabelEntry.POSITION_X, label.position_x)
        values.put(DBContract.LabelEntry.POSITION_Y, label.position_y)
        values.put(DBContract.LabelEntry.CONTENT_TEXT, label.text)
        values.put(DBContract.LabelEntry.TYPE, label.type)

        val rowId = db.insert(DBContract.LabelEntry.TABLE_NAME, null, values)

        return true
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "LABEL.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.LabelEntry.TABLE_NAME + " ( " +
                    DBContract.LabelEntry.POSITION_X + " TEXT, " +
                    DBContract.LabelEntry.POSITION_Y + " TEXT, " +
                    DBContract.LabelEntry.CONTENT_TEXT + " TEXT, " +
                    DBContract.LabelEntry.TYPE + " INTEGER " +
                    ");"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.LabelEntry.TABLE_NAME
    }
}