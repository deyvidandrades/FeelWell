package com.deyvidandrades.feelwell.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.adaptadores.AdaptadorRegistros
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.assistentes.AssistentePersistencia
import com.deyvidandrades.feelwell.objetos.Registro

class MainActivity : AppCompatActivity() {
    private var arrayRegistros = ArrayList<Registro>()
    private lateinit var adaptadorRegistros: AdaptadorRegistros
    private val channelId = "criar_registro"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStatistics: ImageView = findViewById(R.id.btn_statistics)
        val btnConfig: ImageView = findViewById(R.id.btn_opcoes)
        val btnAdd: RelativeLayout = findViewById(R.id.btn_add)
        val etBuscar: EditText = findViewById(R.id.et_buscar)

        //Recycler Registros
        val recyclerRegistros: RecyclerView = findViewById(R.id.recycler_registros)
        val layoutManagerDisciplinas =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adaptadorRegistros = AdaptadorRegistros(this, arrayRegistros)

        recyclerRegistros.setHasFixedSize(false)
        recyclerRegistros.adapter = adaptadorRegistros
        recyclerRegistros.layoutManager = layoutManagerDisciplinas


        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                carregarRegistros(etBuscar.text.toString())
            }
        })

        btnStatistics.setOnClickListener { v ->
            AnimacaoBotao.animar(v)

            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        btnConfig.setOnClickListener { v ->
            AnimacaoBotao.animar(v)

            val intent = Intent(this, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }

        btnAdd.setOnClickListener { v ->
            AnimacaoBotao.animar(v)
        }

        carregarRegistros()

        createNotificationChannel()

        criarNotificacao()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun carregarRegistros(busca: String = "") {
        arrayRegistros.clear()

        val registros = AssistentePersistencia.carregarRegistros(this)

        if (busca != "") {
            for (item in registros)
                if (item.getDataFormatada().lowercase().contains(busca.lowercase()))
                    arrayRegistros.add(item)
        } else
            arrayRegistros.addAll(registros)

        arrayRegistros.sortedDescending()
        adaptadorRegistros.notifyDataSetChanged()
    }

    private fun criarNotificacao() {
        val intent = Intent(this, CriarRegistroActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_spa_24)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.como_voc_est_se_sentindo_hoje))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                R.drawable.baseline_spa_24,
                "${Registro.Sentimento.ANIMADO.emoji} ${
                    Registro.Sentimento.ANIMADO.name.lowercase().replaceFirstChar { it.uppercase() }
                }",
                null
            )
            .addAction(
                R.drawable.baseline_spa_24,
                "${Registro.Sentimento.RELAXADO.emoji} ${
                    Registro.Sentimento.RELAXADO.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                }",
                null
            )
            .addAction(
                R.drawable.baseline_spa_24,
                "${Registro.Sentimento.TRISTE.emoji} ${
                    Registro.Sentimento.TRISTE.name.lowercase().replaceFirstChar { it.uppercase() }
                }",
                null
            )
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(this)) {

            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
                }
                return
            }
            notify(0, builder.build())
        }
    }

    private fun createNotificationChannel() {

        val name = getString(R.string.channel_description)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}