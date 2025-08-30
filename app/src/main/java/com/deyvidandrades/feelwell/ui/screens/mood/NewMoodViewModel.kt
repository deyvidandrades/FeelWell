package com.deyvidandrades.feelwell.ui.screens.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.repository.MoodDataRepository
import com.deyvidandrades.feelwell.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewMoodViewModel(settingsRepository: SettingsRepository, private val moodDataRepository: MoodDataRepository) :
    ViewModel() {

    val stateFlowUserName: StateFlow<String> = settingsRepository.settings.map { it.userName }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = ""
    )

    fun addNewMood(mood: Mood.MOODTYPE, reasons: ArrayList<Mood.REASON>) {
        viewModelScope.launch { moodDataRepository.addMood(mood, reasons) }
    }
}