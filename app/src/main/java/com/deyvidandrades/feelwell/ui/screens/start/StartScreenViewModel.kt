package com.deyvidandrades.feelwell.ui.screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StartScreenViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val stateFlowIsFirstTime: StateFlow<Boolean> = settingsRepository.settings.map { it.isFirstTime }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = true
    )

    fun setStarterInformation(
        userName: String,
        notifications: Boolean,
        quickAction1: Mood.MOODTYPE,
        quickAction2: Mood.MOODTYPE,
        quickAction3: Mood.MOODTYPE
    ) {
        viewModelScope.launch {
            settingsRepository.updateFirstTime(false)
            settingsRepository.updateNotifications(notifications)
            settingsRepository.updateUserName(userName)
            settingsRepository.updateQuickActions(quickAction1, quickAction2, quickAction3)
        }
    }
}
