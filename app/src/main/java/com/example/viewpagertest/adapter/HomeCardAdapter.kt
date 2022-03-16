package com.example.viewpagertest.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.database.models.Form
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
    }
}