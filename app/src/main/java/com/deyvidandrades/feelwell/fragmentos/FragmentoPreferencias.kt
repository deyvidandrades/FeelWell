package com.deyvidandrades.feelwell.fragmentos

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreference
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.activities.TermosActivity
import com.deyvidandrades.feelwell.assistentes.Persistencia

class FragmentoPreferencias : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val notificacoes: SwitchPreference? = findPreference("notificacoes")
        val notificacaoDiaria: SwitchPreference? = findPreference("notificacao_diaria")
        val temaEscuro: SwitchPreference? = findPreference("tema_escuro")

        val versao: Preference? = findPreference("versao")
        val preferenciaPrivacidade: Preference? = findPreference("privacidade")
        val preferenciaDeletar: Preference? = findPreference("deletar")

        val seekBarHorario: SeekBarPreference? = findPreference("horario")

        seekBarHorario?.apply {
            value = Persistencia(requireContext()).getHorario()
        }

        val info = requireContext().packageManager.getPackageInfo(
            requireContext().packageName, PackageManager.GET_ACTIVITIES
        )

        versao?.apply {
            summary =
                "${getString(R.string.app_name)} v${info.versionName}\nDesenvolvido por @deyvidandrades"
        }

        notificacoes!!.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                notificacaoDiaria!!.isChecked = false
            }

            true
        }

        temaEscuro!!.setOnPreferenceChangeListener { _, newValue ->
            AppCompatDelegate.setDefaultNightMode(if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)

            true
        }

        seekBarHorario!!.setOnPreferenceChangeListener { _, newValue ->
            Persistencia(requireContext()).setHorario(newValue.toString().toInt())
            true
        }

        preferenciaPrivacidade?.setOnPreferenceClickListener {
            //val browserIntent =
            //    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uld_politica)))
            //startActivity(browserIntent)
            val intent = Intent(requireContext(), TermosActivity::class.java)
            startActivity(intent)
            true
        }

        preferenciaDeletar?.setOnPreferenceClickListener {
            Persistencia(requireContext()).removerTodosRegistros()
            Toast.makeText(
                requireContext(),
                getString(R.string.registros_removidos),
                Toast.LENGTH_LONG
            ).show()
            true
        }
    }
}
