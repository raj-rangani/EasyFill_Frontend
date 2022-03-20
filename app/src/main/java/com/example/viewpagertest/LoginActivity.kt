package com.example.viewpagertest

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.viewpagertest.api.LoginApi
import com.example.viewpagertest.api.ProfileApi
import com.example.viewpagertest.database.MyDatabaseHelper
import com.example.viewpagertest.helper.Constant
import com.example.viewpagertest.helper.Helper
import com.example.viewpagertest.models.Name
import com.example.viewpagertest.models.Parent
import com.example.viewpagertest.models.Profile
import com.example.viewpagertest.models.Relation
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        findViewById<Button>(R.id.login).setOnClickListener {
            val progress = Helper.showProgress(this,"Logging In ...")
            CoroutineScope(Dispatchers.IO).launch {
                val response = LoginApi.login(etUsername.text.toString(), etPassword.text.toString())

                withContext(Dispatchers.Main) {
                    if(response[0].toString().toBoolean()) {
                        Toast.makeText(this@LoginActivity, "Logged In", Toast.LENGTH_SHORT).show()
                        progress.hide()
                        val finalProgress = Helper.showProgress(this@LoginActivity,"Fetching Details...")
                        val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
                        val edit = authPrefs.edit()
                        edit.putBoolean("LOGIN", true)
                        edit.putString("TOKEN", response[1].toString())
                        edit.apply()
                        CoroutineScope(Dispatchers.IO).launch {
                            val profile = ProfileApi.getProfile(response[1].toString())
                            val profileImage = ProfileApi.getProfileImage(response[1].toString())

                            addProfile(profile!!)
                            addFatherProfile(profile)
                            addMotherProfile(profile)

                            withContext(Dispatchers.Main) {
                                val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                                val profileEdit = prefsProfile.edit()
                                profileEdit.putString("Profile", Gson().toJson(profile))
                                profileEdit.apply()

                                withContext(Dispatchers.Main) {
                                    try {
                                        val file = openFileOutput("Avatar.png", MODE_PRIVATE)
                                        profileImage?.compress(Bitmap.CompressFormat.PNG,100, file)
                                    } catch (Ex: Exception) {
                                        Log.i("Profile Error", Ex.message.toString())
                                    }
                                }

                                finalProgress.hide()
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                        progress.hide()
                    }
                }
            }
        }

        findViewById<LinearLayout>(R.id.signUp).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private fun addProfile(profile: Profile){
        val dbHelper = MyDatabaseHelper(this)

        val name = profile.name
        name.id = dbHelper.insertName(name).toInt()

        val address = profile.address
        address.id = dbHelper.insertAddress(address).toInt()

        val parentName = profile.parent.name
        profile.parent.name.id = dbHelper.insertName(parentName).toInt()

        val parent = profile.parent
        parent.id = dbHelper.insertParent(parent).toInt()

        val addProfile = Profile(null, profile.username, "", profile.email, profile.contactNo, profile.dob, profile.gender,name,address,parent)
        profile.id = dbHelper.insertProfile(addProfile).toInt()

        dbHelper.close()
    }

    private fun addFatherProfile(profile: Profile){
        val dbHelper = MyDatabaseHelper(this)

        val name = Name(null,"[No Data]","[No Data]","[No Data]","[No Data]")
        name.id = dbHelper.insertName(name).toInt()

        val address = profile.address
        address.id = dbHelper.insertAddress(address).toInt()

        val parentName = Name(null,"[No Data]","[No Data]","[No Data]","[No Data]")
        parentName.id = dbHelper.insertName(parentName).toInt()

        val parent = Parent(null, Relation.N,parentName)
        parent.id = dbHelper.insertParent(parent).toInt()

        val addProfile = Profile(null,"[No Data]","[No Data]","[No Data]","[No Data]","[No Data]","[No Data]",name,address,parent)
        profile.id = dbHelper.insertProfile(addProfile).toInt()

        dbHelper.close()
    }
    private fun addMotherProfile(profile: Profile){
        val dbHelper = MyDatabaseHelper(this)

        val name = Name(null,"[No Data]","[No Data]","[No Data]","[No Data]")
        name.id = dbHelper.insertName(name).toInt()

        val address = profile.address
        address.id = dbHelper.insertAddress(address).toInt()

        val parentName = Name(null,"[No Data]","[No Data]","[No Data]","[No Data]")
        parentName.id = dbHelper.insertName(parentName).toInt()

        val parent = Parent(null, Relation.N,parentName)
        parent.id = dbHelper.insertParent(parent).toInt()

        val addProfile = Profile(null,"[No Data]","[No Data]","[No Data]","[No Data]","[No Data]","Female",name,address,parent)
        profile.id = dbHelper.insertProfile(addProfile).toInt()
        dbHelper.close()
    }
}