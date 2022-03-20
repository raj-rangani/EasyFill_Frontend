package com.example.viewpagertest

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.viewpagertest.models.Profile
import com.example.viewpagertest.models.Relation
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class SelectFormDetailsFragment: Fragment() {

    private lateinit var fragmentView:View
    private lateinit var lInflater: LayoutInflater
    private lateinit var fullname: TextView
    private lateinit var dob: TextView
    private lateinit var age: TextView
    private lateinit var contact: TextView
    private lateinit var email: TextView
    private lateinit var gender: RadioGroup
    private lateinit var street: TextView
    private lateinit var area: TextView
    private lateinit var state: TextView
    private lateinit var locality: TextView
    private lateinit var city: TextView
    private lateinit var house: TextView
    private lateinit var post: TextView
    private lateinit var district: TextView
    private lateinit var subDistrict: TextView
    private lateinit var pincode: TextView
    private lateinit var selectFormPopupButton: Button
    private lateinit var parentName: TextView
    private lateinit var adharNo: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView = inflater.inflate(R.layout.fragment_select_form_details, container, false)
        lInflater = inflater

        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        fullname = fragmentView.findViewById<TextView>(R.id.etFullname)
        dob = fragmentView.findViewById<TextView>(R.id.etdob)
        age = fragmentView.findViewById<TextView>(R.id.etAge)
        contact = fragmentView.findViewById<TextView>(R.id.etContact)
        email = fragmentView.findViewById<TextView>(R.id.etEmail)
        gender = fragmentView.findViewById<RadioGroup>(R.id.rbGender)
        street =fragmentView.findViewById<TextView>(R.id.etStreet)
        area =fragmentView.findViewById<TextView>(R.id.etArea)
        state = fragmentView.findViewById<TextView>(R.id.etState)
        locality =fragmentView.findViewById<TextView>(R.id.etLocality)
        city =fragmentView.findViewById<TextView>(R.id.etCity)
        house =fragmentView.findViewById<TextView>(R.id.etHouse)
        post =fragmentView.findViewById<TextView>(R.id.etPostoffice)
        district =fragmentView.findViewById<TextView>(R.id.etDistrict)
        subDistrict =fragmentView.findViewById<TextView>(R.id.etSubdistrict)
        pincode =fragmentView.findViewById<TextView>(R.id.etPincode)
        selectFormPopupButton =fragmentView.findViewById<Button>(R.id.selectRelation)
        parentName =fragmentView.findViewById<TextView>(R.id.etParentFullname)
        adharNo =fragmentView.findViewById<TextView>(R.id.etAdharcardno)

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
                selectFormPopupButton.text = profile.parent.relation.relation
                parentName.text = profile.parent.name.fullname.uppercase()
                state.text = profile.address.state.uppercase()

                val listPopupWindow = ListPopupWindow(fragmentView.context, null, R.attr.listPopupWindowStyle)

                selectFormPopupButton.also { listPopupWindow.anchorView = it }

                val items = arrayOf(Relation.W.relation, Relation.F.relation, Relation.G.relation, Relation.H.relation, Relation.M.relation)
                val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
                listPopupWindow.setAdapter(adapter)

                listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, view: View?, _: Int, _: Long ->
                    selectFormPopupButton.text = (view as TextView).text
                    listPopupWindow.dismiss()
                }

                selectFormPopupButton.setOnClickListener { v: View? -> listPopupWindow.show() }

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        val formatedDate = formatter.format(date)

        val data = "{\"FULLNAME\"=\"${fullname.text.toString()}\", " +
                "\"DOB\"=\"${dob.text.toString()}\", " +
                "\"AGE\"=\"${age.text.toString()}\"," +
                "\"CONTACT\"=\"${contact.text.toString()}\"," +
                "\"EMAIL\"=\"${email.text.toString()}\"," +
                "\"GENDER\"=\"${gender.checkedRadioButtonId}\"," +
                "\"STREETLINE\"=\"${street.text.toString()}\"," +
                "\"AREA\"=\"${area.text.toString()}\"," +
                "\"LANDMARK\"=\"${locality.text.toString()}\"," +
                "\"CITY\"=\"${city.text.toString()}\"," +
                "\"HOUSENO\"=\"${house.text.toString()}\"," +
                "\"POSTOFFICE\"=\"${post.text.toString()}\"," +
                "\"DISTRICT\"=\"${district.text.toString()}\"," +
                "\"SUBDISTRICT\"=\"${subDistrict.text.toString()}\"," +
                "\"PINCODE\"=\"${pincode.text.toString()}\"," +
                "\"relation\"=\"${selectFormPopupButton.text.toString()}\"," +
                "\"parentName\"=\"${parentName.text.toString()}\"," +
                "\"CURRENTDATE\"=\"${formatedDate}\"," +
                "\"STATE\"=\"${state.text.toString()}\"}"

        val dataPrefs = activity?.getSharedPreferences("DATA", MODE_PRIVATE)
        val dataPrefsEdit = dataPrefs?.edit()
        dataPrefsEdit?.putString("data", data)
        dataPrefsEdit?.apply()
    }

    companion object
    {
        val INSTANCE = SelectFormDetailsFragment()
    }
}