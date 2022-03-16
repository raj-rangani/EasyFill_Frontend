package com.example.viewpagertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow

class AddformFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_addform, container, false)

        val selectFormPopupButton = fragmentView.findViewById<Button>(R.id.selectForm)
        val listPopupWindow = ListPopupWindow(fragmentView.context, null, R.attr.listPopupWindowStyle)

        selectFormPopupButton.also { listPopupWindow.anchorView = it }

        val items = listOf("Aadhar Card Form", "Pan Card Form", "Voting Card Form")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, items)
        listPopupWindow.setAdapter(adapter)

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, view: View?, _: Int, _: Long ->
            selectFormPopupButton.text = (view as TextView).text
            listPopupWindow.dismiss()
        }

        selectFormPopupButton.setOnClickListener { v: View? -> listPopupWindow.show() }

        val selectProfilePopupButton = fragmentView.findViewById<Button>(R.id.selectProfile)
        val listProfilePopupWindow = ListPopupWindow(fragmentView.context, null, R.attr.listPopupWindowStyle)

        selectProfilePopupButton.also { listProfilePopupWindow.anchorView = it }

        val profileItems = listOf("For Yourself", "For Father", "For Mother")
        val profileAdapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, profileItems)
        listProfilePopupWindow.setAdapter(profileAdapter)

        listProfilePopupWindow.setOnItemClickListener { _: AdapterView<*>?, view: View?, _: Int, _: Long ->
            selectProfilePopupButton.text = (view as TextView).text
            listProfilePopupWindow.dismiss()
        }

        selectProfilePopupButton.setOnClickListener { v: View? -> listProfilePopupWindow.show() }

        return fragmentView
    }

    companion object
    {
        val INSTANCE = AddformFragment()
    }
}