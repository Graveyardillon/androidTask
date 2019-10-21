package com.example.androidtask.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.androidtask.models.TitleModel

class TitleDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    @Throws(SQLiteException::class)
    fun insertTitle(title: TitleModel) : Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.TitleEntry.TITLE_TEXT, title.title)
        values.put(DBContract.TitleEntry.TITLE_ID, title.id)

        val rowId = db.insert(DBContract.TitleEntry.TABLE_NAME, null, values)

        return true
    }

    fun updateTitle(title: TitleModel) : Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.TitleEntry.TITLE_TEXT, title.title)
        values.put(DBContract.TitleEntry.TITLE_ID, title.id)

        db.update(DBContract.TitleEntry.TABLE_NAME, values, "title_id=?", arrayOf("0"))

        return true
    }

    fun getRecordNum() : Int {
        val db: SQLiteDatabase = readableDatabase

        val recordCount = DatabaseUtils.queryNumEntries(db, DBContract.TitleEntry.TABLE_NAME)

        return recordCount.toInt()
    }

    fun loadRecord(): String {
        val db: SQLiteDatabase = readableDatabase
        val selectQql: String = "SELECT title_text FROM " + DBContract.TitleEntry.TABLE_NAME + " where title_id=? "

        val cursor: Cursor = db.rawQuery(selectQql, arrayOf("0"))
        var title = ""

        try {
            if(cursor.moveToNext()) {
                title = cursor.getString(cursor.getColumnIndex("title_text"))
            }
        } finally {
            cursor.close()
        }

        println(title)

        return title
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
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "TITLE.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.TitleEntry.TABLE_NAME + "(" +
                    DBContract.TitleEntry.TITLE_TEXT + " TEXT, " +
                    DBContract.TitleEntry.TITLE_ID + " INTEGER " +
                    ");"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.TitleEntry.TABLE_NAME
    }
}