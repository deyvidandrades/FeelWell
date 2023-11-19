package com.deyvidandrades.feelwell.adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.dialogos.DialogoRegistro
import com.deyvidandrades.feelwell.objetos.Registro


class AdaptadorRegistros(context: Context, arrayList: ArrayList<Registro>) :
    RecyclerView.Adapter<AdaptadorRegistros.ViewHolder>() {

    private val context: Context
    private var arrayList: ArrayList<Registro> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.item_registro, parent, false
        )

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registro = arrayList[position]

        holder.tvData.text = registro.getDataFormatada()

        holder.tvAnotacao.text =
            "Você estáva se sendtindo ${registro.getSentimento().name.lowercase()}."

        holder.tvSentimento.text = registro.getSentimento().emoji

        holder.itemView.setOnClickListener { v ->
            AnimacaoBotao.animar(v)

            val customBottomSheet = DialogoRegistro()
            customBottomSheet.registro = registro
            customBottomSheet.show(
                (context as AppCompatActivity).supportFragmentManager,
                DialogoRegistro().javaClass.name
            )
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvData: TextView
        var tvAnotacao: TextView
        var tvSentimento: TextView

        init {
            tvData = itemView.findViewById(R.id.tv_data)
            tvAnotacao = itemView.findViewById(R.id.tv_anotacao)
            tvSentimento = itemView.findViewById(R.id.tv_emocao)
        }
    }

    init {
        this.context = context
        this.arrayList = arrayList
    }
}