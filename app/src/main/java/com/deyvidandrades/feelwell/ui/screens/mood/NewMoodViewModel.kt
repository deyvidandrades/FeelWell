package com.deyvidandrades.feelwell.ui.screens.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.data.repository.MoodDataRepository
import kotlinx.coroutines.launch

class NewMoodViewModel( private val moodDataRepository: MoodDataRepository) :
    ViewModel() {

    fun addNewMood(mood: Mood.MOODTYPE, reasons: ArrayList<Mood.REASON>) {
        viewModelScope.launch { moodDataRepository.addMood(mood, reasons) }
    }
}