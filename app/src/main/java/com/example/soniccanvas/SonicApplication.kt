package com.example.soniccanvas

import android.app.Application
import com.example.soniccanvas.data.repository.NotificationCategory
import com.example.soniccanvas.data.repository.NotificationRepository
import com.example.soniccanvas.util.LocalLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SonicApplication : Application() {

    @Inject
    lateinit var localLogger: LocalLogger

    @Inject
    lateinit var notificationRepository: NotificationRepository

    override fun onCreate() {
        super.onCreate()
        localLogger.log("App", "SonicCanvas Application created & local crash logger initialized.")

        if (localLogger.hasCrashedOnLastRun()) {
            val logPath = localLogger.getLogDirectoryPath()
            notificationRepository.addNotification(
                title = "⚠️ App Recovered from Previous Crash",
                message = "SonicCanvas encountered an unexpected error during the last session. Debug log stored at:\n$logPath/SonicCanvas_Debug_Log.txt",
                category = NotificationCategory.SYSTEM
            )
        }
    }
}
