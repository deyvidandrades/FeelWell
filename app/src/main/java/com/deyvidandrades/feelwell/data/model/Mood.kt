package com.deyvidandrades.feelwell.data.model

import android.icu.util.Calendar
import android.text.format.DateUtils
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class Mood(
    val mood: MOODTYPE,
    val reasons: ArrayList<REASON> = ArrayList(),
    val createdAt: Long = Calendar.getInstance().timeInMillis
) {
    @Suppress("unused")
    enum class REASON { NONE, EU, FAMILIA, FRIENDS, RELACIONAMENTOS, SAUDE, TRABALHO }

    enum class MOODTYPE(val emoji: String, val isPositive: Boolean = false) {
        NONE(""),
        FELIZ("\uD83D\uDE0A", true),
        TRISTE("\uD83D\uDE1E"),
        ANIMADO("\uD83D\uDE03", true),
        ANSIOSO("\uD83D\uDE30"),
        RAIVA("\uD83D\uDE21"),
        RELAXADO("\uD83D\uDE0C", true),
        ESTRESSADO("\uD83D\uDE2B"),
        SOZINHO("\uD83D\uDE14"),
        CONFIANTE("\uD83D\uDE0E", true),
        GRATO("\uD83D\uDE4F", true),
        PREOCUPADO("\uD83D\uDE1F"),
        FRUSTRADO("\uD83D\uDE24"),
        DECEPCIONADO("\uD83D\uDE1E"),
        SOBRECARREGADO("\uD83D\uDE29"),
        INSEGURO("\uD83E\uDD7A")
    }

    fun getMonthString(): String {
        val instant = Instant.ofEpochMilli(createdAt)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("MMM", Locale.getDefault())

        return zonedDateTime.format(formatter).replace(".", "").replaceFirstChar { it.uppercase() }
    }

    fun getDateString(): String {
        val instant = Instant.ofEpochMilli(createdAt)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("eee, dd MMM", Locale.getDefault())

        return zonedDateTime.format(formatter).replaceFirstChar { it.uppercase() }
    }

    fun getDayOfMonth(): Int {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = createdAt
        return calendar.get(java.util.Calendar.DAY_OF_MONTH)
    }

    fun isToday(): Boolean = DateUtils.isToday(createdAt)
}
