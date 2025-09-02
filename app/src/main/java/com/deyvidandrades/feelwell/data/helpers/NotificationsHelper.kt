package com.deyvidandrades.feelwell.data.helpers

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.deyvidandrades.feelwell.MainActivity
import com.deyvidandrades.feelwell.R
import com.deyvidandrades.feelwell.data.model.Mood
import com.deyvidandrades.feelwell.ui.theme.primaryLight
import com.deyvidandrades.feelwell.workers.ActionReceiver

object NotificationsHelper {
    private const val CHANNEL_ID = "feel_well_1"

    fun criarCanalDeNotificacoes(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), importance).apply {
            description = context.getString(R.string.canal_para_notificacoes_de_progresso)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun enviarNotificacao(
        context: Context,
        titulo: String,
        descricao: String,
        quickAction1: Mood.MOODTYPE,
        quickAction2: Mood.MOODTYPE,
        quickAction3: Mood.MOODTYPE,
    ) {
        val intentQuickAction1 = Intent(context, ActionReceiver::class.java)
            .putExtra(context.getString(R.string.quick_action), 1)

        val intentQuickAction2 = Intent(context, ActionReceiver::class.java)
            .putExtra(context.getString(R.string.quick_action), 2)

        val intentQuickAction3 = Intent(context, ActionReceiver::class.java)
            .putExtra(context.getString(R.string.quick_action), 3)

        val pendingIntentQuickAction1 = PendingIntent.getBroadcast(
            context,
            1,
            intentQuickAction1,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pendingIntentQuickAction2 = PendingIntent.getBroadcast(
            context,
            2,
            intentQuickAction2,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pendingIntentQuickAction3 = PendingIntent.getBroadcast(
            context,
            3,
            intentQuickAction3,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val actionQuickAction1: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.rounded_spa_24,
            "${quickAction1.emoji} ${quickAction1.name}",
            pendingIntentQuickAction1
        ).build()

        val actionQuickAction2: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.rounded_spa_24,
            "${quickAction2.emoji} ${quickAction2.name}",
            pendingIntentQuickAction2
        ).build()

        val actionQuickAction3: NotificationCompat.Action = NotificationCompat.Action.Builder(
            R.drawable.rounded_spa_24,
            "${quickAction3.emoji} ${quickAction3.name}",
            pendingIntentQuickAction3
        ).build()

        val intentAddMood = Intent(context, MainActivity::class.java)
        intentAddMood.putExtra("addNewMood", true)

        //CRIANDO A NOTIFICACAO
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setAutoCancel(true)
            .setColorized(true)
            .setShowWhen(true)
            .setColor(primaryLight.toArgb())
            .setCategory(Notification.CATEGORY_REMINDER)
            .setContentTitle(titulo)
            .setContentText(descricao)
            .setSmallIcon(R.drawable.rounded_spa_24)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    intentAddMood,
                    PendingIntent.FLAG_IMMUTABLE
                )
            )

        if (quickAction1 != Mood.MOODTYPE.NONE) builder.addAction(actionQuickAction1)
        if (quickAction2 != Mood.MOODTYPE.NONE) builder.addAction(actionQuickAction2)
        if (quickAction3 != Mood.MOODTYPE.NONE) builder.addAction(actionQuickAction3)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(0, builder.build())
        }
    }
}