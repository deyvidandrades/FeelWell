package com.deyvidandrades.feelwell.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvidandrades.feelwell.ui.screens.main.MainScreen
import com.deyvidandrades.feelwell.ui.screens.main.MainScreenViewModel
import com.deyvidandrades.feelwell.ui.screens.mood.NewMoodScreen
import com.deyvidandrades.feelwell.ui.screens.mood.NewMoodViewModel
import com.deyvidandrades.feelwell.ui.screens.settings.SettingsScreen
import com.deyvidandrades.feelwell.ui.screens.settings.SettingsScreenViewModel
import com.deyvidandrades.feelwell.ui.screens.start.StartScreen
import com.deyvidandrades.feelwell.ui.screens.start.StartScreenViewModel

enum class SCREENS { LOADING, START, MAIN, ADD_MOOD, SETTINGS }

@Composable
fun ManualNavigation(
    manualNavigationViewModel: ManualNavigationViewModel,
    mainScreenViewModel: MainScreenViewModel,
    startScreenViewModel: StartScreenViewModel,
    settingsScreenViewModel: SettingsScreenViewModel,
    newMoodViewModel: NewMoodViewModel
) {
    val isFirstTime by manualNavigationViewModel.stateFlowIsFirstTime.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf(SCREENS.MAIN) }

    LaunchedEffect(isFirstTime) {
        if (isFirstTime) currentScreen = SCREENS.START
    }

    when (currentScreen) {
        SCREENS.LOADING -> Loading()

        SCREENS.START -> StartScreen(startScreenViewModel, onScreenChanged = { currentScreen = SCREENS.MAIN })

        SCREENS.MAIN -> MainScreen(mainScreenViewModel, onScreenChanged = { currentScreen = it })

        SCREENS.ADD_MOOD -> NewMoodScreen(newMoodViewModel, onBackPressed = { currentScreen = SCREENS.MAIN })

        SCREENS.SETTINGS -> SettingsScreen(settingsScreenViewModel, onBackPressed = { currentScreen = SCREENS.MAIN })
    }
}

@Composable
private fun Loading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
