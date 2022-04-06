package com.example.mymovieapp.util

object DataParseUtil {
    fun removeTags(title: String?): String? {
        var new_title = title?.replace("<b>", "")
        new_title = new_title?.replace("</b>", "")
        return new_title
    }
}