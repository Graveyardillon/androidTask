package com.example.androidtask.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import androidx.core.view.get
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

        db.insert(DBContract.LabelEntry.TABLE_NAME, null, values)

        return true
    }

    fun loadLabels() : Map<String, MutableList<String>> {
        val db: SQLiteDatabase = readableDatabase
        val id_qql: String = "SELECT _id FROM " + DBContract.LabelEntry.TABLE_NAME
        val position_x_qql: String = "SELECT position_x FROM " + DBContract.LabelEntry.TABLE_NAME
        val position_y_qql: String = "SELECT position_y FROM " + DBContract.LabelEntry.TABLE_NAME
        val text_qql: String = "SELECT text FROM " + DBContract.LabelEntry.TABLE_NAME

        val iCursor: Cursor = db.rawQuery(id_qql, null)
        val xCursor: Cursor = db.rawQuery(position_x_qql, null)
        val yCursor: Cursor = db.rawQuery(position_y_qql, null)
        val tCursor: Cursor = db.rawQuery(text_qql, null)

        iCursor.moveToFirst()
        xCursor.moveToFirst()
        yCursor.moveToFirst()
        tCursor.moveToFirst()

        var id: MutableList<String> = mutableListOf()
        var x: MutableList<String> = mutableListOf()
        var y: MutableList<String> = mutableListOf()
        var text: MutableList<String> = mutableListOf()

        while(iCursor.moveToNext()) {
            xCursor.moveToNext()
            yCursor.moveToNext()
            tCursor.moveToNext()
            id.add(iCursor.getString(iCursor.getColumnIndex("_id")))
            x.add(xCursor.getString(xCursor.getColumnIndex("position_x")))
            y.add(yCursor.getString(yCursor.getColumnIndex("position_y")))
            text.add(tCursor.getString(tCursor.getColumnIndex("text")))
        }

        val content: Map<String, MutableList<String>> = mapOf("id" to id, "x" to x, "y" to y, "text" to text)

        println("content: " + content)

        iCursor.close()
        xCursor.close()
        yCursor.close()
        tCursor.close()

        return content
    }

    fun deleteLabel(v: View) : Boolean {
        val db = writableDatabase

        println(v.tag)
        db.delete(DBContract.LabelEntry.TABLE_NAME, "_id=?", arrayOf(v.tag.toString()))

        return true
    }

    fun updateLabel(v: View, id: Int) {
        val db = writableDatabase


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
        const val DATABASE_NAME = "LABEL.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.LabelEntry.TABLE_NAME + " ( " +
                    DBContract.LabelEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBContract.LabelEntry.POSITION_X + " TEXT, " +
                    DBContract.LabelEntry.POSITION_Y + " TEXT, " +
                    DBContract.LabelEntry.CONTENT_TEXT + " TEXT " +
                    ");"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.LabelEntry.TABLE_NAME
    }
}