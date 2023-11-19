package com.deyvidandrades.feelwell.objetos

data class RegistroEstatistica(
    private var porcentagem: Int,
    private var numOcorrencias: Int,
    private var sentimento: Registro.Sentimento
) : Comparable<RegistroEstatistica> {

    fun getPorcentagem() = porcentagem
    fun getNumOcorrencias() = numOcorrencias
    fun getSentimento() = sentimento

    override operator fun compareTo(other: RegistroEstatistica): Int {
        return getPorcentagem().compareTo(other.getPorcentagem())
    }

    override fun toString(): String {
        return "Horario(porcentagem=$porcentagem, ocorrencias=$numOcorrencias, sentimento=$sentimento"
    }
}