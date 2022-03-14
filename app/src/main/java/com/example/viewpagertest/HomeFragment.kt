package com.example.viewpagertest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.models.Form
import com.example.viewpagertest.adapter.HomeCardAdapter

class HomeFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val fragmentView = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerview = fragmentView.findViewById<RecyclerView>(R.id.homeRecycleview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(fragmentView.context)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Form>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..10) {
            data.add(Form(null, "raj-rangani", "abc", 20, i*10))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = HomeCardAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        return fragmentView
    }

    companion object
    {
        val INSTANCE = HomeFragment()
    }
}