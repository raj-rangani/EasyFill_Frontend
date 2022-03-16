package com.example.viewpagertest

import android.app.UiModeManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import  com.google.android.material.appbar.MaterialToolbar
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.database.models.FieldSpecifier
import com.example.viewpagertest.database.MyDatabaseHelper
import com.example.database.models.Form
import com.example.database.models.FormField
import com.example.viewpagertest.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import java.lang.Exception

class MainActivity : AppCompatActivity()
{
    private lateinit var frgCurrent: FragmentContainerView
    private lateinit var navBottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        insert_Profile()
        insert_Profile_father()
        insert_Profile_mother()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: Database Code Here
//        //database create start
//        try{
//        val dbHelper = MyDatabaseHelper(this)
//            val form = Form(null,"mother","rajbha", 1, 80)
//            val formID = dbHelper.insertForm(form)
//            val arrayOfForm :ArrayList<Form>
//            arrayOfForm = dbHelper.getFormData(null)
//
//            val formfield = FormField(null,"name","Parth","text")
//            val formfieldID = dbHelper.insertFormField(formfield)
//            val arrayOfFormfield :ArrayList<FormField>
//            arrayOfFormfield = dbHelper.getFormFieldData(null)
//
//            val fieldSpecifier =  FieldSpecifier(null,arrayOfForm[0],arrayOfFormfield[0],10.11f,10.12f)
//            val fieldSpecifierID = dbHelper.insertFieldSpecifier(fieldSpecifier)
//            val arrayOffieldspecifier :ArrayList<FieldSpecifier> = dbHelper.getFieldSpecifierData()
//
//
//
//            dbHelper.deleteForm(arrayOfForm[0].id!!)
//            Toast.makeText(this, arrayOfForm.toString(), Toast.LENGTH_SHORT).show()
//        }
//        catch(ex:Exception)
//        {
//            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
//        }

        //TODO: Till Here

//        val playlists = ()
//        val playlistsID = MyDatabaseHelper.insertPlaylist(playlists)
//
//        Toast.makeText(this, playlistsID.toString(), Toast.LENGTH_SHORT).show()

        //databse create end

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
    }

    override fun onResume() {
        setSelectedFragment(navBottom.selectedItemId)
        super.onResume()
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

    private fun insert_Profile(){
        val dbHelper = MyDatabaseHelper(this)

        val name = Name(null,"Rangani","Raj","J","Rangani Raj J")
        name.id = dbHelper.insertName(name).toInt()

        val address = Address(null,"7","Jay Society","Satyam Colony","khbar nathi","Ranjit nagar","Gujarat","Jamnagar","Jamnagar","jamnagar","361006")
        address.id = dbHelper.insertAddress(address).toInt()

        val parentName = Name(null,"Jayeshbhai","Rangani","V","Jayeshbhai V Rangani")
        parentName.id = dbHelper.insertName(parentName).toInt()

        val parent = Parent(null,Relation.F,parentName)
        parent.id = dbHelper.insertParent(parent).toInt()

        val profile = Profile(null,"RajRangani","0000","RajRangani@test.com","1234567890","17-10-2002","Male",name,address,parent)
        profile.id = dbHelper.insertProfile(profile).toInt()

//        Toast.makeText(this, dbHelper.getProfileData(profile.id).toString(), Toast.LENGTH_SHORT).show()
    }

    private fun insert_Profile_father(){
        val dbHelper = MyDatabaseHelper(this)

        val name = Name(null,"Jayeshbhai","Rangani","V","Jayeshbhai V Rangani")
        name.id = dbHelper.insertName(name).toInt()

        val address = Address(null,"7","Jay Society","Satyam Colony","khbar nathi","Ranjit nagar","Gujarat","Jamnagar","Jamnagar","jamnagar","361006")
        address.id = dbHelper.insertAddress(address).toInt()

        val parentName = Name(null,"Vinubhai","Rangani","R","Vinubhai R Rangani")
        parentName.id = dbHelper.insertName(parentName).toInt()

        val parent = Parent(null,Relation.F,parentName)
        parent.id = dbHelper.insertParent(parent).toInt()

        val profile = Profile(null,"JayeshbhaiRangani","0000","RajRangani0000@test.com","1234500000","01-01-2001","Male",name,address,parent)
        profile.id = dbHelper.insertProfile(profile).toInt()

//        Toast.makeText(this, dbHelper.getProfileData(profile.id).toString(), Toast.LENGTH_SHORT).show()
    }
    private fun insert_Profile_mother(){
        val dbHelper = MyDatabaseHelper(this)

        val name = Name(null,"Ushaben","Rangani","J","Ushaben J Rangani")
        name.id = dbHelper.insertName(name).toInt()

        val address = Address(null,"7","Jay Society","Satyam Colony","khbar nathi","Ranjit nagar","Gujarat","Jamnagar","Jamnagar","jamnagar","361006")
        address.id = dbHelper.insertAddress(address).toInt()

        val parentName = Name(null,"Jayeshbhai","Rangani","V","Jayeshbhai V Rangani")
        parentName.id = dbHelper.insertName(parentName).toInt()

        val parent = Parent(null,Relation.W,parentName)
        parent.id = dbHelper.insertParent(parent).toInt()

        val profile = Profile(null,"UshabenRangani","0000","UshaRangani0000@test.com","1234500000","01-01-2001","Female",name,address,parent)
        profile.id = dbHelper.insertProfile(profile).toInt()

//        Toast.makeText(this, dbHelper.getProfileData(profile.id).toString(), Toast.LENGTH_SHORT).show()
    }


}