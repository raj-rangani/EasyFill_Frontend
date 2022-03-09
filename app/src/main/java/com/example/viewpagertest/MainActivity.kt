package com.example.viewpagertest

import android.app.UiModeManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.SharedLibraryInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import  com.google.android.material.appbar.MaterialToolbar
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{
    private lateinit var frgCurrent: FragmentContainerView
    private lateinit var navBottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frgCurrent = findViewById(R.id.frgCurrent)
        setCurrentFragment(HomeFragment.INSTANCE)

        navBottom = findViewById(R.id.navBottom)
        navBottom.setOnItemSelectedListener(::bottomNavItemClicked)

        val colorMode = findViewById<Button>(R.id.uiMode)
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
                }
                UiModeManager.MODE_NIGHT_NO -> {
                    setDefaultNightMode(MODE_NIGHT_NO)
                    edit.putString("Mode", "Light")
                    edit.apply()
                }
            }
        }

        else if (mode.equals("Dark")) {
            setDefaultNightMode(MODE_NIGHT_YES)
        }

        else {
            setDefaultNightMode(MODE_NIGHT_NO)
        }

        colorMode.setOnClickListener {
            val edit = prefs.edit()
            if(mode.equals("Dark")) {
                setDefaultNightMode(MODE_NIGHT_NO)
                edit.putString("Mode", "Light")
                edit.apply()
            } else {
                setDefaultNightMode(MODE_NIGHT_YES)
                edit.putString("Mode", "Dark")
                edit.apply()
            }
        }
        
        actionBar.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bottomNavItemClicked(menuItem: MenuItem): Boolean
    {
        when (menuItem.itemId)
        {
            R.id.menuHome -> setCurrentFragment(HomeFragment.INSTANCE)
            R.id.menuCart -> setCurrentFragment(CartFragment.INSTANCE)
            R.id.menuProfile -> setCurrentFragment(ProfileFragment.INSTANCE)
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
}