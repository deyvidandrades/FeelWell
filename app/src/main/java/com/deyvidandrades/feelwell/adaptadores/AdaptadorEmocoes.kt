package com.deyvidandrades.feelwell.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.activities.OnItemClickListener
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.objetos.Sentimento


class AdaptadorEmocoes(
    context: Context,
    arrayList: ArrayList<Sentimento>,
    listener: OnItemClickListener
) :
    RecyclerView.Adapter<AdaptadorEmocoes.ViewHolder>() {

    private val context: Context
    private var arrayList: ArrayList<Sentimento> = ArrayList()
    private val listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.item_sentimento_escolha, parent, false
        )
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sentimento = arrayList[position]

        holder.tvSentimento.text = sentimento.getSentimento().name
        holder.tvSentimentoEmoji.text = sentimento.getSentimento().emoji

        holder.li_bg.backgroundTintList = ColorStateList.valueOf(
            if (sentimento.isSelecionado()) context.getColor(R.color.accent) else context.getColor(R.color.textLight)
        )

        holder.itemView.setOnClickListener { v ->
            AnimacaoBotao.animar(v)

            listener.onItemClick(sentimento.getSentimento())
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSentimento: TextView
        var tvSentimentoEmoji: TextView
        var li_bg: LinearLayout

        init {
            li_bg = itemView.findViewById(R.id.bg)
            tvSentimento = itemView.findViewById(R.id.tv_emocao)
            tvSentimentoEmoji = itemView.findViewById(R.id.tv_emocao_emoji)
        }
    }

    init {
        this.context = context
        this.arrayList = arrayList
        this.listener = listener
    }
}