package com.example.sonixa

import com.example.sonixa.domain.model.LrcParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LrcParserRobustTest {

    @Test
    fun testParseValidSyncedLyrics() {
        val lrc = """
            [00:10.50] Hello world
            [01:15.100] High precision ms
        """.trimIndent()

        val result = LrcParser.parse(lrc)
        assertEquals(2, result.size)
        assertEquals(10500L, result[0].timestampMs)
        assertEquals("Hello world", result[0].text)
        assertEquals(75100L, result[1].timestampMs)
        assertEquals("High precision ms", result[1].text)
    }

    // --- Negative Test Flows ---

    @Test
    fun testParseEmptyOrBlankInputReturnsEmptyList() {
        val resultBlank = LrcParser.parse("   ")
        val resultEmpty = LrcParser.parse("")
        assertTrue(resultBlank.isEmpty())
        assertTrue(resultEmpty.isEmpty())
    }

    @Test
    fun testParseMalformedTimestampsReturnsEmptyList() {
        val malformedLrc = """
            [ab:cd] Invalid non-digit time format
            [xx:yy.zz] Non-numeric timestamps
            Plain text lyrics without any timestamps
        """.trimIndent()

        val result = LrcParser.parse(malformedLrc)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testParsePlaintextWithoutTimestampsReturnsEmptyList() {
        val plainText = "Just some un-timed lyrics text\nAnother line without brackets"
        val result = LrcParser.parse(plainText)
        assertTrue(result.isEmpty())
    }
}
