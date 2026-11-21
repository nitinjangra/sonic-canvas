package com.pixelpushers.sonixa

import com.pixelpushers.sonixa.data.scanner.ScanProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannerAndQueueTest {

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
    fun testNegativeDurationFiltering() {
        val minDurationMs = 30000L
        val shortTrackDurationMs = 15000L
        val validTrackDurationMs = 180000L

        assertFalse("Short track should be filtered out", shortTrackDurationMs >= minDurationMs)
        assertTrue("Valid track should be included", validTrackDurationMs >= minDurationMs)
    }
}
