package com.deyvidandrades.feelwell.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.helpers.NotificationsHelper
import com.deyvidandrades.feelwell.data.helpers.WorkManagerHelper
import com.deyvidandrades.feelwell.data.repository.SettingsRepositoryImpl
import com.deyvidandrades.feelwell.data.source.PreferencesDataStoreImpl
import com.deyvidandrades.feelwell.dataStore
import kotlinx.coroutines.flow.first

class NotificationsWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            val preferencesDataStore = PreferencesDataStoreImpl(applicationContext.dataStore)
            val settings = SettingsRepositoryImpl(preferencesDataStore).settings.first()

            WorkManagerHelper.rescheduleWorker(applicationContext, settings.notificationTime)

            if (settings.notifications) {
                NotificationsHelper.enviarNotificacao(
                    applicationContext,
                    context.getString(R.string.hora_de_registrar_seu_mood),
                    context.getString(R.string.como_foi_seu_dia),
                    settings.quickAction1,
                    settings.quickAction2,
                    settings.quickAction3
                )
            }
            return Result.success()
        } catch (_: Exception) {
            return Result.failure()
        }
    }
}