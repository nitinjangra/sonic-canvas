package com.example.sonixa

import com.example.sonixa.domain.model.LrcParser
import org.junit.Assert.assertEquals
import org.junit.Test

class LrcParserTest {

    @Test
    fun testParseSyncedLyrics() {
        val lrc = """
            [00:12.34] First line of song
            [01:05.50] Second line of song
        """.trimIndent()

        val parsed = LrcParser.parse(lrc)
        assertEquals(2, parsed.size)
        assertEquals(12340L, parsed[0].timestampMs)
        assertEquals("First line of song", parsed[0].text)
        assertEquals(65500L, parsed[1].timestampMs)
        assertEquals("Second line of song", parsed[1].text)
    }
}
