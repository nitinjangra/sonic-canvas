package com.example.soniccanvas.domain.model

data class LyricsLine(
    val timestampMs: Long,
    val text: String
)

object LrcParser {
    // Regex matches LRC timestamps like [01:23.45] or [01:23.456]
    private val LRC_LINE_REGEX = Regex("""\[(\d{2}):(\d{2})(?:\.(\d{2,3}))?\](.*)""")

    fun parse(lrcText: String): List<LyricsLine> {
        if (lrcText.isBlank()) return emptyList()

        val lines = mutableListOf<LyricsLine>()

        lrcText.lines().forEach { line ->
            val trimmed = line.trim()
            val matches = LRC_LINE_REGEX.findAll(trimmed)

            var textContent = ""
            val timestamps = mutableListOf<Long>()

            for (match in matches) {
                val min = match.groupValues[1].toLongOrNull() ?: 0L
                val sec = match.groupValues[2].toLongOrNull() ?: 0L
                val msString = match.groupValues[3]
                val ms = when {
                    msString.length == 2 -> msString.toLongOrNull()?.times(10) ?: 0L
                    msString.length == 3 -> msString.toLongOrNull() ?: 0L
                    else -> 0L
                }

                val totalMs = (min * 60 * 1000) + (sec * 1000) + ms
                timestamps.add(totalMs)
                textContent = match.groupValues[4].trim()
            }

            timestamps.forEach { time ->
                lines.add(LyricsLine(timestampMs = time, text = textContent))
            }
        }

        return lines.sortedBy { it.timestampMs }
    }
}
