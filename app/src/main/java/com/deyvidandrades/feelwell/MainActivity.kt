package com.deyvidandrades.feelwell

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deyvidandrades.feelwell.data.helpers.NotificationsHelper
import com.deyvidandrades.feelwell.data.helpers.WorkManagerHelper
import com.deyvidandrades.feelwell.data.repository.MoodDataRepositoryImpl
import com.deyvidandrades.feelwell.data.repository.SettingsRepositoryImpl
import com.deyvidandrades.feelwell.data.source.PreferencesDataStoreImpl
import com.deyvidandrades.feelwell.ui.navigation.ManualNavigation
import com.deyvidandrades.feelwell.ui.navigation.ManualNavigationViewModel
import com.deyvidandrades.feelwell.ui.screens.main.MainScreenViewModel
import com.deyvidandrades.feelwell.ui.screens.mood.NewMoodViewModel
import com.deyvidandrades.feelwell.ui.screens.settings.SettingsScreenViewModel
import com.deyvidandrades.feelwell.ui.screens.start.StartScreenViewModel
import com.deyvidandrades.feelwell.ui.theme.FeelWellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //DataStore
        val preferencesDataStore = PreferencesDataStoreImpl(dataStore)

        //Repositories
        val moodDataRepository = MoodDataRepositoryImpl(preferencesDataStore)
        val settingsRepository = SettingsRepositoryImpl(preferencesDataStore)

        //ViewModels
        val manualNavigationViewModel = ManualNavigationViewModel(settingsRepository)
        val mainScreenViewModel = MainScreenViewModel(moodDataRepository, settingsRepository)
        val startScreenViewModel = StartScreenViewModel(settingsRepository)
        val settingsScreenViewModel = SettingsScreenViewModel(settingsRepository)
        val newMoodViewModel = NewMoodViewModel(settingsRepository, moodDataRepository)

        setContent {
            val settings by settingsScreenViewModel.stateFlowSettings.collectAsStateWithLifecycle()

            FeelWellTheme(darkTheme = settings.isDarkTheme) {
                var hasNotificationPermission by remember { mutableStateOf(true) }

                ApplyStatusBarStyle(settings.isDarkTheme)

                LaunchedEffect(hasNotificationPermission) {
                    if (hasNotificationPermission) {
                        NotificationsHelper.criarCanalDeNotificacoes(applicationContext)
                        WorkManagerHelper.initWorker(applicationContext, settings.notificationTime)
                    } else {
                        WorkManagerHelper.stopWorker(applicationContext)
                    }
                }

                if (settings.notifications)
                    NotificationPermissionRequester(onPermissionRequested = { hasNotificationPermission = it })

                Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
                    ManualNavigation(
                        manualNavigationViewModel,
                        mainScreenViewModel,
                        startScreenViewModel,
                        settingsScreenViewModel,
                        newMoodViewModel
                    )
                }
            }
        }
    }

    @Composable
    fun ApplyStatusBarStyle(isDarkTheme: Boolean) {
        val view = LocalView.current
        val activity = LocalActivity.current as Activity

        SideEffect {
            val window = activity.window
            WindowCompat.setDecorFitsSystemWindows(window, false)

            val insetsController = WindowInsetsControllerCompat(window, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }

    @Composable
    fun NotificationPermissionRequester(onPermissionRequested: (Boolean) -> Unit) {
        val context = LocalContext.current
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted -> onPermissionRequested.invoke(isGranted) }

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

