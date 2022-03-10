package com.example.viewpagertest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.example.viewpagertest.R

class ProfileGroupAdapter(val dataArray: List<DataModel>) : IAccordianAdapter {
    override fun onCreateViewHolderForTitle(parent: ViewGroup): AccordianView.ViewHolder {
        return TitleViewHolder.create(parent)
    }

    override fun onCreateViewHolderForContent(parent: ViewGroup): AccordianView.ViewHolder {
        return ContentViewHolder.create(parent)
    }

    override fun onBindViewForTitle(viewHolder: AccordianView.ViewHolder, position: Int, arrowDirection: IAccordianAdapter.ArrowDirection) {
        val dataModel = dataArray[position]
        (viewHolder as TitleViewHolder).itemView.apply {
            this.findViewById<TextView>(R.id.titleTextView).text = dataModel.title
            when (arrowDirection) {
                IAccordianAdapter.ArrowDirection.UP -> this.findViewById<TextView>(R.id.titleArrowIcon).text = "▲"
                IAccordianAdapter.ArrowDirection.DOWN -> this.findViewById<TextView>(R.id.titleArrowIcon).text = "▼"
                IAccordianAdapter.ArrowDirection.NONE -> this.findViewById<TextView>(R.id.titleArrowIcon).text = ""
            }
        }
    }

    override fun onBindViewForContent(viewHolder: AccordianView.ViewHolder, position: Int) {
        val dataModel = dataArray[position]
        (viewHolder as ContentViewHolder).itemView.apply {
            if(this.findViewById<LinearLayout>(R.id.contentView).childCount >= 1) {
                this.findViewById<LinearLayout>(R.id.contentView).removeAllViewsInLayout()
            }
            this.findViewById<LinearLayout>(R.id.contentView).addView(dataModel.view)
        }
    }

    override fun getItemCount() = dataArray.size
}

class TitleViewHolder(itemView: View) : AccordianView.ViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup): TitleViewHolder {
            return TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.title_view, parent, false))
        }
    }
}

class ContentViewHolder(itemView: View) : AccordianView.ViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup): ContentViewHolder {
            return ContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.content_view, parent, false))
        }
    }
}