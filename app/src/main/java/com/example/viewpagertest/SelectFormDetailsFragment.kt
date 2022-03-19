package com.example.viewpagertest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SelectFormDetailsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_select_form_details, container, false)

        // ...

        return fragmentView
    }

    companion object
    {
        val INSTANCE = SelectFormDetailsFragment()
    }
}