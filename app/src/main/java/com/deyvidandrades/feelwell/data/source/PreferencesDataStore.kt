package com.deyvidandrades.feelwell.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

interface PreferencesDataStore {
    val loadSettings: Flow<Settings>
    val loadMoodArray: Flow<ArrayList<Mood>>
    suspend fun saveSettings(settings: Settings)
    suspend fun saveMoodData(array: ArrayList<Mood>)
}

class PreferencesDataStoreImpl(private val dataStore: DataStore<Preferences>) : PreferencesDataStore {

    private object PreferencesKeys {
        val KEY_MOOD = stringPreferencesKey("mood")
        val KEY_SETTINGS = stringPreferencesKey("settings")
    }

    override val loadSettings: Flow<Settings> = dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.KEY_SETTINGS] ?: "{}"

        try {
            Json.decodeFromString<Settings>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            Settings(
                "",
                notifications = true,
                notificationTime = 20,
                isDarkTheme = false,
                isFirstTime = true,
                quickAction1 = Mood.MOODTYPE.FELIZ,
                quickAction2 = Mood.MOODTYPE.CALMO,
                quickAction3 = Mood.MOODTYPE.TRISTE
            )
        }
    }

    override val loadMoodArray: Flow<ArrayList<Mood>> = dataStore.data.map { preferences ->
        val jsonString = preferences[PreferencesKeys.KEY_MOOD] ?: "{}"

        try {
            Json.decodeFromString<ArrayList<Mood>>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    override suspend fun saveSettings(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_SETTINGS] = Json.encodeToString(settings)
        }
    }

    override suspend fun saveMoodData(array: ArrayList<Mood>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_MOOD] = Json.encodeToString(array)
        }
    }
}
