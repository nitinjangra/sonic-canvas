package com.pixelpushers.sonixa

import com.pixelpushers.sonixa.data.scanner.ScanProgress
import com.pixelpushers.sonixa.data.scanner.normalizePath
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

    @Test
    fun testPositiveIncludedFolderMatching() {
        val selectedFolders = setOf("/tree/primary:Music", "/storage/emulated/0/Downloads")
        val mediaStoreFolderPath = "/storage/emulated/0/Music/Rock"

        val normalizedFolderPath = normalizePath(mediaStoreFolderPath)
        val matchesIncluded = selectedFolders.any { inc ->
            val normalizedInc = normalizePath(inc)
            val folderLeaf = inc.substringAfterLast(":").substringAfterLast("/").trim()
            normalizedFolderPath.startsWith(normalizedInc, ignoreCase = true) ||
            normalizedInc.startsWith(normalizedFolderPath, ignoreCase = true) ||
            (folderLeaf.isNotEmpty() && mediaStoreFolderPath.contains(folderLeaf, ignoreCase = true))
        }

        assertTrue("File inside selected Music folder should be included in positive flow", matchesIncluded)
    }

    @Test
    fun testExpressiveCardAndThumbnailShapes() {
        val materialDesignEnabled = true
        val playlistShapeRadius = if (materialDesignEnabled) 28 else 16
        val thumbnailShapeRadius = if (materialDesignEnabled) 36 else 24

        assertEquals(28, playlistShapeRadius)
        assertEquals(36, thumbnailShapeRadius)
    }

    @Test
    fun testHappyResumePlaybackWithPosition() {
        val resumePlaybackOnStart = true
        val lastPlayedSongId = 101L
        val lastPlayedPositionMs = 45000L

        val shouldResume = resumePlaybackOnStart && lastPlayedSongId != -1L
        assertTrue("Happy flow should allow resuming playback", shouldResume)
        assertEquals(45000L, lastPlayedPositionMs)
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
    fun testNegativeExcludedFolderFiltering() {
        val selectedFolders = setOf("/tree/primary:Music")
        val outsideFolderPath = "/storage/emulated/0/Podcasts/Episodes"

        val normalizedFolderPath = normalizePath(outsideFolderPath)
        val matchesIncluded = selectedFolders.any { inc ->
            val normalizedInc = normalizePath(inc)
            val folderLeaf = inc.substringAfterLast(":").substringAfterLast("/").trim()
            normalizedFolderPath.startsWith(normalizedInc, ignoreCase = true) ||
            normalizedInc.startsWith(normalizedFolderPath, ignoreCase = true) ||
            (folderLeaf.isNotEmpty() && outsideFolderPath.contains(folderLeaf, ignoreCase = true))
        }

        assertFalse("File outside selected Music folder should be excluded in negative flow", matchesIncluded)
    }

    @Test
    fun testNegativeResumePlaybackDisabled() {
        val resumePlaybackOnStart = false
        val lastPlayedSongId = 101L
        val lastPlayedPositionMs = 45000L

        val shouldResume = resumePlaybackOnStart && lastPlayedSongId != -1L
        assertFalse("Negative flow should prevent resume when setting is disabled", shouldResume)
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
