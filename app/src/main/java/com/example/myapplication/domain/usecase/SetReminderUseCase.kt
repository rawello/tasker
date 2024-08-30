package com.example.myapplication.domain.usecase

import android.app.Application
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.example.myapplication.domain.model.Note
import com.example.myapplication.utils.NotificationWorker
import java.util.concurrent.TimeUnit

class SetReminderUseCase(private val application: Application) {
    operator fun invoke(note: Note, timeInMillis: Long) {
        val delay = timeInMillis - System.currentTimeMillis()

        if (delay > 0) {
            val data = workDataOf(
                "note_id" to note.id,
                "note_text" to note.textNote
            )

            val workRequest: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(note.id.toString())
                .build()

            WorkManager.getInstance(application).enqueue(workRequest)
        }
    }
}