package com.deyvidandrades.feelwell.activities

import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao

class TermosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termos)

        val ivVoltar: ImageView = findViewById(R.id.btn_back)
        val tvTermos: TextView = findViewById(R.id.tvTermos)

        tvTermos.text = Html.fromHtml(getString(R.string.politica_privacidade), 0)

        ivVoltar.setOnClickListener { v ->
            AnimacaoBotao.animar(v)
            finish()
        }
    }
}