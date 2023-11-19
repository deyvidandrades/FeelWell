package com.deyvidandrades.feelwell.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.fragmentos.FragmentoPreferencias

class ConfiguracoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, FragmentoPreferencias())
            .commit()

        val ivVoltar: ImageView = findViewById(R.id.btn_back)

        ivVoltar.setOnClickListener { v ->
            AnimacaoBotao.animar(v)
            finish()
        }
    }
}