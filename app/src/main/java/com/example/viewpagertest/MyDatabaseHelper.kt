package com.example.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.database.models.Form
import kotlin.coroutines.coroutineContext

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, MyDatabaseHelper.DATABASE_NAME, null, MyDatabaseHelper.DATABASE_VERSION) {
    companion object {
        @JvmStatic
        val DATABASE_NAME = "easyfillv1"

        @JvmStatic
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {

            if (db == null)
                return

            db.execSQL("CREATE TABLE Form (Id INTEGER PRIMARY KEY AUTOINCREMENT, authorname TEXT NOT NULL, username TEXT NOT NULL, fieldspacing INTEGER NOT NULL, fillpercent INTEGER NOT NULL)")
            db.execSQL("INSERT INTO Form (authorname, username, fieldspacing, fillpercent) VALUES ('mother','rajbha', 1, 80)")


        } catch (ex: SQLiteException) {
            Log.e("MyDatabaseHelper","Database already exists.")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

//    fun getPlaylist(id: Int): Form {
//        val cursor = readableDatabase.query(
//            "Playlists",
//            arrayOf("*"),
//            "Id = ?",
//            arrayOf(id.toString()),
//            null,
//            null,
//            null
//        )
//        cursor.moveToFirst()

//        val playlistId = cursor.getInt(0)
//        val playlistName = cursor.getString(1)
//
//        cursor.close()
//
//        return Form(playlistId, playlistName)
//    }

    fun insertForm(form: Form): Long {
        val values = ContentValues()
        values.put("id", form.id)
        values.put("authorname", form.authorname)
        values.put("fieldspacing", form.fieldspacing)
        values.put("fillpercent", form.fillpercent)
        values.put("username", form.username)

        return writableDatabase.insert("Playlists", null, values)
    }
//
//    fun updatePlaylists(playlist: Form, id: Int): Int {
//        val values = ContentValues()
//        values.put("Name", playlist.name)
//
//        return writableDatabase.update("Playlists", values, "Id = ?", arrayOf(id.toString()))
//    }
//
//    fun deletePlaylists(id: Int): Int {
//        return writableDatabase.delete("Playlists", "Id = ?", arrayOf(id.toString()))
//    }
}