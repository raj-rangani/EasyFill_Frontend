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
        data.add(Form(1, "Raj Rangani", "Adhar card Form", 10, 20))
        data.add(Form(2, "Parth Trilokchandani", "Adhar card Form", 10, 60))
        data.add(Form(3, "Raj Rangani", "Pan Card Form", 10, 50))
        data.add(Form(4, "Sachin Sudani", "Voting card Form", 10, 90))
        data.add(Form(5, "Bhavik Mungra", "Adhar card Form", 10, 20))

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