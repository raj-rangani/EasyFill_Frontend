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
import android.widget.*

import com.example.viewpagertest.api.ProfileApi
import com.example.viewpagertest.models.Profile
import com.example.viewpagertest.models.Relation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = findViewById<MaterialCardView>(R.id.userCard)
        val back = findViewById<Button>(R.id.back)
        val image = findViewById<ImageView>(R.id.profileImage)

        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val tvDob = findViewById<TextView>(R.id.tvDob)
        val tvArea = findViewById<TextView>(R.id.tvArea)
        val tvLocality = findViewById<TextView>(R.id.tvLocality)
        val tvStreetLine1 = findViewById<TextView>(R.id.tvStreetline1)
        val tvCity = findViewById<TextView>(R.id.tvCity)
        val tvDistrict = findViewById<TextView>(R.id.tvDistrict)
        val tvSubDistrict = findViewById<TextView>(R.id.tvSubdistrict)
        val tvState = findViewById<TextView>(R.id.tvState)
        val tvHouseNo = findViewById<TextView>(R.id.tvHouseno)
        val tvPincode = findViewById<TextView>(R.id.tvPincode)
        val tvPostoffice = findViewById<TextView>(R.id.tvPostoffice)
        val tvFirstname = findViewById<TextView>(R.id.tvFirstname)
        val tvLastname = findViewById<TextView>(R.id.tvLastname)
        val tvMiddlename = findViewById<TextView>(R.id.tvMiddlename)
        val tvFullname = findViewById<TextView>(R.id.tvFullname)
        val tvRelation = findViewById<TextView>(R.id.tvRelation)
        val tvParentfirstname = findViewById<TextView>(R.id.tvParentfirstname)
        val tvParentmiddlename = findViewById<TextView>(R.id.tvParentmiddlename)
        val tvParentlastname = findViewById<TextView>(R.id.tvParentlastname)
        val tvParentfullname = findViewById<TextView>(R.id.tvParentfullname)

        CoroutineScope(Dispatchers.IO).launch {
            ProfileApi.getProfile().also {
                withContext(Dispatchers.Main) {
                    tvArea.text = it.address.area.uppercase()
                    tvCity.text = it.address.city.uppercase()
                    tvDistrict.text = it.address.district.uppercase()
                    tvDob.text = it.dob
                    tvEmail.text = it.email
                    tvFirstname.text = it.name.firstname.uppercase()
                    tvFullname.text = it.name.fullname.uppercase()
                    tvHouseNo.text = it.address.houseNo.uppercase()
                    tvLastname.text = it.name.lastname.uppercase()
                    tvLocality.text = it.address.locality.uppercase()
                    tvMiddlename.text = it.name.middlename.uppercase()
                    tvParentfirstname.text = it.parent.name.firstname.uppercase()
                    tvParentfullname.text = it.parent.name.fullname.uppercase()
                    tvParentlastname.text = it.parent.name.lastname.uppercase()
                    tvParentmiddlename.text = it.parent.name.middlename.uppercase()
                    tvPhone.text = it.contactNo
                    tvPincode.text = it.address.pincode
                    tvPostoffice.text = it.address.postOffice.uppercase()
                    tvRelation.text = it.parent.relation.relation
                    tvState.text = it.address.state.uppercase()
                    tvStreetLine1.text = it.address.street_line_1.uppercase()
                    tvSubDistrict.text = it.address.subDistrict.uppercase()
                    tvUsername.text = it.username
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            ProfileApi.updateProfile("raj-rangani", "rajrangani@gmail.com", "9929435622", "2002/10/07", "m")
        }


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