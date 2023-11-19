package com.deyvidandrades.feelwell.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.adaptadores.AdaptadorEstatistica
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.assistentes.AssistentePersistencia
import com.deyvidandrades.feelwell.objetos.Registro
import com.deyvidandrades.feelwell.objetos.RegistroEstatistica

class StatisticsActivity : AppCompatActivity() {
    private var arrayEstatisticas = ArrayList<RegistroEstatistica>()
    private lateinit var adaptadorEstatistica: AdaptadorEstatistica

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val btnVoltar: ImageView = findViewById(R.id.btn_back)

        //Recycler Registros
        val recyclerRegistros: RecyclerView = findViewById(R.id.recycler_registros)
        val layoutManagerDisciplinas =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        adaptadorEstatistica = AdaptadorEstatistica(this, arrayEstatisticas)

        recyclerRegistros.setHasFixedSize(false)
        recyclerRegistros.adapter = adaptadorEstatistica
        recyclerRegistros.layoutManager = layoutManagerDisciplinas

        btnVoltar.setOnClickListener { v ->
            AnimacaoBotao.animar(v)
            finish()
        }

        carregarRegistros()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun carregarRegistros() {
        arrayEstatisticas.clear()
        val registros = AssistentePersistencia.carregarRegistros(this)

        for (emocao in Registro.Sentimento.entries) {
            var numSentimento = 0

            for (item in registros) {
                if (item.getSentimento() == emocao)
                    numSentimento++
            }
            val media = (numSentimento * 100) / registros.size

            if (media > 0)
                arrayEstatisticas.add(RegistroEstatistica(media, numSentimento, emocao))
        }

        arrayEstatisticas.sortDescending()
        adaptadorEstatistica.notifyDataSetChanged()
    }
}