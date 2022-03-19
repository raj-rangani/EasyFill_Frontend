package com.example.viewpagertest.helper

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
    }
}