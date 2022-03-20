package com.example.viewpagertest

import android.app.UiModeManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import  com.google.android.material.appbar.MaterialToolbar
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.viewpagertest.api.ProfileApi
import com.example.viewpagertest.database.MyDatabaseHelper
import com.example.viewpagertest.helper.Constant
import com.example.viewpagertest.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity()
{
    private lateinit var frgCurrent: FragmentContainerView
    private lateinit var navBottom: BottomNavigationView
    private lateinit var wishMeText: TextView
    private lateinit var profileAvatar: ImageView
    private lateinit var username: TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wishMeText = findViewById(R.id.wishMeText)
        wishMeText.text = getWishes()

        username = findViewById(R.id.username)

        profileAvatar = findViewById(R.id.profile)

        frgCurrent = findViewById(R.id.frgCurrent)
        setCurrentFragment(HomeFragment.INSTANCE)

        navBottom = findViewById(R.id.navBottom)
        navBottom.setOnItemSelectedListener(::bottomNavItemClicked)
        navBottom.selectedItemId

        val colorMode = findViewById<MaterialButton>(R.id.uiMode)
        val actionBar = findViewById<MaterialToolbar>(R.id.appBar)

        val prefs = getSharedPreferences("COLOR_MODE", MODE_PRIVATE)
        val mode = prefs.getString("Mode", "Default")

        if(mode.equals("Default")) {
            val edit = prefs.edit()
            val uiMode = applicationContext.getSystemService(UI_MODE_SERVICE) as UiModeManager

            when(uiMode.nightMode) {
                UiModeManager.MODE_NIGHT_YES -> {
                    setDefaultNightMode(MODE_NIGHT_YES)
                    edit.putString("Mode", "Dark")
                    edit.apply()
                    colorMode.setIconResource(R.drawable.ic_dark)
                }
                UiModeManager.MODE_NIGHT_NO -> {
                    setDefaultNightMode(MODE_NIGHT_NO)
                    edit.putString("Mode", "Light")
                    edit.apply()
                    colorMode.setIconResource(R.drawable.ic_light)
                }
            }
        }

        else if (mode.equals("Dark")) {
            setDefaultNightMode(MODE_NIGHT_YES)
            colorMode.setIconResource(R.drawable.ic_dark)
        }

        else {
            setDefaultNightMode(MODE_NIGHT_NO)
            colorMode.setIconResource(R.drawable.ic_light)
        }

        colorMode.setOnClickListener {
            val edit = prefs.edit()
            if(mode.equals("Dark")) {
                setDefaultNightMode(MODE_NIGHT_NO)
                colorMode.setIconResource(R.drawable.ic_light)
                edit.putString("Mode", "Light")
                edit.apply()
            } else {
                setDefaultNightMode(MODE_NIGHT_YES)
                colorMode.setIconResource(R.drawable.ic_dark)
                edit.putString("Mode", "Dark")
                edit.apply()
            }
        }

        actionBar.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Load All data to profiles

        val databasePref = getSharedPreferences("DATABASE", MODE_PRIVATE)
        val field = databasePref.getString("Add", "FALSE")

        if(field.equals("FALSE")) {
            addFields()
        }

        loadToCache()
    }

    override fun onResume() {
        setSelectedFragment(navBottom.selectedItemId)
        super.onResume()

        val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
        val auth = authPrefs.getBoolean("LOGIN", false)

        if(!auth) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
        val profileJson = prefsProfile.getString("Profile", "[NO DATA]")
        if(!(profileJson.equals("[NO DATA]"))) {
            if(!(profileJson.equals(null))) {
                val profile = Gson().fromJson(profileJson, Profile::class.java)
                username.text = profile.username

                val image = openFileInput("Avatar.png")
                val bitmap = BitmapFactory.decodeStream(image)

                if(bitmap != null) {
                    profileAvatar.setImageBitmap(bitmap)
                } else {
                    profileAvatar.setImageResource(R.drawable.default_profile)
                }
            } else {
                username.text = "Anonymous"
                profileAvatar.setImageResource(R.drawable.default_profile)
            }

        }
    }

    private fun setSelectedFragment(selectedItemID: Int)
    {
        when (selectedItemID)
        {
            R.id.menuHome -> setCurrentFragment(HomeFragment.INSTANCE)
            R.id.menuPdf -> setCurrentFragment(PdfFragment.INSTANCE)
            R.id.menuProfileGroup -> setCurrentFragment(ProfileGroupFragment.INSTANCE)
            R.id.menuAddform -> setCurrentFragment(AddformFragment.INSTANCE)
        }
    }

    private fun bottomNavItemClicked(menuItem: MenuItem): Boolean
    {
        when (menuItem.itemId)
        {
            R.id.menuHome -> setCurrentFragment(HomeFragment.INSTANCE)
            R.id.menuPdf -> setCurrentFragment(PdfFragment.INSTANCE)
            R.id.menuProfileGroup -> setCurrentFragment(ProfileGroupFragment.INSTANCE)
            R.id.menuAddform -> setCurrentFragment(AddformFragment.INSTANCE)
            else -> return false
        }

        return true
    }

    private fun setCurrentFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frgCurrent, fragment)
            commit()
        }
    }


    private fun getWishes(): String
    {
        val cal = Calendar.getInstance()
        val hour =  cal.get(Calendar.HOUR_OF_DAY).toInt()

        if (hour in 4..12) {return "GOOD MORNING,"}
        else if (hour in 12..16) {return "GOOD AFTERNOON,"}
        else if (hour in 16..20) {return "GOOD EVENING,"}
        else {return "GOOD NIGHT,"}
    }

    private fun loadToCache() {
        val assetsList = assets.list("")

        assetsList?.forEach {
            if(it.toString() == "images" || it.toString() == "webkit") {
                return@forEach
            }

            val file = File(cacheDir, it.toString())
            if (!file.exists()) {
                val asset: InputStream = assets.open(it.toString())
                val output = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var size: Int
                while (asset.read(buffer).also { size = it } != -1) {
                    output.write(buffer, 0, size)
                }
                asset.close()
                output.close()
            }
        }
    }

    private fun addFields() {
        val fields = Constant.Fields.AADHAR_FIELD_NAMES
        for (field in fields) {
            val fieldName = field.split("@")[0]
            val fieldType = field.split("@")[1].split(":")[0].split("+")[0]
            val formField = FormField(null, fieldName, fieldType)

            val dbHelper = MyDatabaseHelper(this)
            dbHelper.insertFormField(formField)
            dbHelper.close()
        }

        val prefs = getSharedPreferences("DATABASE", MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString("Add", "TRUE")
        edit.apply()
    }
}