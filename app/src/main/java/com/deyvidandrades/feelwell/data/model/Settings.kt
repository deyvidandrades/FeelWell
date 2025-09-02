package com.deyvidandrades.feelwell.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    var userName: String,
    var notifications: Boolean,
    var notificationTime: Int,
    var isDarkTheme: Boolean,
    var isFirstTime: Boolean,
    var quickAction1: Mood.MOODTYPE = Mood.MOODTYPE.FELIZ,
    var quickAction2: Mood.MOODTYPE = Mood.MOODTYPE.CALMO,
    var quickAction3: Mood.MOODTYPE = Mood.MOODTYPE.TRISTE
)
