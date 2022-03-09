package com.example.viewpagertest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomsheet.BottomSheetDialog

import android.app.Dialog
import android.graphics.Color

import android.graphics.drawable.ColorDrawable
import android.view.*

import android.widget.Toast

import android.widget.LinearLayout




class ProfileActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = findViewById<MaterialCardView>(R.id.userCard)

        user.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        val editLayout: LinearLayout = dialog.findViewById(R.id.layoutEdit)
        val shareLayout: LinearLayout = dialog.findViewById(R.id.layoutShare)
        val uploadLayout: LinearLayout = dialog.findViewById(R.id.layoutUpload)
        val printLayout: LinearLayout = dialog.findViewById(R.id.layoutPrint)
        editLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Edit is Clicked", Toast.LENGTH_SHORT).show()
        }
        shareLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Share is Clicked", Toast.LENGTH_SHORT).show()
        }
        uploadLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Upload is Clicked", Toast.LENGTH_SHORT).show()
        }
        printLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(this, "Print is Clicked", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}