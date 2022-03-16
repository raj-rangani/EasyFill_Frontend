package com.example.viewpagertest.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.database.models.FieldSpecifier
import com.example.database.models.Form
import com.example.database.models.FormField
import com.example.viewpagertest.models.*

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

            db.execSQL("CREATE TABLE Form (Id INTEGER PRIMARY KEY AUTOINCREMENT, author_name TEXT NOT NULL, form_name TEXT NOT NULL, field_spacing INTEGER NOT NULL, fill_percent INTEGER NOT NULL)")
            db.execSQL("CREATE TABLE Address (Id INTEGER PRIMARY KEY AUTOINCREMENT,street_line_1 TEXT NOT NULL, area TEXT NOT NULL,locality TEXT NOT NULL,houseNo TEXT NOT NULL,postOffice TEXT NOT NULL,state TEXT NOT NULL,district TEXT NOT NULL,subDistrict TEXT NOT NULL,city TEXT NOT NULL,pincode TEXT NOT NULL )")
            db.execSQL("CREATE TABLE FormField (Id INTEGER PRIMARY KEY AUTOINCREMENT, field_name TEXT NOT NULL, type TEXT NOT NULL)")
            db.execSQL("CREATE TABLE Name (Id INTEGER PRIMARY KEY AUTOINCREMENT, firstname TEXT NOT NULL, lastname TEXT NOT NULL, middlename TEXT NOT NULL, fullname TEXT NOT NULL)")
            db.execSQL("CREATE TABLE FieldSpecifier (Id INTEGER PRIMARY KEY AUTOINCREMENT, formId LONG NOT NULL, fieldId LONG NOT NULL, xaxis FLOAT NOT NULL, yaxis FLOAT NOT NULL, FOREIGN KEY(formId) REFERENCES Form(Id) ON DELETE CASCADE, FOREIGN KEY(fieldId) REFERENCES FormField(Id) ON DELETE CASCADE)")
            db.execSQL("CREATE TABLE Parent (Id INTEGER PRIMARY KEY AUTOINCREMENT, relation TEXT NOT NULL, nameId LONG NOT NULL, FOREIGN KEY(nameId) REFERENCES Name(Id) ON DELETE CASCADE)")
            db.execSQL("CREATE TABLE Profile (Id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL, contactNo TEXT NOT NULL, dob TEXT NOT NULL, gender TEXT NOT NULL, nameId LONG NOT NULL, addressId LONG NOT NULL, parentId LONG NOT NULL,FOREIGN KEY(nameId) REFERENCES Name(Id) ON DELETE CASCADE,FOREIGN KEY(addressId) REFERENCES Address(Id) ON DELETE CASCADE,FOREIGN KEY(parentId) REFERENCES Parent(Id) ON DELETE CASCADE)")

        } catch (ex: SQLiteException) {
            Log.e("MyDatabaseHelper", "Database already exists.")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun getFormData(id: Int?): ArrayList<Form> {
        var cursor: Cursor? = null
        if (id == null) {
            cursor = readableDatabase.query(
                "Form",
                arrayOf("*"),
                null,
                null,
                null,
                null,
                null
            )
        }
        if (id != null) {
            cursor = readableDatabase.query(
                "Form",
                arrayOf("*"),
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        }
        if (!(cursor!!.equals(null))) {
            cursor.moveToFirst()
            val arrayOfForm = arrayListOf<Form>()

            while (!cursor.isAfterLast()) {
                val form = Form(
                    id = cursor.getInt(0),
                    authorname = cursor.getString(1),
                    formname = cursor.getString(2),
                    fieldspacing = cursor.getInt(3),
                    fillpercent = cursor.getInt(3)
                )
                arrayOfForm.add(form)
                cursor.moveToNext()
            }
            cursor.close()
            return arrayOfForm
        }
        return arrayListOf()
    }

    fun insertForm(form: Form): Long {
        val values = ContentValues()
        values.put("author_name", form.authorname)
        values.put("field_spacing", form.fieldspacing)
        values.put("fill_percent", form.fillpercent)
        values.put("form_name", form.formname)

        return writableDatabase.insert("Form", null, values)
    }

    fun updateForm(form: Form, id: Int): Int {
        val values = ContentValues()
        values.put("author_name", form.authorname)
        values.put("field_spacing", form.fieldspacing)
        values.put("fill_percent", form.fillpercent)
        values.put("form_name", form.formname)

        return writableDatabase.update("Form", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteForm(id: Int): Int {
        return writableDatabase.delete("Form", "Id = ?", arrayOf(id.toString()))
    }

    fun getFormFieldData(id: Int?): ArrayList<FormField> {
        var cursor: Cursor? = null
        if (id == null) {
            cursor = readableDatabase.query(
                "FormField",
                arrayOf("*"),
                null,
                null,
                null,
                null,
                null
            )
        }

        if (id != null) {
            cursor = readableDatabase.query(
                "FormField",
                arrayOf("*"),
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        }

        if (!(cursor!!.equals(null))) {
            cursor.moveToFirst()
            val arrayOfFormField = arrayListOf<FormField>()

            while (!cursor.isAfterLast()) {
                val formField = FormField(
                    id = cursor.getInt(0),
                    fieldname = cursor.getString(1),
                    type = cursor.getString(2),
                )
                arrayOfFormField.add(formField)
                cursor.moveToNext()
            }
            cursor.close()
            return arrayOfFormField
        }
        return arrayListOf()
    }

    fun insertFormField(formField: FormField): Long {
        val values = ContentValues()
        values.put("fieldname", formField.fieldname)
        values.put("type", formField.type)

        return writableDatabase.insert("FormField", null, values)
    }

    fun updateFormField(formField: FormField, id: Int): Int {
        val values = ContentValues()
        values.put("fieldname", formField.fieldname)
        values.put("type", formField.type)

        return writableDatabase.update("FormField", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteFormField(id: Int): Int {
        return writableDatabase.delete("FormField", "Id = ?", arrayOf(id.toString()))
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

    fun getAddressData(id: Int?):ArrayList<Address>{
        var cursor: Cursor? = null
        if (id == null) {
        cursor = readableDatabase.query(
            "Address",
            arrayOf("*"),
            null,
            null,
            null,
            null,
            null
        )}

        if (id != null) {
            cursor = readableDatabase.query(
                "Address",
                arrayOf("*"),
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        }
        if (!(cursor!!.equals(null))) {
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
        return arrayListOf()
    }

    fun updateAddress(address: Address, id: Int): Int {
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

        return writableDatabase.update("Address", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteAddress(id: Int): Int {
        return writableDatabase.delete("Address", "Id = ?", arrayOf(id.toString()))
    }

    fun insertFieldSpecifier(fieldSpecifier: FieldSpecifier): Long {
        val values = ContentValues()

        values.put("formId", fieldSpecifier.form?.id)
        values.put("fieldId", fieldSpecifier.field?.id)
        values.put("xaxis", fieldSpecifier.xaxis)
        values.put("yaxis", fieldSpecifier.yaxis)

        return writableDatabase.insert("FieldSpecifier", null, values)
    }

    fun getFieldSpecifierData():ArrayList<FieldSpecifier>{

        val cursor = readableDatabase.query(
            "FieldSpecifier",
            arrayOf("*"),
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val arrayOfFieldSpecifier = arrayListOf<FieldSpecifier>()

        while (!cursor.isAfterLast()) {
            val form = getFormData(cursor.getInt(1))
            val formField = getFormFieldData(cursor.getInt(2))
            val fieldSpecifier = FieldSpecifier(
                id = cursor.getInt(0),
                form = form[0],
                field = formField[0],
                xaxis = cursor.getFloat(3),
                yaxis = cursor.getFloat(4)
            )
            arrayOfFieldSpecifier.add(fieldSpecifier)
            cursor.moveToNext()
        }
        cursor.close()
        return arrayOfFieldSpecifier
    }

    fun updateFieldSpecifier(fieldSpecifier: FieldSpecifier, id: Int): Int {
        val values = ContentValues()

        values.put("formId", fieldSpecifier.form?.id)
        values.put("fieldId", fieldSpecifier.field?.id)
        values.put("xaxis", fieldSpecifier.xaxis)
        values.put("yaxis", fieldSpecifier.yaxis)

        return writableDatabase.update("FieldSpecifier", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteFieldSpecifier(id: Int): Int {
        return writableDatabase.delete("FieldSpecifier", "Id = ?", arrayOf(id.toString()))
    }

    fun insertName(name: Name): Long {
        val values = ContentValues()
        values.put("firstname", name.firstname)
        values.put("lastname", name.lastname)
        values.put("middlename", name.middlename)
        values.put("fullname", name.fullname)

        return writableDatabase.insert("Name", null, values)
    }

    fun getNameData(id:Int?):ArrayList<Name>{
        var cursor: Cursor? = null
        if (id == null) {
        cursor = readableDatabase.query(
            "Name",
            arrayOf("*"),
            null,
            null,
            null,
            null,
            null
        )}
        if (id != null) {
            cursor = readableDatabase.query(
                "Name",
                arrayOf("*"),
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        }
        if (!(cursor!!.equals(null))) {
        cursor.moveToFirst()
        val arrayOfName = arrayListOf<Name>()

        while (!cursor.isAfterLast()) {
            val name = Name(
                id = cursor.getInt(0),
                firstname = cursor.getString(1),
                lastname = cursor.getString(2),
                middlename = cursor.getString(3),
                fullname = cursor.getString(4)
            )
                arrayOfName.add(name)
                cursor.moveToNext()
            }
            cursor.close()
            return arrayOfName
        }
            return arrayListOf()
    }


    fun updateName(name: Name, id: Int): Int {
        val values = ContentValues()
        values.put("firstname", name.firstname)
        values.put("lastname", name.lastname)
        values.put("middlename", name.middlename)
        values.put("fullname", name.fullname)

        return writableDatabase.update("Name", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteName(id: Int): Int {
        return writableDatabase.delete("Name", "Id = ?", arrayOf(id.toString()))
    }

    fun insertParent(parent: Parent): Long {
        val values = ContentValues()
        values.put("relation", parent.relation.relation)
        values.put("name", parent.name.id)

        return writableDatabase.insert("Parent", null, values)
    }

    fun getParentData(id:Int?):ArrayList<Parent>{
        var cursor: Cursor? = null
        if (id == null) {
            cursor = readableDatabase.query(
                "Parent",
                arrayOf("*"),
                null,
                null,
                null,
                null,
                null
            )
        }

        if (id != null) {
            cursor = readableDatabase.query(
                "Parent",
                arrayOf("*"),
                "id = ?",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        }
        if (!(cursor!!.equals(null))) {
            cursor.moveToFirst()
            val arrayOfParent = arrayListOf<Parent>()
            val name = getNameData(cursor.getInt(2))[0]

            while (!cursor.isAfterLast()) {
                val parent = Parent(
                    id = cursor.getInt(0),
                    relation = Relation.N,  //static relation
                    name = name
                )
                arrayOfParent.add(parent)
                cursor.moveToNext()
            }
            cursor.close()
            return arrayOfParent
        }
        return arrayListOf()
    }

    fun updateParent(parent: Parent, id: Int): Int {
        val values = ContentValues()
        values.put("relation", parent.relation.relation)
        values.put("name", parent.name.id)

        return writableDatabase.update("Parent", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteParent(id: Int): Int {
        return writableDatabase.delete("Parent", "Id = ?", arrayOf(id.toString()))
    }

    fun insertProfile(profile: Profile): Long {
        val values = ContentValues()
        values.put("relation", profile.parent.relation.relation)
        values.put("username", profile.username)
        values.put("password", profile.password)
        values.put("dob", profile.dob)
        values.put("contact_no", profile.contactNo)
        values.put("email", profile.email)
        values.put("gender", profile.gender)
        values.put("name",profile.name.id)
        values.put("address",profile.address.id)
        values.put("parent",profile.parent.id)

        return writableDatabase.insert("Parent", null, values)
    }

    fun getProfileData(id:Int?):ArrayList<Profile>{
        var cursor: Cursor? = null
        if (id == null) {
            cursor = readableDatabase.query(
                "Profile",
                arrayOf("*"),
                null,
                null,
                null,
                null,
                null
            )}
        if (id != null) {
            cursor = readableDatabase.query(
                "Profile",
                arrayOf("*"),
                "id = ?s",
                arrayOf(id.toString()),
                null,
                null,
                null
            )
        }
        if (!(cursor!!.equals(null))) {
            cursor.moveToFirst()
            val arrayOfProfile = arrayListOf<Profile>()
            val name = getNameData(cursor.getInt(7))[0]
            val address = getAddressData(cursor.getInt(8))[0]
            val parent = getParentData(cursor.getInt(9))[0]

            while (!cursor.isAfterLast()) {
                val profile = Profile(
                    id = cursor.getInt(0),
                    username = cursor.getString(1),
                    password = cursor.getString(2),
                    email = cursor.getString(3),
                    contactNo = cursor.getString(4),
                    dob = cursor.getString(5),
                    gender = cursor.getString(6),
                    name = name,
                    address = address,
                    parent = parent
                )
                arrayOfProfile.add(profile)
                cursor.moveToNext()
            }
            cursor.close()
            return arrayOfProfile
        }
        return arrayListOf()
    }

    fun updateProfile(profile: Profile, id: Int): Int {
        val values = ContentValues()
        values.put("relation", profile.parent.relation.relation)
        values.put("username", profile.username)
        values.put("password", profile.password)
        values.put("dob", profile.dob)
        values.put("contact_no", profile.contactNo)
        values.put("email", profile.email)
        values.put("gender", profile.gender)
        values.put("name",profile.name.id)
        values.put("address",profile.address.id)
        values.put("parent",profile.parent.id)

        return writableDatabase.update("Parent", values, "Id = ?", arrayOf(id.toString()))
    }

    fun deleteProfile(id: Int): Int {
        return writableDatabase.delete("Profile", "Id = ?", arrayOf(id.toString()))
    }

}