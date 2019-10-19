package com.example.androidtask.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.androidtask.LabelView
import com.example.androidtask.storage.DBContract
import com.example.androidtask.models.LabelModel

class LabelDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    @Throws(SQLiteException::class)
    fun insertLabel(label: LabelModel) : Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.LabelEntry.POSITION_X, label.position_x)
        values.put(DBContract.LabelEntry.POSITION_Y, label.position_y)
        values.put(DBContract.LabelEntry.CONTENT_TEXT, label.text)

        val rowId = db.insert(DBContract.LabelEntry.TABLE_NAME, null, values)

        return true
    }

    fun loadLabels() : Map<String, MutableList<String>> {
        val db: SQLiteDatabase = readableDatabase
        val position_x_qql: String = "SELECT position_x FROM " + DBContract.LabelEntry.TABLE_NAME
        val position_y_qql: String = "SELECT position_y FROM " + DBContract.LabelEntry.TABLE_NAME
        val text_qql: String = "SELECT text FROM " + DBContract.LabelEntry.TABLE_NAME

        val xCursor: Cursor = db.rawQuery(position_x_qql, null)
        val yCursor: Cursor = db.rawQuery(position_y_qql, null)
        val tCursor: Cursor = db.rawQuery(text_qql, null)

        xCursor.moveToFirst()
        yCursor.moveToFirst()
        tCursor.moveToFirst()

        var x: MutableList<String> = mutableListOf()
        var y: MutableList<String> = mutableListOf()
        var text: MutableList<String> = mutableListOf()

        while(xCursor.moveToNext()) {
            yCursor.moveToNext()
            x.add(xCursor.getString(xCursor.getColumnIndex("position_x")))
            y.add(yCursor.getString(yCursor.getColumnIndex("position_y")))
            text.add(tCursor.getString(tCursor.getColumnIndex("text")))
        }

        val content: Map<String, MutableList<String>> = mapOf("x" to x, "y" to y, "text" to text)

        println("content: " + content)

        xCursor.close()
        yCursor.close()
        tCursor.close()

        return content
    }

    fun deleteLabel() {

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
                    DBContract.LabelEntry.CONTENT_TEXT + " TEXT " +
                    ");"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.LabelEntry.TABLE_NAME
    }
}