package com.deyvidandrades.feelwell.dialogos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.objetos.Registro
import com.deyvidandrades.feelwell.objetos.Registro.Sentimento
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DialogoRegistro : BottomSheetDialogFragment() {
    var registro: Registro = Registro(0, "", Sentimento.ANIMADO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogoView = inflater.inflate(R.layout.dialogo_registro, container, false)

        val tvData: TextView = dialogoView.findViewById(R.id.tv_data)
        val tvSentimento: TextView = dialogoView.findViewById(R.id.tv_emocao)
        val tvAnotacao: TextView = dialogoView.findViewById(R.id.tv_anotacao)

        tvData.text = registro.getDataFormatada().replaceFirstChar { it.uppercase() }
        tvSentimento.text = registro.getSentimento().emoji

        tvAnotacao.visibility = if (registro.getAnotacao() != "") View.VISIBLE else View.GONE
        tvAnotacao.text = registro.getAnotacao()

        return dialogoView
    }

}