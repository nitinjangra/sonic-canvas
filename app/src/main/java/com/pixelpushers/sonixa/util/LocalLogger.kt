package com.pixelpushers.sonixa.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalLogger @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val logFile: File by lazy {
        File(context.filesDir, "Sonixa_Debug_Log.txt")
    }

    private val crashFlagFile: File by lazy {
        File(context.filesDir, "app_crashed_flag.txt")
    }

    init {
        setupUncaughtExceptionHandler()
    }

    private fun setupUncaughtExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val logMessage = "\n--- CRASH at $timestamp ---\nThread: ${thread.name}\nMessage: ${throwable.message}\nStacktrace:\n${throwable.stackTraceToString()}\n"
            try {
                logFile.appendText(logMessage)
                crashFlagFile.writeText(timestamp)
            } catch (_: Exception) {}
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    fun hasCrashedOnLastRun(): Boolean {
        return try {
            if (crashFlagFile.exists()) {
                crashFlagFile.delete()
                true
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun getLogDirectoryPath(): String {
        return context.filesDir.absolutePath
    }

    fun log(tag: String, message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val entry = "[$timestamp] [$tag]: $message\n"
        try {
            logFile.appendText(entry)
            if (logFile.length() > 500_000) {
                val lines = logFile.readLines()
                if (lines.size > 200) {
                    logFile.writeText(lines.takeLast(100).joinToString("\n") + "\n")
                }
            }
        } catch (_: Exception) {}
    }

    fun getLogs(): String {
        return try {
            if (logFile.exists()) logFile.readText() else "No logs recorded yet."
        } catch (e: Exception) {
            "Error reading logs: ${e.message}"
        }
    }

    fun clearLogs() {
        try {
            if (logFile.exists()) logFile.delete()
        } catch (_: Exception) {}
    }
}
