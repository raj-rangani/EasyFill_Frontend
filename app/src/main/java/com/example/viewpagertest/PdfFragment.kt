package com.example.viewpagertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PdfFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_pdf, container, false)

        // ...

        return fragmentView
    }

    companion object
    {
        val INSTANCE = PdfFragment()
    }
}