package com.example.viewpagertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpagertest.adapter.DataModel
import com.example.viewpagertest.adapter.ProfileGroupAdapter

class ProfileGroupFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_profile_group, container, false)

        val profile = layoutInflater.inflate(R.layout.user_profile_layout, null)

//        (profile.parent as ViewManager).removeView(profile)

        val adapter = ProfileGroupAdapter(listOf(
            DataModel("Zombie Facts", profile.findViewById(R.id.profileLayout)),
            DataModel("Mariana Trench",profile.findViewById(R.id.profileLayout)),
            DataModel("Broccoli Facts",profile.findViewById(R.id.profileLayout)),
            DataModel("Llama Facts", profile.findViewById(R.id.profileLayout)),
            DataModel("Turtle Facts", profile.findViewById(R.id.profileLayout))
        ))
        fragmentView.findViewById<com.example.viewpagertest.adapter.AccordianView>(R.id.accordian_view).adapter = adapter

        return fragmentView
    }

    companion object
    {
        val INSTANCE = ProfileGroupFragment()
    }
}