package com.example.viewpagertest.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.example.viewpagertest.R

class Helper {
    companion object {
        fun convertToHashMap(list:String?) : ArrayList<HashMap<String, String>> {
            val temp:ArrayList<HashMap<String, String>> = arrayListOf()
            val musicList = list?.split("@")

            for(music in musicList!!) {
                val removeFirst = music.replace("{", "")
                val removeLast = removeFirst.replace("}", "")
                val removeComma = removeLast.replace("\"", "")
                val removeSlash = removeComma.replace("\\", "")
                val output = removeSlash.split(",")
                    .map { it.split(":") }
                    .map { it.first() to it.last() }
                    .toMap()
                val hash = HashMap(output)
                temp.add(hash)
            }
            return temp
        }

        internal fun showProgress(context: Context, progressText: String) : Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.progress_bar)
            dialog.findViewById<TextView>(R.id.progressText).text = progressText
            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setGravity(Gravity.CENTER)
            return dialog
        }
    }
}