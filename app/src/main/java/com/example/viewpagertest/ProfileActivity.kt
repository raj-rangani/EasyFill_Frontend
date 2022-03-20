package com.example.viewpagertest

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.card.MaterialCardView

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color

import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.*

import com.example.viewpagertest.api.ProfileApi
import com.example.viewpagertest.helper.Helper
import com.example.viewpagertest.models.Profile
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

import android.app.DatePickerDialog
import androidx.appcompat.widget.ListPopupWindow
import com.example.viewpagertest.api.LoginApi
import com.example.viewpagertest.models.Relation
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.widget.Toast

import android.graphics.Bitmap
import android.net.Uri
import com.example.viewpagertest.helper.Constant
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class ProfileActivity : AppCompatActivity() {
    private val RESULT_LOAD_IMG = 1
    private lateinit var profileImage:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById<ImageView>(R.id.profileImage)
        val user = findViewById<MaterialCardView>(R.id.userCard)
        val name = findViewById<MaterialCardView>(R.id.nameCard)
        val parent = findViewById<MaterialCardView>(R.id.parentCard)
        val address = findViewById<MaterialCardView>(R.id.addressCard)
        val logout = findViewById<Button>(R.id.logout)
        val delete = findViewById<MaterialCardView>(R.id.delete)
        val back = findViewById<Button>(R.id.back)

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

        profileImage.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        }

        logout.setOnClickListener {
            val progess = Helper.showProgress(this, "Logging Out ...")
            val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
            val token = authPrefs.getString("TOKEN", "[No Data]")!!

            CoroutineScope(Dispatchers.IO).launch {
                val result = LoginApi.logout(token)
                withContext(Dispatchers.Main) {
                    if (result) {
                        val authEdit = authPrefs.edit()
                        authEdit.remove("TOKEN")
                        authEdit.remove("LOGIN")
                        authEdit.clear()
                        authEdit.apply()

                        val sharedProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                        val profileEdit = sharedProfile.edit()
                        profileEdit.remove("Profiles")
                        profileEdit.clear()
                        profileEdit.apply()

                        finish()
                        progess.hide()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Logging Out Failed", Toast.LENGTH_SHORT).show()
                        progess.hide()
                    }
                }
            }
        }

        delete.setOnClickListener {
            val progress = Helper.showProgress(this, "Deleting Account")
            val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
            val token = authPrefs.getString("TOKEN", "[No Data]")!!

            CoroutineScope(Dispatchers.IO).launch {
                val result = ProfileApi.deleteProfile(token)
                withContext(Dispatchers.Main) {
                    if (result) {
                        val authEdit = authPrefs.edit()
                        authEdit.clear()
                        authEdit.apply()

                        val sharedProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                        val profileEdit = sharedProfile.edit()
                        profileEdit.clear()
                        profileEdit.apply()

                        finish()
                        progress.hide()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Delete Failed", Toast.LENGTH_SHORT).show()
                        progress.hide()
                    }
                }
            }
        }

        back.setOnClickListener {
            finish()
        }

        /**
         * User Profile Update Code
         *      The code to Update user Profile and to load it in the text view after updated
         */

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
            val dobSelect = dialog.findViewById<MaterialCardView>(R.id.dobSelect)

            // Select Date from datepicker dialog

            var dobText = ""
            val dpd = DatePickerDialog(dialog.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dobText = "${year}/${if(monthOfYear+1 > 9) "${monthOfYear+1}" else "0${monthOfYear+1}"}/${if(dayOfMonth > 9) "${dayOfMonth}" else "0${dayOfMonth}"}"
            }, 2021, 2, 1)

            dobSelect.setOnClickListener { dpd.show() }

            username.text = SpannableStringBuilder(tvUsername.text.toString())
            email.text = SpannableStringBuilder(tvEmail.text.toString())
            phone.text = SpannableStringBuilder(tvPhone.text.toString())

            val userUpdate = dialog.findViewById<Button>(R.id.userUpdate)
            dialog.findViewById<Button>(R.id.close).setOnClickListener { dialog.hide() }
            userUpdate.setOnClickListener {
                dialog.hide()
                val progess = Helper.showProgress(this, "Updating User Details")

                CoroutineScope(Dispatchers.IO).launch {
                    val data = ProfileApi.updateProfile(username.text.toString(), email.text.toString(), phone.text.toString(), dobText, "m", token)
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

                                    tvUsername.text = profile.username
                                    tvPhone.text = profile.contactNo
                                    tvEmail.text = profile.email
                                    tvDob.text = profile.dob
                                }
                                Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                                progess.hide()
                            }
                        } else {
                            Toast.makeText(this@ProfileActivity, data[1].toString(), Toast.LENGTH_SHORT).show()
                            progess.hide()
                        }
                    }
                }
            }
        }

        /**
         * User Name Update Code
         *      The code to Update user names and to load it in the text view after updated
         */
        name.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_name_update_layout)
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)

            val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
            val token = authPrefs.getString("TOKEN", "[No Data]")!!
            val firstName = dialog.findViewById<EditText>(R.id.etFirstname)
            val lastName = dialog.findViewById<EditText>(R.id.etLastname)
            val middleName = dialog.findViewById<EditText>(R.id.etMiddlename)
            val fullName = dialog.findViewById<EditText>(R.id.etFullname)

            firstName.text = SpannableStringBuilder(tvFirstname.text.toString())
            lastName.text = SpannableStringBuilder(tvLastname.text.toString())
            middleName.text = SpannableStringBuilder(tvMiddlename.text.toString())
            fullName.text = SpannableStringBuilder(tvFullname.text.toString())

            val userUpdate = dialog.findViewById<Button>(R.id.nameUpdate)
            dialog.findViewById<Button>(R.id.close).setOnClickListener { dialog.hide() }
            userUpdate.setOnClickListener {
                dialog.hide()
                val progress = Helper.showProgress(this, "Updating User Details")

                CoroutineScope(Dispatchers.IO).launch {
                    val data = ProfileApi.updateName(firstName.text.toString(), lastName.text.toString(), middleName.text.toString(), fullName.text.toString(), token)
                    withContext(Dispatchers.Main) {
                        if(data[0].toString().toBoolean()) {
                            val json: JSONObject = data[1] as JSONObject
                            val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                            val profileJson = prefsProfile.getString("Profile", "[NO DATA]")

                            if(!(profileJson.equals("[NO DATA]"))) {
                                if (!(profileJson.equals(null))) {
                                    val profile = Gson().fromJson(profileJson, Profile::class.java)
                                    profile.name.firstname = json.getString("firstname")
                                    profile.name.lastname = json.getString("lastname")
                                    profile.name.middlename = json.getString("middlename")
                                    profile.name.fullname = json.getString("fullname")

                                    val profileEdit = prefsProfile.edit()
                                    profileEdit.putString("Profile", Gson().toJson(profile))
                                    profileEdit.apply()

                                    tvFirstname.text = profile.name.firstname.uppercase()
                                    tvFullname.text = profile.name.fullname.uppercase()
                                    tvMiddlename.text = profile.name.middlename.uppercase()
                                    tvLastname.text = profile.name.lastname.uppercase()
                                }
                                Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                                progress.hide()
                            }
                        } else {
                            Toast.makeText(this@ProfileActivity, data[1].toString(), Toast.LENGTH_SHORT).show()
                            progress.hide()
                        }
                    }
                }
            }
        }

        /**
         * User Parent Update Code
         *      The code to Update user parent and to load it in the text view after updated
         */
        parent.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_parent_update_layout)
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)

            val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
            val token = authPrefs.getString("TOKEN", "[No Data]")!!
            val firstName = dialog.findViewById<EditText>(R.id.etParentFirstname)
            val lastName = dialog.findViewById<EditText>(R.id.etParentLastname)
            val middleName = dialog.findViewById<EditText>(R.id.etParentMiddlename)
            val fullName = dialog.findViewById<EditText>(R.id.etParentFullname)
            val selectRelationPopupButton = dialog.findViewById<Button>(R.id.selectRelation)

            val listProfilePopupWindow = ListPopupWindow(this, null, R.attr.listPopupWindowStyle)
            selectRelationPopupButton.setOnClickListener { v: View? -> listProfilePopupWindow.show() }
            selectRelationPopupButton.also { listProfilePopupWindow.anchorView = it }

            val profileItems = arrayOf(Relation.W.relation, Relation.F.relation, Relation.G.relation, Relation.H.relation, Relation.M.relation)
            val profileAdapter = ArrayAdapter(this, R.layout.list_popup_window_item, profileItems)
            listProfilePopupWindow.setAdapter(profileAdapter)

            listProfilePopupWindow.setOnItemClickListener { _: AdapterView<*>?, view: View?, _: Int, _: Long ->
                selectRelationPopupButton.text = (view as TextView).text
                listProfilePopupWindow.dismiss()
            }

            selectRelationPopupButton.text = tvRelation.text.toString()
            firstName.text = SpannableStringBuilder(tvParentfirstname.text.toString())
            lastName.text = SpannableStringBuilder(tvParentlastname.text.toString())
            middleName.text = SpannableStringBuilder(tvParentmiddlename.text.toString())
            fullName.text = SpannableStringBuilder(tvParentfullname.text.toString())

            val userUpdate = dialog.findViewById<Button>(R.id.parentUpdate)
            dialog.findViewById<Button>(R.id.close).setOnClickListener { dialog.hide() }
            userUpdate.setOnClickListener {
                dialog.hide()
                val progress = Helper.showProgress(this, "Updating User Details")

                CoroutineScope(Dispatchers.IO).launch {
                    val data = ProfileApi.updateParent(selectRelationPopupButton.text.toString(), firstName.text.toString(), lastName.text.toString(), middleName.text.toString(), fullName.text.toString(), token)
                    withContext(Dispatchers.Main) {
                        if(data[0].toString().toBoolean()) {
                            val json: JSONObject = data[1] as JSONObject
                            val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                            val profileJson = prefsProfile.getString("Profile", "[NO DATA]")

                            if(!(profileJson.equals("[NO DATA]"))) {
                                if (!(profileJson.equals(null))) {
                                    val profile = Gson().fromJson(profileJson, Profile::class.java)
                                    profile.parent.relation = Relation.valueOf(json.getString("relation"))
                                    val name = json.getJSONObject("name")
                                    profile.parent.name.firstname = name.getString("firstname")
                                    profile.parent.name.lastname = name.getString("lastname")
                                    profile.parent.name.middlename = name.getString("middlename")
                                    profile.parent.name.fullname = name.getString("fullname")

                                    val profileEdit = prefsProfile.edit()
                                    profileEdit.putString("Profile", Gson().toJson(profile))
                                    profileEdit.apply()

                                    tvRelation.text = profile.parent.relation.relation
                                    tvFirstname.text = profile.parent.name.firstname.uppercase()
                                    tvFullname.text = profile.parent.name.fullname.uppercase()
                                    tvMiddlename.text = profile.parent.name.middlename.uppercase()
                                    tvLastname.text = profile.parent.name.lastname.uppercase()
                                }
                                Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                                progress.hide()
                            }
                        } else {
                            Toast.makeText(this@ProfileActivity, data[1].toString(), Toast.LENGTH_SHORT).show()
                            progress.hide()
                        }
                    }
                }
            }
        }

        /**
         * User Address Update Code
         *      The code to Update user address and to load it in the text view after updated
         */
        address.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet_address_update_layout)
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)

            val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
            val token = authPrefs.getString("TOKEN", "[No Data]")!!
            val street = dialog.findViewById<EditText>(R.id.etStreet)
            val area = dialog.findViewById<EditText>(R.id.etArea)
            val locality = dialog.findViewById<EditText>(R.id.etLocality)
            val house = dialog.findViewById<EditText>(R.id.etHouse)
            val postOffice = dialog.findViewById<EditText>(R.id.etPostoffice)
            val state = dialog.findViewById<EditText>(R.id.etState)
            val district = dialog.findViewById<EditText>(R.id.etDistrict)
            val subDistrict = dialog.findViewById<EditText>(R.id.etSubdistrict)
            val city = dialog.findViewById<EditText>(R.id.etCity)
            val pincode = dialog.findViewById<EditText>(R.id.etPincode)

            street.text = SpannableStringBuilder(tvStreetLine1.text.toString())
            area.text = SpannableStringBuilder(tvArea.text.toString())
            locality.text = SpannableStringBuilder(tvLocality.text.toString())
            house.text = SpannableStringBuilder(tvHouseNo.text.toString())
            postOffice.text = SpannableStringBuilder(tvPostoffice.text.toString())
            state.text = SpannableStringBuilder(tvState.text.toString())
            district.text = SpannableStringBuilder(tvDistrict.text.toString())
            subDistrict.text = SpannableStringBuilder(tvSubDistrict.text.toString())
            city.text = SpannableStringBuilder(tvCity.text.toString())
            pincode.text = SpannableStringBuilder(tvPincode.text.toString())

            val userUpdate = dialog.findViewById<Button>(R.id.nameUpdate)
            dialog.findViewById<Button>(R.id.close).setOnClickListener { dialog.hide() }
            userUpdate.setOnClickListener {
                dialog.hide()
                val progress = Helper.showProgress(this, "Updating User Details")

                CoroutineScope(Dispatchers.IO).launch {
                    val data = ProfileApi.updateAddress(
                        street.text.toString(),
                        area.text.toString(),
                        locality.text.toString(),
                        house.text.toString(),
                        postOffice.text.toString(),
                        state.text.toString(),
                        district.text.toString(),
                        subDistrict.text.toString(),
                        city.text.toString(),
                        pincode.text.toString(),
                        token
                    )

                    withContext(Dispatchers.Main) {
                        if(data[0].toString().toBoolean()) {
                            val json: JSONObject = data[1] as JSONObject
                            val prefsProfile = getSharedPreferences("PROFILES", MODE_PRIVATE)
                            val profileJson = prefsProfile.getString("Profile", "[NO DATA]")

                            if(!(profileJson.equals("[NO DATA]"))) {
                                if (!(profileJson.equals(null))) {
                                    val profile = Gson().fromJson(profileJson, Profile::class.java)
                                    profile.address.street_line_1 = json.getString("street_line_1")
                                    profile.address.pincode = json.getString("pincode")
                                    profile.address.city = json.getString("city")
                                    profile.address.subDistrict = json.getString("sub_district")
                                    profile.address.district = json.getString("district")
                                    profile.address.state = json.getString("state")
                                    profile.address.postOffice = json.getString("post_office")
                                    profile.address.houseNo = json.getString("house_no")
                                    profile.address.locality = json.getString("locality")
                                    profile.address.area = json.getString("area")

                                    val profileEdit = prefsProfile.edit()
                                    profileEdit.putString("Profile", Gson().toJson(profile))
                                    profileEdit.apply()

                                    tvStreetLine1.text = profile.address.street_line_1.uppercase()
                                    tvPincode.text = profile.address.pincode.uppercase()
                                    tvCity.text = profile.address.city.uppercase()
                                    tvSubDistrict.text = profile.address.subDistrict.uppercase()
                                    tvDistrict.text = profile.address.district.uppercase()
                                    tvState.text = profile.address.state.uppercase()
                                    tvPostoffice.text = profile.address.postOffice.uppercase()
                                    tvHouseNo.text = profile.address.houseNo.uppercase()
                                    tvLocality.text = profile.address.locality.uppercase()
                                    tvArea.text = profile.address.area.uppercase()
                                }
                                Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                                progress.hide()
                            }
                        } else {
                            Toast.makeText(this@ProfileActivity, data[1].toString(), Toast.LENGTH_SHORT).show()
                            progress.hide()
                        }
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val progressBar =  Helper.showProgress(this, "Uploading Image")
            try {
                val imageUri: Uri? = data?.data
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)

                val file = openFileOutput("Avatar.png", MODE_PRIVATE)
                selectedImage?.compress(Bitmap.CompressFormat.PNG,100, file)
                val bytes = ByteArrayOutputStream()
                selectedImage?.compress(Bitmap.CompressFormat.PNG,100, bytes)

                val authPrefs = getSharedPreferences("AUTH", MODE_PRIVATE)
                val token = authPrefs.getString("TOKEN", "[No Data]")!!

                CoroutineScope(Dispatchers.IO).launch {
                    val result = ProfileApi.setProfileImage(bytes, token)
                    withContext(Dispatchers.Main) {
                        if (result) {
                            val stream = openFileInput("Avatar.png")
                            val image = BitmapFactory.decodeStream(stream)
                            profileImage.setImageBitmap(image)
                            progressBar.hide()
                        }
                    }
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this@ProfileActivity, "Something went wrong", Toast.LENGTH_LONG).show()
                progressBar.hide()
            }
        } else {
            Toast.makeText(this@ProfileActivity, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }
}