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

        /**
         *  FATHER PROFILE DATA
         */
        val fatherFirstname = fatherProfile.findViewById<TextView>(R.id.tvFirstname)
        val fatherLastName = fatherProfile.findViewById<TextView>(R.id.tvLastname)
        val fatherMiddlename = fatherProfile.findViewById<TextView>(R.id.tvMiddlename)
        val fatherFullname = fatherProfile.findViewById<TextView>(R.id.tvFullname)
        val fatherParentrelation = fatherProfile.findViewById<TextView>(R.id.tvRelation)
        val fatherParentFullname = fatherProfile.findViewById<TextView>(R.id.tvParentfullname)
        val fatherParentFirstname = fatherProfile.findViewById<TextView>(R.id.tvParentfirstname)
        val fatherParentLastname = fatherProfile.findViewById<TextView>(R.id.tvParentlastname)
        val fatherParentMiddlename = fatherProfile.findViewById<TextView>(R.id.tvParentmiddlename)

        /**
         *  MOTHER PROFILE DATA
         */
        val motherFirstname = motherProfile.findViewById<TextView>(R.id.tvFirstname)
        val motherLastName = motherProfile.findViewById<TextView>(R.id.tvLastname)
        val motherMiddlename = motherProfile.findViewById<TextView>(R.id.tvMiddlename)
        val motherFullname = motherProfile.findViewById<TextView>(R.id.tvFullname)
        val motherParentrelation = motherProfile.findViewById<TextView>(R.id.tvRelation)
        val motherParentFullname = motherProfile.findViewById<TextView>(R.id.tvParentfullname)
        val motherParentFirstname = motherProfile.findViewById<TextView>(R.id.tvParentfirstname)
        val motherParentLastname = motherProfile.findViewById<TextView>(R.id.tvParentlastname)
        val motherParentMiddlename = motherProfile.findViewById<TextView>(R.id.tvParentmiddlename)

        /**
         *  YOUR PROFILE DATA
         */
        val yourFirstname = yourProfile.findViewById<TextView>(R.id.tvFirstname)
        val yourLastName = yourProfile.findViewById<TextView>(R.id.tvLastname)
        val yourMiddlename = yourProfile.findViewById<TextView>(R.id.tvMiddlename)
        val yourFullname = yourProfile.findViewById<TextView>(R.id.tvFullname)
        val yourParentrelation = yourProfile.findViewById<TextView>(R.id.tvRelation)
        val yourParentFullname = yourProfile.findViewById<TextView>(R.id.tvParentfullname)
        val yourParentFirstname = yourProfile.findViewById<TextView>(R.id.tvParentfirstname)
        val yourParentLastname = yourProfile.findViewById<TextView>(R.id.tvParentlastname)
        val yourParentMiddlename = yourProfile.findViewById<TextView>(R.id.tvParentmiddlename)

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