package com.deyvidandrades.feelwell.data.model

import android.icu.util.Calendar
import android.text.format.DateUtils
import com.deyvidandrades.feelwell.R
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

    enum class TYPE { POSITIVE, NEUTRAL, NEGATIVE }

    enum class MOODTYPE(val title: Int, val emoji: String, val type: TYPE) {
        NONE(-1, "", TYPE.NEUTRAL),
        FELIZ(R.string.moodtype_feliz, "😊", TYPE.POSITIVE),
        CALMO(R.string.moodtype_calmo, "\uD83D\uDE0C", TYPE.POSITIVE),
        ALEGRE(R.string.moodtype_alegre, "\uD83D\uDE04", TYPE.POSITIVE),
        ANIMADO(R.string.moodtype_animado, "\uD83E\uDD29", TYPE.POSITIVE),
        ANSIOSO(R.string.moodtype_ansioso, "\uD83D\uDE25", TYPE.NEGATIVE),
        TRISTE(R.string.moodtype_triste, "\uD83D\uDE14", TYPE.NEGATIVE),
        IRRITADO(R.string.moodtype_irritado, "\uD83D\uDE21", TYPE.NEGATIVE),
        ESTRESSADO(R.string.moodtype_estressado, "\uD83D\uDE2B", TYPE.NEGATIVE),
        CANSADO(R.string.moodtype_cansado, "\uD83D\uDE34", TYPE.NEGATIVE),
        PENSATIVO(R.string.moodtype_pensativo, "\uD83E\uDD14", TYPE.NEUTRAL),
        ENTEDIADO(R.string.moodtype_entediado, "\uD83D\uDE10", TYPE.NEUTRAL),
        CURIOSO(R.string.moodtype_curioso, "\uD83E\uDDD0", TYPE.NEUTRAL),
        CONFUSO(R.string.moodtype_confuso, "\uD83D\uDE15", TYPE.NEUTRAL),
        ESPERANCOSO(R.string.moodtype_esperancoso, "\uD83D\uDE07", TYPE.POSITIVE),
        AMADO(R.string.moodtype_amado, "\uD83E\uDD70", TYPE.POSITIVE),
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
