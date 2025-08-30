package com.deyvidandrades.feelwell.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvidandrades.feelwell.data.helpers.WorkManagerHelper
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.model.Settings
import com.deyvidandrades.feelwell.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val stateFlowSettings: StateFlow<Settings> = settingsRepository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = Settings("", true, 20, isDarkTheme = false, isFirstTime = true)
    )

    fun setNotifications(value: Boolean, context: Context) {
        viewModelScope.launch {
            settingsRepository.updateNotifications(value)

            if (!value)
                WorkManagerHelper.stopWorker(context)
            else
                WorkManagerHelper.initWorker(context, settingsRepository.settings.first().notificationTime)
        }
    }

    fun setNotificationTime(value: Int, context: Context) {
        viewModelScope.launch {
            settingsRepository.updateNotificationTime(value)

            WorkManagerHelper.stopWorker(context)
            WorkManagerHelper.initWorker(context, value)
        }
    }

    fun setDarkTheme(value: Boolean) {
        viewModelScope.launch { settingsRepository.updateDarkTheme(value) }
    }

    fun setUserName(value: String) {
        viewModelScope.launch { settingsRepository.updateUserName(value) }
    }

    fun setQuickAction1(value: Mood.MOODTYPE) {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            settingsRepository.updateQuickActions(value, settings.quickAction2, settings.quickAction3)
        }
    }

    fun setQuickAction2(value: Mood.MOODTYPE) {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            settingsRepository.updateQuickActions(settings.quickAction1, value, settings.quickAction3)
        }
    }

    fun setQuickAction3(value: Mood.MOODTYPE) {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            settingsRepository.updateQuickActions(settings.quickAction1, settings.quickAction2, value)
        }
    }

    fun deleteAllMoodData() {
        viewModelScope.launch {
//            settingsRepository.deleteAllMoodData()
            settingsRepository.updateFirstTime(true)
        }
    }
}