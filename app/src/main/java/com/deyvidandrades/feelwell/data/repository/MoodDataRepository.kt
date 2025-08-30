package com.deyvidandrades.feelwell.data.repository

import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.source.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface MoodDataRepository {
    val moodArray: Flow<ArrayList<Mood>>
    suspend fun addMood(mood: Mood.MOODTYPE, reasons: ArrayList<Mood.REASON>)
    suspend fun deleteMood(createdAt: Long)
    suspend fun updateMood(mood: Mood)

    suspend fun debugAddMood(mood: Mood)
}

class MoodDataRepositoryImpl(private val preferencesDataStore: PreferencesDataStore) : MoodDataRepository {

    override val moodArray: Flow<ArrayList<Mood>> = preferencesDataStore.loadMoodArray

    override suspend fun debugAddMood(mood: Mood) { /*todo*/
        val currentMoodArray = preferencesDataStore.loadMoodArray.first()
        currentMoodArray.add(mood)
        preferencesDataStore.saveMoodData(ArrayList(currentMoodArray))
    }

    override suspend fun addMood(mood: Mood.MOODTYPE, reasons: ArrayList<Mood.REASON>) {
        val currentMoodArray = preferencesDataStore.loadMoodArray.first()
        currentMoodArray.add(Mood(mood, reasons))
        preferencesDataStore.saveMoodData(ArrayList(currentMoodArray))
    }

    override suspend fun deleteMood(createdAt: Long) {
        val currentMoodArray = preferencesDataStore.loadMoodArray.first()
        val updatedMoodArray = currentMoodArray.filter { it.createdAt != createdAt }
        preferencesDataStore.saveMoodData(ArrayList(updatedMoodArray))
    }

    override suspend fun updateMood(mood: Mood) {
        val currentMoodArray = preferencesDataStore.loadMoodArray.first()
        val index = currentMoodArray.indexOfFirst { it.createdAt == mood.createdAt }

        if (index != -1) currentMoodArray[index] = mood

        preferencesDataStore.saveMoodData(currentMoodArray)
    }
}