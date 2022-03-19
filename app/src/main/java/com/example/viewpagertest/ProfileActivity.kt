package com.example.viewpagertest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomsheet.BottomSheetDialog

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color

import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.*

import com.example.viewpagertest.api.ProfileApi
import com.example.viewpagertest.models.Profile
import com.example.viewpagertest.models.Relation
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val user = findViewById<MaterialCardView>(R.id.userCard)
        val back = findViewById<Button>(R.id.back)
        val profileImage = findViewById<ImageView>(R.id.profileImage)

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

        val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
        val profileJson = prefsProfile.getString("Profile", "[NO DATA]")
        if(!(profileJson.equals("[NO DATA]"))) {
            if(!(profileJson.equals(null))) {
                val profile = Gson().fromJson(profileJson, Profile::class.java)
                val image = openFileInput("Avatar.png")
                val bitmap = BitmapFactory.decodeStream(image)

                if(bitmap != null) {
                    profileImage.setImageBitmap(bitmap)
                } else {
                    profileImage.setImageResource(R.drawable.default_profile)
                }

                tvArea.text = profile.address.area.uppercase()
                tvCity.text = profile.address.city.uppercase()
                tvDistrict.text = profile.address.district.uppercase()
                tvDob.text = profile.dob
                tvEmail.text = profile.email
                tvFirstname.text = profile.name.firstname.uppercase()
                tvFullname.text = profile.name.fullname.uppercase()
                tvHouseNo.text = profile.address.houseNo.uppercase()
                tvLastname.text = profile.name.lastname.uppercase()
                tvLocality.text = profile.address.locality.uppercase()
                tvMiddlename.text = profile.name.middlename.uppercase()
                tvParentfirstname.text = profile.parent.name.firstname.uppercase()
                tvParentfullname.text = profile.parent.name.fullname.uppercase()
                tvParentlastname.text = profile.parent.name.lastname.uppercase()
                tvParentmiddlename.text = profile.parent.name.middlename.uppercase()
                tvPhone.text = profile.contactNo
                tvPincode.text = profile.address.pincode.uppercase()
                tvPostoffice.text = profile.address.postOffice.uppercase()
                tvRelation.text = profile.parent.relation.relation
                tvState.text = profile.address.state.uppercase()
                tvStreetLine1.text = profile.address.street_line_1.uppercase()
                tvSubDistrict.text = profile.address.subDistrict.uppercase()
                tvUsername.text = profile.username

            } else {
                tvUsername.text = "Anonymous"
                profileImage.setImageResource(R.drawable.default_profile)
            }

        }

        back.setOnClickListener {
            finish()
        }

        user.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_user_update_layout)
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)

            val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
            val token = authPrefs.getString("TOKEN", "[No Data]")!!
            val username = dialog.findViewById<EditText>(R.id.etUsername)
            val email = dialog.findViewById<EditText>(R.id.etEmail)
            val phone = dialog.findViewById<EditText>(R.id.etContact)

            val userUpdate = dialog.findViewById<Button>(R.id.userUpdate)
            dialog.findViewById<Button>(R.id.close).setOnClickListener { dialog.hide() }
            userUpdate.setOnClickListener {
                dialog.hide()
            CoroutineScope(Dispatchers.IO).launch {
                val data = ProfileApi.updateProfile(username.text.toString(), email.text.toString(), phone.text.toString(), "2002/10/07", "m", token)
                withContext(Dispatchers.Main) {
                    if(data[0].toString().toBoolean()) {
                        val json: JSONObject = data[1] as JSONObject
                        val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                        val profileJson = prefsProfile.getString("Profile", "[NO DATA]")

                        if(!(profileJson.equals("[NO DATA]"))) {
                            if (!(profileJson.equals(null))) {
                                val profile = Gson().fromJson(profileJson, Profile::class.java)
                                profile.username = json.getString("username")
                                profile.contactNo = json.getString("contact_no")
                                profile.email = json.getString("email")
                                profile.gender = json.getString("gender")
                                profile.dob = json.getString("dob")

                                val profileEdit = prefsProfile.edit()
                                profileEdit.putString("Profile", Gson().toJson(profile))
                                profileEdit.apply()
                            }
                            Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                            dialog.hide()
                        }
                    } else {
                        Toast.makeText(this@ProfileActivity, data[1].toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
        }
    }
}