package com.deyvidandrades.feelwell.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.model.QuickActionAverage
import com.deyvidandrades.feelwell.data.repository.MoodDataRepository
import com.deyvidandrades.feelwell.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(private val moodDataRepository: MoodDataRepository, settingsRepository: SettingsRepository) :
    ViewModel() {

    /*todo optimize code in this class */

    val stateFlowUserName: StateFlow<String> = settingsRepository.settings.map { it.userName }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = ""
    )

    val stateFlowMoodArray: StateFlow<ArrayList<Mood>> = moodDataRepository.moodArray.map { array ->
        array.sortedByDescending { it.createdAt }.toCollection(ArrayList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = arrayListOf()
    )

    val stateFlowTodayMood: StateFlow<Mood> = moodDataRepository.moodArray.map { array ->
        array.find { it.isToday() } ?: Mood(Mood.MOODTYPE.NONE)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = Mood(Mood.MOODTYPE.NONE)
    )

    val stateFlowQuickActionAverages: StateFlow<ArrayList<QuickActionAverage>> = stateFlowMoodArray.map { moods ->
        val settings = settingsRepository.settings.first()
        val quickAction1 = settings.quickAction1
        val quickAction2 = settings.quickAction2
        val quickAction3 = settings.quickAction3

        val quickActionMoods = arrayListOf(quickAction1, quickAction2, quickAction3)
        val totalCount = moods.count()

        quickActionMoods.map { quickActionMood ->
            val moodCount = moods.count { it.mood == quickActionMood }
            val percentage = if (totalCount > 0) (moodCount.toFloat() / totalCount) * 100 else 0f
            QuickActionAverage(quickActionMood, percentage)
        }.toCollection(ArrayList())

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = arrayListOf(
            QuickActionAverage(Mood.MOODTYPE.FELIZ, 0f),
            QuickActionAverage(Mood.MOODTYPE.RELAXADO, 0f),
            QuickActionAverage(Mood.MOODTYPE.TRISTE, 0f)
        )
    )

    val stateFlowMoodAverages: StateFlow<ArrayList<QuickActionAverage>> = stateFlowMoodArray.map { moods ->
        val totalCount = moods.count()

        Mood.MOODTYPE.entries.map { quickActionMood ->
            val moodCount = moods.count { it.mood == quickActionMood }
            val percentage = if (totalCount > 0) (moodCount.toFloat() / totalCount) * 100 else 0f
            QuickActionAverage(quickActionMood, percentage)
        }.filter { it.mood != Mood.MOODTYPE.NONE }.toCollection(ArrayList())

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = arrayListOf(
            QuickActionAverage(Mood.MOODTYPE.FELIZ, 0f),
            QuickActionAverage(Mood.MOODTYPE.RELAXADO, 0f),
            QuickActionAverage(Mood.MOODTYPE.TRISTE, 0f)
        )
    )

    val stateFlowPositiveEmotions: StateFlow<Float> = stateFlowMoodArray.map { moods ->
        val totalCount = moods.count()
        val positiveCount = moods.count { it.mood.isPositive }

        if (totalCount != 0)
            (positiveCount * 100f) / totalCount
        else 0f
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0f
    )

    /*
    //init { debugAdd() }

    fun debugAdd() {
        fun daysAgoInMilli(timeInMilli: Long, days: Int): Long = timeInMilli - (days * 24 * 60 * 60 * 1000L)

        val numDays = Random.nextInt(0, 365)
        val numMood = Random.nextInt(0, Mood.MOODTYPE.entries.count() - 1)
        val numReason = Random.nextInt(0, Mood.REASON.entries.count() - 1)

        viewModelScope.launch {
            moodDataRepository.debugAddMood(
                Mood(
                    Mood.MOODTYPE.entries[numMood],
                    arrayListOf(Mood.REASON.entries[numReason]),
                    daysAgoInMilli(Calendar.getInstance().timeInMillis, numDays)
                )
            )
        }
    }*/

    fun removeMood(moodId: Long) {
        viewModelScope.launch { moodDataRepository.deleteMood(moodId) }
    }
}
