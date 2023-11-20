package com.deyvidandrades.feelwell.assistentes

import com.deyvidandrades.feelwell.objetos.Registro
import java.util.Calendar
import kotlin.random.Random


object AssistentePersistencia {

    private var registros = ArrayList<Registro>()

    init {
        gerarRegistros()
    }

    private fun gerarRegistros() {
        registros.clear()

        for (i in 0..30) {
            val registro = Registro.Sentimento.values().random()
            registros.add(
                Registro(
                    DataUtil.getTimestampFromDay(i, Calendar.getInstance().timeInMillis),
                    if (Random.nextInt(0, 2) == 1)
                        "Hoje estou me sendindo um pouco ${registro.name.lowercase()}.." else "",
                    registro
                )
            )
        }
        registros.sortedDescending()
    }

    fun getRegistros() = registros

    fun getOcorrencia(registro: Registro): Registro? {
        val registrosAux = registros
        registrosAux.remove(registro)

        for (reg in registrosAux)
            if (reg.getSentimento().name == registro.getSentimento().name)
                return reg
        return null
    }

    fun addRegistro(registro: Registro) {
        registros.add(registro)
    }

    fun delRegistro(registro: Registro) {
        registros.remove(registro)
    }
}