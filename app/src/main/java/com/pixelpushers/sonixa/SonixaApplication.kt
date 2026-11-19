package com.pixelpushers.sonixa

import android.app.Application
import com.pixelpushers.sonixa.data.repository.NotificationCategory
import com.pixelpushers.sonixa.data.repository.NotificationRepository
import com.pixelpushers.sonixa.util.LocalLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SonixaApplication : Application() {

    @Inject
    lateinit var localLogger: LocalLogger

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun onCreate() {
        super.onCreate()
        localLogger.log("App", "Sonixa Application created & local crash logger initialized.")

        if (localLogger.hasCrashedOnLastRun()) {
            val logPath = localLogger.getLogDirectoryPath()
            notificationRepository.addNotification(
                title = "⚠️ App Recovered from Previous Crash",
                message = "Sonixa encountered an unexpected error during the last session. Debug log stored at:\n$logPath/Sonixa_Debug_Log.txt",
                category = NotificationCategory.SYSTEM
            )
        }
    }
}
