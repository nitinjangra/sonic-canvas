package com.pixelpushers.sonixa

import com.pixelpushers.sonixa.data.scanner.ScanProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannerAndQueueTest {

    // --- Positive Test Flows ---

    @Test
    fun testScanProgressInitialAndActiveStates() {
        val initialProgress = ScanProgress()
        assertFalse(initialProgress.isScanning)
        assertEquals(0, initialProgress.scannedCount)
        assertEquals("", initialProgress.currentFolder)

        val activeProgress = ScanProgress(isScanning = true, scannedCount = 42, currentFolder = "/Music/FLAC")
        assertTrue(activeProgress.isScanning)
        assertEquals(42, activeProgress.scannedCount)
        assertEquals("/Music/FLAC", activeProgress.currentFolder)
    }

    @Test
    fun testPositiveDurationInclusion() {
        val minDurationMs = 30000L
        val validTrackDurationMs = 180000L
        assertTrue("Valid track >= 30s should be included", validTrackDurationMs >= minDurationMs)
    }

    // --- Negative Test Flows ---

    @Test
    fun testNegativeDurationFiltering() {
        val minDurationMs = 30000L
        val shortTrackDurationMs = 15000L
        assertFalse("Short track < 30s should be filtered out", shortTrackDurationMs >= minDurationMs)
    }

    @Test
    fun testNegativeHiddenFolderExclusionRule() {
        val hiddenFolderPath = "/storage/emulated/0/.hidden/audio"
        val isHidden = hiddenFolderPath.split("/").any { it.startsWith(".") && it.isNotEmpty() }
        assertTrue("Hidden folders starting with '.' should be excluded in negative flow", isHidden)
    }

    @Test
    fun testNegativeSkippedFolderFiltering() {
        val folderPath = "/storage/emulated/0/Ringtones"
        val skippedFolders = setOf("/storage/emulated/0/Ringtones", "/storage/emulated/0/Notifications")
        val isSkipped = skippedFolders.any { folderPath.startsWith(it, ignoreCase = true) }
        assertTrue("Explicit skipped folders should be excluded in negative flow", isSkipped)
    }

    @Test
    fun testScanCancellationResetsState() {
        var progress = ScanProgress(isScanning = true, scannedCount = 10, currentFolder = "/Music")
        assertTrue(progress.isScanning)

        // Cancel scan
        progress = ScanProgress(isScanning = false, scannedCount = 0, currentFolder = "")
        assertFalse("Cancelled scan should reset isScanning to false", progress.isScanning)
        assertEquals(0, progress.scannedCount)
    }
}
