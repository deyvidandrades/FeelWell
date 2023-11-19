package com.deyvidandrades.feelwell.objetos

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Registro(
    private var data: Long,
    private var anotacao: String = "",
    private var sentimento: Sentimento
) : Comparable<Registro> {

    enum class Sentimento(val emoji: String) {
        FELIZ("\uD83D\uDE0A"),
        TRISTE("\uD83D\uDE1E"),
        ANIMADO("\uD83D\uDE03"),
        ANSIOSO("\uD83D\uDE30"),
        RAIVA("\uD83D\uDE21"),
        RELAXADO("\uD83D\uDE0C"),
        ESTRESSADO("\uD83D\uDE2B"),
        SOZINHO("\uD83D\uDE14"),
        CONFIANTE("\uD83D\uDE0C"),
        GRATO("\uD83D\uDE4F"),
        PREOCUPADO("\uD83D\uDE1F"),
        FRUSTRADO("\uD83D\uDE24"),
        DECPCIONADO("\uD83D\uDE1E"),
        SOBRECARREGADO("\uD83D\uDE29"),
        INSEGURO("\uD83E\uDD7A")
    }

    fun getData() = data

    fun getDataFormatada(): String {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = data

        val diaSemana = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendario.time).slice(
            IntRange(0, 2)
        )
        val diaMes = SimpleDateFormat("dd", Locale.getDefault()).format(calendario.time)
        val mes = SimpleDateFormat("MMM", Locale.getDefault()).format(calendario.time)

        return "$diaSemana, $diaMes $mes".replaceFirstChar { it.uppercase() }
    }

    fun getAnotacao() = anotacao.replaceFirstChar { it.uppercase() }
    fun getSentimento() = sentimento

    override operator fun compareTo(other: Registro): Int {
        return getData().compareTo(other.getData())
    }

    override fun toString(): String {
        return "Horario(data=$data, sentimento=${sentimento}, anotacao=$anotacao)"
    }
}