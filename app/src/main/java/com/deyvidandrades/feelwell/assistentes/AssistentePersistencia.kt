package com.deyvidandrades.feelwell.assistentes

import android.content.Context
import com.deyvidandrades.feelwell.objetos.Registro


class AssistentePersistencia {

    companion object {
        private var registros = ArrayList<Registro>()

        fun carregarRegistros(
            context: Context
        ): ArrayList<Registro> {
            registros.clear()

            registros.add(
                Registro(
                    1698838528000,
                    "isso é um teste",
                    Registro.Sentimento.PREOCUPADO
                )
            )
            registros.add(Registro(1698752128000, "", Registro.Sentimento.ESTRESSADO))
            registros.add(Registro(1698665728000, "", Registro.Sentimento.SOBRECARREGADO))
            registros.add(Registro(1698579328000, "", Registro.Sentimento.RAIVA))

            for (i in 0..(0..20).random()) {
                registros.add(
                    Registro(
                        1698838528000,
                        "isso é um teste",
                        Registro.Sentimento.values().random()
                    )
                )
            }

            return registros
        }
    }
}