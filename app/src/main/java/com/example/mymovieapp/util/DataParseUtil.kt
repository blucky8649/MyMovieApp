package com.example.mymovieapp.util

import java.text.SimpleDateFormat
import java.util.*

object DataParseUtil {
    fun removeTags(title: String?): String? {
        var new_title = title?.replace("<b>", "")
        new_title = new_title?.replace("</b>", "")
        return new_title
    }

    fun formatDate(d: Long): String {
        val date = Date(d)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
}