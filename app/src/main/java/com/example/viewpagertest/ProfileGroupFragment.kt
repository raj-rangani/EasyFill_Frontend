package com.example.viewpagertest

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.`AppCompatEditText$InspectionCompanion`
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpagertest.adapter.DataModel
import com.example.viewpagertest.adapter.ProfileGroupAdapter
import com.example.viewpagertest.database.MyDatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class ProfileGroupFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_profile_group, container, false)

        /**
        *   ALL PROFILES - PROFILES FOR ALL USERS
        */
        val fatherProfile = fragmentView.findViewById(R.id.fatherData) as MaterialButton
        val yourProfile = fragmentView.findViewById(R.id.profileData) as MaterialButton
        val motherProfile = fragmentView.findViewById(R.id.motherData) as MaterialButton

        val dbHelper = MyDatabaseHelper(fragmentView.context)

        val profile = dbHelper.getProfileData(1)[0]
        val getFatherProfile = dbHelper.getProfileData(3)[0]
        val getMotherProfile = dbHelper.getProfileData(3)[0]

        dbHelper.close()

        fatherProfile.setOnClickListener {
            val fatherProfileCard = fragmentView.findViewById<CardView>(R.id.parentCard)
            val mainParentCard = fragmentView.findViewById<CardView>(R.id.mainFatherCard)
            val view = inflater.inflate(R.layout.user_profile_layout, null)

            if(fatherProfileCard.tag.equals("close")) {
                fatherProfileCard.addView(view)
                scaleView(mainParentCard, fatherProfileCard.scaleX, fatherProfileCard.scaleY)
                fatherProfileCard.visibility = View.VISIBLE
                fatherProfileCard.tag = "open"
                fatherProfile.setIconResource(R.drawable.ic_chevron_up)

                view.findViewById<TextView>(R.id.tvFirstname).text = getFatherProfile.name.firstname
                view.findViewById<TextView>(R.id.tvLastname).text = getFatherProfile.name.lastname.uppercase()
                view.findViewById<TextView>(R.id.tvMiddlename).text = getFatherProfile.name.middlename.uppercase()
                view.findViewById<TextView>(R.id.tvFullname).text = getFatherProfile.name.fullname.uppercase()
                view.findViewById<TextView>(R.id.tvRelation).text = getFatherProfile.parent.relation.relation.uppercase()
                view.findViewById<TextView>(R.id.tvParentfullname).text = getFatherProfile.parent.name.fullname.uppercase()
                view.findViewById<TextView>(R.id.tvParentfirstname).text = getFatherProfile.parent.name.firstname.uppercase()
                view.findViewById<TextView>(R.id.tvParentlastname).text = getFatherProfile.parent.name.lastname.uppercase()
                view.findViewById<TextView>(R.id.tvParentmiddlename).text = getFatherProfile.parent.name.middlename.uppercase()

            } else {
                fatherProfileCard.removeView(view)
                fatherProfileCard.visibility = View.GONE
                fatherProfileCard.tag = "close"
                fatherProfile.setIconResource(R.drawable.ic_chevron_down)
            }

        }

        motherProfile.setOnClickListener {
            val motherProfileCard = fragmentView.findViewById<CardView>(R.id.motherCard)
            val mainMotherCard = fragmentView.findViewById<CardView>(R.id.mainMotherCard)
            val view = inflater.inflate(R.layout.user_profile_layout, null)

            if(motherProfileCard.tag.equals("close")) {
                motherProfileCard.addView(view)
                scaleView(mainMotherCard, motherProfileCard.scaleX, motherProfileCard.scaleY)
                motherProfileCard.visibility = View.VISIBLE
                motherProfileCard.tag = "open"
                motherProfile.setIconResource(R.drawable.ic_chevron_up)

                view.findViewById<TextView>(R.id.tvFirstname).text = getMotherProfile.name.firstname.uppercase()
                view.findViewById<TextView>(R.id.tvLastname).text = getMotherProfile.name.lastname.uppercase()
                view.findViewById<TextView>(R.id.tvMiddlename).text = getMotherProfile.name.middlename.uppercase()
                view.findViewById<TextView>(R.id.tvFullname).text = getMotherProfile.name.fullname.uppercase()
                view.findViewById<TextView>(R.id.tvRelation).text = getMotherProfile.parent.relation.relation.uppercase()
                view.findViewById<TextView>(R.id.tvParentfullname).text = getMotherProfile.parent.name.fullname.uppercase()
                view.findViewById<TextView>(R.id.tvParentfirstname).text = getMotherProfile.parent.name.firstname.uppercase()
                view.findViewById<TextView>(R.id.tvParentlastname).text = getMotherProfile.parent.name.lastname.uppercase()
                view.findViewById<TextView>(R.id.tvParentmiddlename).text = getMotherProfile.parent.name.middlename.uppercase()

            } else {
                motherProfileCard.removeView(view)
                motherProfileCard.visibility = View.GONE
                motherProfileCard.tag = "close"
                motherProfile.setIconResource(R.drawable.ic_chevron_down)
            }
        }

        yourProfile.setOnClickListener {
            val yourProfileCard = fragmentView.findViewById<CardView>(R.id.profileCard)
            val mainProfileCard = fragmentView.findViewById<CardView>(R.id.mainProfileCard)
            val view = inflater.inflate(R.layout.user_profile_layout, null)

            if(yourProfileCard.tag.equals("close")) {
                yourProfileCard.addView(view)
                scaleView(mainProfileCard, yourProfileCard.scaleX, yourProfileCard.scaleY)
                yourProfileCard.visibility = View.VISIBLE
                yourProfileCard.tag = "open"
                yourProfile.setIconResource(R.drawable.ic_chevron_up)

                /**
                 * Text Fields Data Set
                 */

                view.findViewById<TextView>(R.id.tvFirstname).text = profile.name.firstname.uppercase()
                view.findViewById<TextView>(R.id.tvLastname).text = profile.name.lastname.uppercase()
                view.findViewById<TextView>(R.id.tvMiddlename).text = profile.name.middlename.uppercase()
                view.findViewById<TextView>(R.id.tvFullname).text = profile.name.fullname.uppercase()
                view.findViewById<TextView>(R.id.tvRelation).text = profile.parent.relation.relation.uppercase()
                view.findViewById<TextView>(R.id.tvParentfullname).text = profile.parent.name.fullname.uppercase()
                view.findViewById<TextView>(R.id.tvParentfirstname).text = profile.parent.name.firstname.uppercase()
                view.findViewById<TextView>(R.id.tvParentlastname).text = profile.parent.name.lastname.uppercase()
                view.findViewById<TextView>(R.id.tvParentmiddlename).text = profile.parent.name.middlename.uppercase()

            } else {
                yourProfileCard.removeView(view)
                yourProfileCard.visibility = View.GONE
                yourProfileCard.tag = "close"
                yourProfile.setIconResource(R.drawable.ic_chevron_down)
            }

        }

        return fragmentView
    }

    companion object
    {
        val INSTANCE = ProfileGroupFragment()
    }

    private fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim = ScaleAnimation(
            1f, 1f, // Start and end values for the X axis scaling
            startScale, endScale, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0f) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 200
        v.startAnimation(anim)
    }
}