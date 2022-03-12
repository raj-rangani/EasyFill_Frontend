package com.example.viewpagertest.adapter

import android.view.View
import android.view.ViewGroup

interface IAccordianAdapter {
    enum class ArrowDirection {
        UP, DOWN, NONE
    }
    var parentTitle: ViewGroup?

    fun onCreateViewHolderForTitle(parent: ViewGroup): AccordianView.ViewHolder
    fun onCreateViewHolderForContent(parent: ViewGroup): AccordianView.ViewHolder
    fun onBindViewForTitle(viewHolder: AccordianView.ViewHolder, position: Int, arrowDirection: ArrowDirection)
    fun onBindViewForContent(viewHolder: AccordianView.ViewHolder, position: Int)
    fun getItemCount(): Int
}