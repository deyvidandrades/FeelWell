package com.deyvidandrades.feelwell.assistentes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.deyvidandrades.feelwell.objetos.Registro
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import kotlin.random.Random


class Persistencia constructor(val context: Context) {
    private var arrayRegistros = ArrayList<Registro>()
    private var preferences: SharedPreferences

    enum class Chaves(val value: String) {
        DADOS("dados"),
        REGISTROS("registros"),
        HORARIO("horario")
    }

    init {
        preferences = context.getSharedPreferences(Chaves.DADOS.value, MODE_PRIVATE)
        carregarDados()

        if (arrayRegistros.isEmpty())
            gerarRegistrosDeTeste()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: Persistencia? = null
        private var horario: Int = 19

        fun getInstance(context: Context): Persistencia {
            if (instance == null)
                instance = Persistencia(context)

            return instance!!
        }
    }

    // Fluxo de testes
    private fun gerarRegistrosDeTeste() {
        arrayRegistros.clear()

        for (i in 1..50) {
            val registro = Registro.Sentimento.values().random()

            adicionarRegistro(
                Registro(
                    DataUtil.getTimestampFromDay(i, Calendar.getInstance().timeInMillis),
                    if (Random.nextInt(0, 2) == 1)
                        "Hoje estou me sendindo um pouco ${registro.name.lowercase()}.." else "",
                    registro
                )
            )
        }
    }

    // Fluxo de preferências
    private fun carregarDados() {
        try {
            val listaRaw = preferences.getString(Chaves.REGISTROS.value, "")
            val typeToken = object : TypeToken<ArrayList<Registro>>() {}.type

            arrayRegistros.clear()
            arrayRegistros.addAll(Gson().fromJson(listaRaw, typeToken))
        } catch (e: NullPointerException) {
            arrayRegistros = ArrayList()
        }
        horario = preferences.getInt(Chaves.HORARIO.value, 19)
    }

    private fun salvarDados() {
        arrayRegistros.sortedDescending()
        with(preferences.edit()) {
            putString(Chaves.REGISTROS.value, Gson().toJson(arrayRegistros))
            putInt(Chaves.HORARIO.value, horario)
            commit()
        }
    }

    private fun recarregarDados() {
        salvarDados()
        carregarDados()
    }

    //Fluxo de Registros
    fun adicionarRegistro(novoRegistro: Registro) {
        if (!arrayRegistros.contains(novoRegistro)) {
            arrayRegistros.add(novoRegistro)
            recarregarDados()
        }
    }

    fun removerRegistro(registro: Registro) {
        arrayRegistros.remove(registro)
        recarregarDados()
    }

    fun removerTodosRegistros() {
        arrayRegistros.clear()
        recarregarDados()
    }

    //Acesso aos registros
    fun getRegistros() = arrayRegistros

    fun getRegistrosPorSentimento(sentimento: Registro.Sentimento): ArrayList<Registro> {
        val arrayList = ArrayList<Registro>()
        for (item in arrayRegistros) {
            if (item.getSentimento() == sentimento)
                arrayList.add(item)
        }
        return arrayList
    }


    fun getOcorrencia(registro: Registro): Registro? {
        val registrosAux = arrayRegistros
        registrosAux.remove(registro)

        for (reg in registrosAux)
            if (reg.getSentimento().name == registro.getSentimento().name)
                return reg
        return null
    }

    //Fluxo de horário

    fun getHorario() = horario

    fun setHorario(novoHorario: Int) {
        horario = novoHorario
    }
}