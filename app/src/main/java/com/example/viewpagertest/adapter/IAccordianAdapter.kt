package com.example.viewpagertest.adapter

import android.view.ViewGroup

interface IAccordianAdapter {
    enum class ArrowDirection {
        UP, DOWN, NONE
    }

    fun onCreateViewHolderForTitle(parent: ViewGroup): AccordianView.ViewHolder
    fun onCreateViewHolderForContent(parent: ViewGroup): AccordianView.ViewHolder
    fun onBindViewForTitle(viewHolder: AccordianView.ViewHolder, position: Int, arrowDirection: ArrowDirection)
    fun onBindViewForContent(viewHolder: AccordianView.ViewHolder, position: Int)
    fun getItemCount(): Int
}