package com.deyvidandrades.feelwell.data.repository

import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.model.Settings
import com.deyvidandrades.feelwell.data.source.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface SettingsRepository {
    val settings: Flow<Settings>
    suspend fun updateUserName(userName: String)
    suspend fun updateDarkTheme(enabled: Boolean)
    suspend fun updateFirstTime(enabled: Boolean)
    suspend fun updateNotifications(enabled: Boolean)
    suspend fun updateNotificationTime(time: Int)
    suspend fun updateQuickActions(action1: Mood.MOODTYPE, action2: Mood.MOODTYPE, action3: Mood.MOODTYPE)
    suspend fun deleteAllMoodData()
}

class SettingsRepositoryImpl(private val preferencesDataStore: PreferencesDataStore) : SettingsRepository {
    override val settings: Flow<Settings> = preferencesDataStore.loadSettings

    override suspend fun updateUserName(userName: String) {
        val currentSettings = preferencesDataStore.loadSettings.first()
        currentSettings.userName = userName
        preferencesDataStore.saveSettings(currentSettings)
    }

    override suspend fun updateNotifications(enabled: Boolean) {
        val currentSettings = preferencesDataStore.loadSettings.first()
        currentSettings.notifications = enabled
        preferencesDataStore.saveSettings(currentSettings)
    }

    override suspend fun updateNotificationTime(time: Int) {
        val currentSettings = preferencesDataStore.loadSettings.first()
        currentSettings.notificationTime = time
        preferencesDataStore.saveSettings(currentSettings)
    }

    override suspend fun updateDarkTheme(enabled: Boolean) {
        val currentSettings = preferencesDataStore.loadSettings.first()
        currentSettings.isDarkTheme = enabled
        preferencesDataStore.saveSettings(currentSettings)
    }

    override suspend fun updateFirstTime(enabled: Boolean) {
        val currentSettings = preferencesDataStore.loadSettings.first()
        currentSettings.isFirstTime = enabled
        preferencesDataStore.saveSettings(currentSettings)
    }

    override suspend fun updateQuickActions(action1: Mood.MOODTYPE, action2: Mood.MOODTYPE, action3: Mood.MOODTYPE) {
        val currentSettings = preferencesDataStore.loadSettings.first()
        currentSettings.quickAction1 = action1
        currentSettings.quickAction2 = action2
        currentSettings.quickAction3 = action3
        preferencesDataStore.saveSettings(currentSettings)
    }

    override suspend fun deleteAllMoodData() {
        preferencesDataStore.saveMoodData(ArrayList())
    }
}