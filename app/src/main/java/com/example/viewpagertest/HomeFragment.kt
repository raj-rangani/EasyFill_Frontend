package com.example.viewpagertest

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpagertest.adapter.HomeCardAdapter
import com.example.viewpagertest.database.MyDatabaseHelper

class HomeFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_home, container, false)
        val notFound = fragmentView.findViewById<ConstraintLayout>(R.id.notFound)
        val main = fragmentView.findViewById<LinearLayout>(R.id.mainLayout)
        val recyclerview = fragmentView.findViewById<RecyclerView>(R.id.homeRecycleview)
        val authorname = fragmentView.findViewById<TextView>(R.id.Authorname)
        val formname = fragmentView.findViewById<TextView>(R.id.Formname)
        val percent = fragmentView.findViewById<TextView>(R.id.fillPercent)

        val data = MyDatabaseHelper(fragmentView.context).getFormData(null)
        if(data.size > 0) {
            val prefs = activity?.getSharedPreferences("FORM", Context.MODE_PRIVATE)
            val form = prefs?.getString("Form", "-1")

            if(!(form.equals("-1"))) {
                val data = MyDatabaseHelper(fragmentView.context).getFormData(form?.toInt())[0]
                authorname.text = data.authorname?.uppercase()
                formname.text = data.formname?.uppercase()
                percent.text = data.fillpercent.toString() + " Percent"
            }

            notFound.visibility = View.GONE
            main.visibility = View.VISIBLE
            recyclerview.layoutManager = LinearLayoutManager(fragmentView.context)
            val adapter = HomeCardAdapter(data)
            recyclerview.adapter = adapter
        } else {
            notFound.visibility = View.VISIBLE
            main.visibility = View.GONE
        }

        return fragmentView
    }

    companion object
    {
        val INSTANCE = HomeFragment()
    }
}