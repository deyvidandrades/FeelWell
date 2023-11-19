package com.deyvidandrades.feelwell.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.objetos.Registro
import com.deyvidandrades.feelwell.objetos.RegistroEstatistica
import com.google.android.material.progressindicator.CircularProgressIndicator
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit


class AdaptadorEstatistica(context: Context, arrayList: ArrayList<RegistroEstatistica>) :
    RecyclerView.Adapter<AdaptadorEstatistica.ViewHolder>() {

    private val context: Context
    private var arrayList: ArrayList<RegistroEstatistica> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.item_estatistica, parent, false
        )

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registroEstatistica = arrayList[position]

        holder.progress.progress = registroEstatistica.getPorcentagem()
        holder.tv_porcentagem.text = registroEstatistica.getSentimento().emoji

        holder.tv_emocao.text =
            registroEstatistica.getSentimento().name.lowercase().replaceFirstChar { it.uppercase() }

        holder.tv_porcentagem_texto.text = if (registroEstatistica.getPorcentagem() > 1)
            "Você se sentiu ${registroEstatistica.getSentimento().name.lowercase()} ${registroEstatistica.getPorcentagem()}% do tempo." else
            "Você ainda não se sentiu ${registroEstatistica.getSentimento().name.lowercase()}."

        if (position == 0 && registroEstatistica.getSentimento() == Registro.Sentimento.FELIZ) {
            holder.konfettiView.start(
                Party(
                    speed = 0f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                    position = Position.Relative(0.5, 0.3)
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progress: CircularProgressIndicator
        var tv_porcentagem: TextView
        var tv_emocao: TextView
        var tv_porcentagem_texto: TextView
        var konfettiView: KonfettiView

        init {
            progress = itemView.findViewById(R.id.progress)
            tv_porcentagem = itemView.findViewById(R.id.tv_porcentagem)
            tv_emocao = itemView.findViewById(R.id.tv_emocao)
            tv_porcentagem_texto = itemView.findViewById(R.id.tv_porcentagem_texto)
            konfettiView = itemView.findViewById(R.id.konfettiView)
        }
    }

    init {
        this.context = context
        this.arrayList = arrayList
    }
}