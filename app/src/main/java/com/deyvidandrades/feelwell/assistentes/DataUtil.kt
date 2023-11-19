package com.deyvidandrades.feelwell.assistentes

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DataUtil {

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEE d MMM yy", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    /*private fun formatHours(timestamp: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Calendar.getInstance()
        date.timeInMillis = timestamp
        val differenceMillis = currentTimeMillis - date.timeInMillis
        val hoursDifference = TimeUnit.MILLISECONDS.toHours(differenceMillis)

        return "$hoursDifference hours ago"
    }*/
    private fun formatHours(timestamp: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Calendar.getInstance()
        date.timeInMillis = timestamp
        val differenceMillis = currentTimeMillis - date.timeInMillis
        val hoursDifference = TimeUnit.MILLISECONDS.toHours(differenceMillis)

        return "${hoursDifference.toInt()} hours ago"
    }

    fun getDataFormatada(timestamp: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val date = Calendar.getInstance()
        date.timeInMillis = timestamp
        val differenceMillis = currentTimeMillis - date.timeInMillis
        val daysDifference = TimeUnit.MILLISECONDS.toDays(differenceMillis)

        return if (daysDifference >= 1) {
            formatDate(timestamp)
        } else {
            formatHours(timestamp)
        }
    }

    fun getTimestampFromDay(
        days: Int, timestamp: Long = Calendar.getInstance().timeInMillis
    ): Long {
        val calendar = Calendar.getInstance()
        val date = Calendar.getInstance()
        date.timeInMillis = timestamp
        calendar.time = date.time
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return calendar.timeInMillis
    }
}