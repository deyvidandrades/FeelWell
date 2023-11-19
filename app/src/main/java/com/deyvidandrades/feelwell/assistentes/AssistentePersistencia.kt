package com.deyvidandrades.feelwell.assistentes

import android.content.Context
import com.deyvidandrades.feelwell.objetos.Registro
import java.util.Calendar


class AssistentePersistencia {

    companion object {
        private var registros = ArrayList<Registro>()

        fun carregarRegistros(
            context: Context
        ): ArrayList<Registro> {
            registros.clear()

            for (i in 0..30) {
                registros.add(
                    Registro(
                        DataUtil.getTimestampFromDay(i, Calendar.getInstance().timeInMillis),
                        "isso é um teste",
                        Registro.Sentimento.values().random()
                    )
                )
            }

            return registros
        }
    }
}