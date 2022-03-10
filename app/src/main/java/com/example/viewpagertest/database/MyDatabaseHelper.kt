package com.example.viewpagertest.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.database.models.FieldSpecifier
import com.example.database.models.Form
import com.example.viewpagertest.models.Address

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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
            db.execSQL("CREATE TABLE Address (Id INTEGER PRIMARY KEY AUTOINCREMENT,street_line_1 TEXT NOT NULL, area TEXT NOT NULL,locality TEXT NOT NULL,houseNo TEXT NOT NULL,postOffice TEXT NOT NULL,state TEXT NOT NULL,district TEXT NOT NULL,subDistrict TEXT NOT NULL,city TEXT NOT NULL,pincode TEXT NOT NULL )")
            db.execSQL("CREATE TABLE FormField (Id INTEGER PRIMARY KEY AUTOINCREMENT, fieldname TEXT NOT NULL, fielddata TEXT NOT NULL, type TEXT NOT NULL)")
            db.execSQL("CREATE TABLE Name (Id INTEGER PRIMARY KEY AUTOINCREMENT, firstname TEXT NOT NULL, lastname TEXT NOT NULL, middlename TEXT NOT NULL, fullname TEXT NOT NULL)")
            db.execSQL("CREATE TABLE FieldSpecifier (Id INTEGER PRIMARY KEY AUTOINCREMENT, formId LONG NOT NULL, fieldId LONG NOT NULL, xaxis FLOAT NOT NULL, yaxis FLOAT NOT NULL, FOREIGN KEY(formId) REFERENCES Form(Id), FOREIGN KEY(fieldId) REFERENCES FormField(Id))")
            db.execSQL("CREATE TABLE Parent (Id INTEGER PRIMARY KEY AUTOINCREMENT, relation TEXT NOT NULL, nameId LONG NOT NULL, FOREIGN KEY(nameId) REFERENCES Name(Id))")
            db.execSQL("CREATE TABLE Profile (Id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL, contactNo TEXT NOT NULL, dob TEXT NOT NULL, gender TEXT NOT NULL, nameId LONG NOT NULL, addressId LONG NOT NULL, parentId LONG NOT NULL,FOREIGN KEY(nameId) REFERENCES Name(Id),FOREIGN KEY(addressId) REFERENCES Address(Id),FOREIGN KEY(parentId) REFERENCES Parent(Id))")

            db.execSQL("INSERT INTO Form (authorname,username,fieldspacing,fillpercent) VALUES ('raj','rajbha',20,99)")

        } catch (ex: SQLiteException) {
            Log.e("MyDatabaseHelper","Database already exists.")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun getFormData():ArrayList<Form>{
        val cursor = readableDatabase.query(
            "Form",
            arrayOf("*"),
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val arrayOfForm = arrayListOf<Form>()

        while (!cursor.isAfterLast()) {
            val form = Form(
                id = cursor.getInt(0),
                authorname = cursor.getString(1),
                username = cursor.getString(2),
                fieldspacing = cursor.getInt(3),
                fillpercent = cursor.getInt(3)
            )
            arrayOfForm.add(form)
            cursor.moveToNext()
        }
        cursor.close()
        return arrayOfForm
    }

    fun insertForm(form: Form): Long {
        val values = ContentValues()
        values.put("authorname", form.authorname)
        values.put("fieldspacing", form.fieldspacing)
        values.put("fillpercent", form.fillpercent)
        values.put("username", form.username)

        return writableDatabase.insert("Form", null, values)
    }
//
//    fun updatePlaylists(playlist: Form, id: Int): Int {
//        val values = ContentValues()
//        values.put("Name", playlist.name)
//
//        return writableDatabase.update("Playlists", values, "Id = ?", arrayOf(id.toString()))
//    }
//
    fun deleteForm(id: Int): Int {
        return writableDatabase.delete("Form", "Id = ?", arrayOf(id.toString()))
    }

    fun insertAddress(address: Address): Long {
        val values = ContentValues()
        values.put("street_line_1", address.street_line_1)
        values.put("area", address.area)
        values.put("locality", address.locality)
        values.put("houseNo", address.houseNo)
        values.put("postOffice", address.postOffice)
        values.put("state", address.state)
        values.put("district", address.district)
        values.put("subDistrict", address.subDistrict)
        values.put("city", address.city)
        values.put("pincode", address.pincode)

        return writableDatabase.insert("Address", null, values)
    }
    fun getAddressData():ArrayList<Address>{
        val cursor = readableDatabase.query(
            "Address",
            arrayOf("*"),
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val arrayOfAddress = arrayListOf<Address>()

        while (!cursor.isAfterLast()) {
            val address = Address(
                id = cursor.getInt(0),
                street_line_1 = cursor.getString(1),
                area = cursor.getString(2),
                locality = cursor.getString(3),
                houseNo = cursor.getString(4),
                postOffice = cursor.getString(5),
                state = cursor.getString(6),
                district = cursor.getString(7),
                subDistrict = cursor.getString(8),
                city = cursor.getString(9),
                pincode = cursor.getString(10)
            )
            arrayOfAddress.add(address)
            cursor.moveToNext()
        }
        cursor.close()
        return arrayOfAddress
    }
    fun deleteAddress(id: Int): Int {
        return writableDatabase.delete("Address", "Id = ?", arrayOf(id.toString()))
    }

    fun insertFieldSpecifier(address: FieldSpecifier): Long {
        val values = ContentValues()
//        values.put("form", address.form)
//        values.put("field", address.field)
        values.put("xaxis", address.xaxis)
        values.put("yaxis", address.yaxis)

        return writableDatabase.insert("Address", null, values)
    }
//    fun getFieldSpecifierData():ArrayList<Address>{
//        val cursor = readableDatabase.query(
//            "Address",
//            arrayOf("*"),
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        cursor.moveToFirst()
//        val arrayOfAddress = arrayListOf<Address>()
//
//        while (!cursor.isAfterLast()) {
//            val address = Address(
//                id = cursor.getInt(0),
//                street_line_1 = cursor.getString(1),
//                area = cursor.getString(2),
//                locality = cursor.getString(3),
//                houseNo = cursor.getString(4),
//                postOffice = cursor.getString(5),
//                state = cursor.getString(6),
//                district = cursor.getString(7),
//                subDistrict = cursor.getString(8),
//                city = cursor.getString(9),
//                pincode = cursor.getString(10)
//            )
//            arrayOfAddress.add(address)
//            cursor.moveToNext()
//        }
//        cursor.close()
//        return arrayOfAddress
//    }
//    fun deleteAddress(id: Int): Int {
//        return writableDatabase.delete("Address", "Id = ?", arrayOf(id.toString()))
//    }
}