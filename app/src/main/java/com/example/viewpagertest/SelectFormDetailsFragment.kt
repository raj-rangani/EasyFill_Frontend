package com.example.viewpagertest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.viewpagertest.models.Profile
import com.google.gson.Gson
import java.util.*
import kotlin.math.log

class SelectFormDetailsFragment: Fragment() {

    private lateinit var fragmentView:View
    private lateinit var lInflater: LayoutInflater
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView = inflater.inflate(R.layout.fragment_select_form_details, container, false)
        lInflater = inflater

        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        val verify = fragmentView.findViewById<RadioGroup>(R.id.verification)
        val verifyDetails = fragmentView.findViewById<LinearLayout>(R.id.verificationDetail)
        verify.setOnCheckedChangeListener { group, checkedId ->
            when(verify.checkedRadioButtonId) {
                R.id.document -> {
                    val view = lInflater.inflate(R.layout.document_view, null)
                    verifyDetails.removeAllViewsInLayout()
                    verifyDetails.visibility = View.VISIBLE
                    verifyDetails.addView(view)
                }
                R.id.introducer -> {
                    val view = lInflater.inflate(R.layout.introducer_view, null)
                    verifyDetails.removeAllViewsInLayout()
                    verifyDetails.visibility = View.VISIBLE
                    verifyDetails.addView(view)
                }
                R.id.hof -> {
                    val view = lInflater.inflate(R.layout.hof_view, null)
                    verifyDetails.removeAllViewsInLayout()
                    verifyDetails.visibility = View.VISIBLE
                    verifyDetails.addView(view)
                }
            }
        }

        val fullname = fragmentView.findViewById<TextView>(R.id.etFullname)
        val dob = fragmentView.findViewById<TextView>(R.id.etdob)
        val age = fragmentView.findViewById<TextView>(R.id.etAge)
        val contact = fragmentView.findViewById<TextView>(R.id.etContact)
        val email = fragmentView.findViewById<TextView>(R.id.etEmail)
        val gender = fragmentView.findViewById<RadioGroup>(R.id.rbGender)
        val street =fragmentView.findViewById<TextView>(R.id.etStreet)
        val area =fragmentView.findViewById<TextView>(R.id.etArea)
        val state = fragmentView.findViewById<TextView>(R.id.etState)
        val locality =fragmentView.findViewById<TextView>(R.id.etLocality)
        val city =fragmentView.findViewById<TextView>(R.id.etCity)
        val house =fragmentView.findViewById<TextView>(R.id.etHouse)
        val post =fragmentView.findViewById<TextView>(R.id.etPostoffice)
        val district =fragmentView.findViewById<TextView>(R.id.etDistrict)
        val subDistrict =fragmentView.findViewById<TextView>(R.id.etSubdistrict)
        val pincode =fragmentView.findViewById<TextView>(R.id.etPincode)
        val relation =fragmentView.findViewById<Button>(R.id.selectRelation)
        val parentName =fragmentView.findViewById<TextView>(R.id.etParentFullname)
        val adharNo =fragmentView.findViewById<TextView>(R.id.etAdharcardno)

        val prefsProfile = activity?.getSharedPreferences("PROFILES", AppCompatActivity.MODE_PRIVATE)
        val profileJson = prefsProfile?.getString("Profile", "[NO DATA]")
        if(!(profileJson.equals("[NO DATA]"))) {
            if (!(profileJson.equals(null))) {
                val profile = Gson().fromJson(profileJson, Profile::class.java)
                val today = Calendar.getInstance()
                val calculateDob = Calendar.getInstance()

                if(profile.dob.equals(null)) {
                    profile.dob = "[NO DATA]"
                } else {
                    calculateDob.set(profile.dob.split("/")[0].toInt(),profile.dob.split("/")[1].toInt(),profile.dob.split("/")[2].toInt())
                }

                fullname.text = profile.name.fullname
                dob.text = profile.dob
                age.text = if (profile.dob == "[NO DATA]") "[NO DATA]" else (today.get(Calendar.YEAR) - calculateDob.get(Calendar.YEAR) - 1).toString()
                contact.text = profile.contactNo
                email.text = profile.email
                gender.check(R.id.female)
                street.text = profile.address.street_line_1.uppercase()
                area.text = profile.address.area.uppercase()
                locality.text = profile.address.locality.uppercase()
                city.text = profile.address.city.uppercase()
                house.text = profile.address.houseNo.uppercase()
                post.text = profile.address.postOffice.uppercase()
                district.text = profile.address.district.uppercase()
                subDistrict.text = profile.address.subDistrict.uppercase()
                pincode.text = profile.address.pincode.uppercase()
                relation.text = profile.parent.relation.relation
                parentName.text = profile.parent.name.fullname.uppercase()
                state.text = profile.address.state.uppercase()

                val data = "{\"fullname\"=\"${fullname.text.toString()}\", " +
                        "\"dob\"=\"${dob.text.toString()}\", " +
                        "\"age\"=\"${age.text.toString()}\"," +
                        "\"contact\"=\"${contact.text.toString()}\"," +
                        "\"email\"=\"${email.text.toString()}\"," +
                        "\"gender\"=\"${gender.checkedRadioButtonId}\"," +
                        "\"street\"=\"${street.text.toString()}\"," +
                        "\"area\"=\"${area.text.toString()}\"," +
                        "\"locality\"=\"${locality.text.toString()}\"," +
                        "\"city\"=\"${city.text.toString()}\"," +
                        "\"house\"=\"${house.text.toString()}\"," +
                        "\"post\"=\"${post.text.toString()}\"," +
                        "\"district\"=\"${district.text.toString()}\"," +
                        "\"subDistrict\"=\"${subDistrict.text.toString()}\"," +
                        "\"pincode\"=\"${pincode.text.toString()}\"," +
                        "\"relation\"=\"${relation.text.toString()}\"," +
                        "\"parentName\"=\"${parentName.text.toString()}\"," +
                        "\"state\"=\"${state.text.toString()}\""

                Log.e("@@@@@@@@@", data)
            }
        }
    }

    companion object
    {
        val INSTANCE = SelectFormDetailsFragment()
    }
}