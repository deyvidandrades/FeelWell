package com.deyvidandrades.feelwell.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvidandrades.feelwell.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ManualNavigationViewModel(settingsRepository: SettingsRepository, addNewMood: Boolean) : ViewModel() {

    val stateFlowIsFirstTime: StateFlow<Boolean> = settingsRepository.settings.map { it.isFirstTime }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = false
    )

    var isNewMood = addNewMood
}