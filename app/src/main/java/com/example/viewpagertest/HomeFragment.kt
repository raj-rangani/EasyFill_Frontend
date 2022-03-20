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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.viewpagertest.adapter.HomeCardAdapter
import com.example.viewpagertest.database.MyDatabaseHelper
import com.google.android.material.progressindicator.CircularProgressIndicator

class HomeFragment : Fragment()
{
    private lateinit var fragmentView: View
    private lateinit var notFound: ConstraintLayout
    private lateinit var main: LinearLayout
    private lateinit var recyclerview: RecyclerView
    private lateinit var authorname: TextView
    private lateinit var formname: TextView
    private lateinit var percent: TextView
    private lateinit var progress: CircularProgressIndicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false)
        notFound = fragmentView.findViewById<ConstraintLayout>(R.id.notFound)
        main = fragmentView.findViewById<LinearLayout>(R.id.mainLayout)
        recyclerview = fragmentView.findViewById<RecyclerView>(R.id.homeRecycleview)
        authorname = fragmentView.findViewById<TextView>(R.id.Authorname)
        formname = fragmentView.findViewById<TextView>(R.id.Formname)
        percent = fragmentView.findViewById<TextView>(R.id.fillPercent)
        progress = fragmentView.findViewById(R.id.progress)

        return fragmentView
    }

    override fun onDetach() {
        fragmentView.invalidate()
        super.onDetach()
    }

    override fun onResume() {
        val data = MyDatabaseHelper(fragmentView.context).getFormData(null)
        if(data.size > 0) {
            val prefs = activity?.getSharedPreferences("FORM", Context.MODE_PRIVATE)
            val form = prefs?.getString("Form", "-1")

            if(!(form.equals("-1"))) {
                val data = MyDatabaseHelper(fragmentView.context).getFormData(form?.toInt())[0]
                authorname.text = data.authorname?.uppercase()
                formname.text = data.formname?.uppercase()
                percent.text = data.fillpercent.toString() + " Percent"
                progress.progress = data.fillpercent!!
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

        super.onResume()
    }

    companion object
    {
        val INSTANCE = HomeFragment()
    }
}