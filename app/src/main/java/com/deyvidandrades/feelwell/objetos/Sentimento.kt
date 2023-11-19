package com.deyvidandrades.feelwell.objetos

data class Sentimento(
    private var sentimento: Registro.Sentimento,
    private var selecionado: Boolean = false
) : Comparable<Sentimento> {

    fun getSentimento() = sentimento
    fun isSelecionado() = selecionado

    override operator fun compareTo(other: Sentimento): Int {
        return getSentimento().name.compareTo(other.getSentimento().name)
    }

    override fun toString(): String {
        return "Horario(selecionado=$selecionado, sentimento=${sentimento}"
    }
}