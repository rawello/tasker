package com.example.myapplication.utils

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.R

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    @SuppressLint("ServiceCast")
    override fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val noteText = inputData.getString("note_text")

        val notificationBuilder = NotificationCompat.Builder(applicationContext, "note_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Напоминание")
            .setContentText("Ваша заметка \"$noteText\" готова!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, notificationBuilder.build())
        return Result.success()
    }
}