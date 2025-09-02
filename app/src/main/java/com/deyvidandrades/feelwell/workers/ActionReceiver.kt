package com.deyvidandrades.feelwell.workers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.repository.MoodDataRepositoryImpl
import com.deyvidandrades.feelwell.data.repository.SettingsRepositoryImpl
import com.deyvidandrades.feelwell.data.source.PreferencesDataStoreImpl
import com.deyvidandrades.feelwell.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val bundle = intent.extras

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(0)

            CoroutineScope(Dispatchers.IO).launch {
                val preferencesDataStore = PreferencesDataStoreImpl(context.dataStore)
                val moodDataRepository = MoodDataRepositoryImpl(preferencesDataStore)

                val settings = SettingsRepositoryImpl(preferencesDataStore).settings.first()

                when (bundle!!.getInt(context.getString(R.string.quick_action), 0)) {
                    1 -> moodDataRepository.addMood(settings.quickAction1, arrayListOf())
                    2 -> moodDataRepository.addMood(settings.quickAction2, arrayListOf())
                    3 -> moodDataRepository.addMood(settings.quickAction3, arrayListOf())
                }
            }
        }
    }
}