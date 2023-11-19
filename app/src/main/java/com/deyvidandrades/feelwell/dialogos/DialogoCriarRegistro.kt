package com.deyvidandrades.feelwell.dialogos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.activities.MainActivity
import com.deyvidandrades.feelwell.assistentes.AnimacaoBotao
import com.deyvidandrades.feelwell.objetos.Registro
import com.deyvidandrades.feelwell.objetos.Registro.Sentimento
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar


class DialogoCriarRegistro : BottomSheetDialogFragment() {
    var sentimento: Sentimento = Sentimento.FELIZ

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogoView = inflater.inflate(R.layout.dialogo_criar_registro, container, false)

        val tvData: TextView = dialogoView.findViewById(R.id.tv_data)
        val tvSentimento: TextView = dialogoView.findViewById(R.id.tv_emocao)
        val tvSentimentoEmoji: TextView = dialogoView.findViewById(R.id.tv_emocao_emoji)
        val etAnotacao: EditText = dialogoView.findViewById(R.id.et_anotacao)
        val btnShare: ImageView = dialogoView.findViewById(R.id.btn_share)
        val btnSave: ImageView = dialogoView.findViewById(R.id.btn_save)
        val btnCancelar: TextView = dialogoView.findViewById(R.id.btn_cancelar)

        val registro =
            Registro(Calendar.getInstance().timeInMillis, etAnotacao.text.toString(), sentimento)

        tvData.text = registro.getDataFormatada()
        tvSentimento.text =
            "Hoje você está se sentindo ${registro.getSentimento().name.lowercase()}"
        tvSentimentoEmoji.text = registro.getSentimento().emoji

        btnShare.setOnClickListener { v ->
            AnimacaoBotao.animar(v)
            Snackbar.make(dialogoView, "Não implementado!", Snackbar.LENGTH_LONG).show()
        }

        btnSave.setOnClickListener { v ->
            AnimacaoBotao.animar(v)

            //todo salvar
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        btnCancelar.setOnClickListener { v ->
            AnimacaoBotao.animar(v)

            dismiss()
        }

        return dialogoView
    }

}