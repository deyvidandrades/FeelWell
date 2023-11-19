package com.deyvidandrades.feelwell.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.adaptadores.AdaptadorEmocoes
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.dialogos.DialogoCriarRegistro
import com.deyvidandrades.feelwell.objetos.Registro
import com.deyvidandrades.feelwell.objetos.Sentimento

interface OnItemClickListener {
    fun onItemClick(sentimento: Registro.Sentimento)
}

class CriarRegistroActivity : AppCompatActivity(), OnItemClickListener {
    private val arraySentimentos = ArrayList<Sentimento>()
    private lateinit var adaptadorSentimentos: AdaptadorEmocoes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_registro)


        val ivVoltar: ImageView = findViewById(R.id.btn_back)

        //Recycler Registros
        val recyclerSentimentos: RecyclerView = findViewById(R.id.recycler_registros)
        val layoutManagerDisciplinas =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        adaptadorSentimentos = AdaptadorEmocoes(this, arraySentimentos, this)

        recyclerSentimentos.setHasFixedSize(false)
        recyclerSentimentos.adapter = adaptadorSentimentos
        recyclerSentimentos.layoutManager = layoutManagerDisciplinas

        ivVoltar.setOnClickListener { v ->
            AnimacaoBotao.animar(v)
            finish()
        }

        carregarSentimentos()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun carregarSentimentos(sentimento: Registro.Sentimento? = null) {
        arraySentimentos.clear()

        for (item in Registro.Sentimento.entries) {

            if (sentimento != null) {
                arraySentimentos.add(
                    Sentimento(
                        item,
                        item.name == sentimento.name
                    )
                )
            } else
                arraySentimentos.add(Sentimento(item))
        }

        arraySentimentos.sortedDescending()
        adaptadorSentimentos.notifyDataSetChanged()
    }

    override fun onItemClick(sentimento: Registro.Sentimento) {
        Log.d("DWS", "AAAA")
        carregarSentimentos(sentimento)

        val customBottomSheet = DialogoCriarRegistro()
        customBottomSheet.sentimento = sentimento
        customBottomSheet.show(supportFragmentManager, DialogoCriarRegistro().javaClass.name)
    }
}