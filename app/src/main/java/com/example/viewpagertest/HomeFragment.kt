package com.example.viewpagertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpagertest.adapter.HomeCardAdapter
import com.example.viewpagertest.database.MyDatabaseHelper

class HomeFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerview = fragmentView.findViewById<RecyclerView>(R.id.homeRecycleview)

        recyclerview.layoutManager = LinearLayoutManager(fragmentView.context)
        val data = MyDatabaseHelper(fragmentView.context).getFormData(null)
        val adapter = HomeCardAdapter(data)
        recyclerview.adapter = adapter

        return fragmentView
    }

    companion object
    {
        val INSTANCE = HomeFragment()
    }
}