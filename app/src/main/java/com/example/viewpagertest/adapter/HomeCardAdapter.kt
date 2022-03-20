package com.example.viewpagertest.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpagertest.DisplayFormActivity
import com.example.viewpagertest.models.Form
import com.example.viewpagertest.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class HomeCardAdapter(private val mList: ArrayList<Form>) : RecyclerView.Adapter<HomeCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_card_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.progress.progress = ItemsViewModel.fillpercent!!.toInt()
        holder.formName.text = ItemsViewModel.formname
        holder.formAuthor.text = ItemsViewModel.authorname
        holder.fillPercent.text = ItemsViewModel.fillpercent.toString()
        holder.itemView.tag = ItemsViewModel.id.toString()

        if(ItemsViewModel.fillpercent!!.toInt() >= 90) {
            holder.background.backgroundTintList =
                ColorStateList.valueOf(holder.itemView.context.resources.getColor(R.color.success))
        }
        else if(ItemsViewModel.fillpercent!!.toInt() in 40..90) {
            holder.background.backgroundTintList =
                ColorStateList.valueOf(holder.itemView.context.resources.getColor(R.color.warning))
        }
        else {
            holder.background.backgroundTintList =
                ColorStateList.valueOf(holder.itemView.context.resources.getColor(R.color.error))
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val progress: CircularProgressIndicator = itemView.findViewById(R.id.formProgress)
        val formName: TextView = itemView.findViewById(R.id.formName)
        val formAuthor: TextView = itemView.findViewById(R.id.formAuthor)
        val background: CardView = itemView.findViewById(R.id.fillPercentBackground)
        val fillPercent: TextView = itemView.findViewById(R.id.fillPercent)

        init {
            itemView.setOnClickListener {

                val intent = Intent(itemView.context, DisplayFormActivity::class.java)
                intent.putExtra("File", formName.text.toString())
                intent.putExtra("FormId", itemView.tag.toString().toInt())

                val prefs = itemView.context?.getSharedPreferences("FORM", Context.MODE_PRIVATE)
                val prefEditor = prefs?.edit()
                prefEditor?.putString("Form", itemView.tag.toString())
                prefEditor?.apply()

                itemView.context.startActivity(intent)
            }
        }
    }
}