package com.example.viewpagertest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomsheet.BottomSheetDialog

import android.app.Dialog
import android.graphics.Color

import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Button
import android.widget.ImageView

import android.widget.Toast

import android.widget.LinearLayout

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = findViewById<MaterialCardView>(R.id.userCard)
        val back = findViewById<Button>(R.id.back)
        val image = findViewById<ImageView>(R.id.profileImage)

        image.setOnClickListener{
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()
        }
        back.setOnClickListener {
            finish()
        }

        user.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_user_update_layout)
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}