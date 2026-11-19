package com.example.sonixa.ui.lyrics

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sonixa.data.local.entity.SongEntity
import com.example.sonixa.domain.model.LyricsLine

@Composable
fun LyricsScreen(
    song: SongEntity?,
    uiState: LyricsUiState,
    currentPositionMs: Long,
    onSeekTo: (Long) -> Unit,
    onSaveLyrics: (Long, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (song == null) return

    var showAddLyricsDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    BackHandler {
        onBack()
    }

    val currentLineIndex = remember(currentPositionMs, uiState.lyricsLines) {
        if (!uiState.isSynced || uiState.lyricsLines.isEmpty()) -1
        else {
            val index = uiState.lyricsLines.indexOfLast { it.timestampMs <= currentPositionMs }
            if (index >= 0) index else 0
        }
    }

    LaunchedEffect(currentLineIndex) {
        if (currentLineIndex >= 0 && currentLineIndex < uiState.lyricsLines.size) {
            listState.animateScrollToItem(currentLineIndex)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = song.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = song.artist,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { showAddLyricsDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add lyrics",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chips: Synced / Plain
            if (uiState.isSynced || uiState.plainText.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    FilterChip(
                        selected = uiState.isSynced,
                        onClick = {},
                        label = { Text("Synced") }
                    )
                    FilterChip(
                        selected = !uiState.isSynced && uiState.plainText.isNotEmpty(),
                        onClick = {},
                        label = { Text("Plain") }
                    )
                }
            }

            // Hint
            if (uiState.isSynced && uiState.lyricsLines.isNotEmpty()) {
                Text(
                    text = "Tap a line to jump to it",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Main Lyrics View
            if (uiState.isSynced && uiState.lyricsLines.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(top = 40.dp, bottom = 160.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(uiState.lyricsLines) { index, line ->
                        val isCurrent = index == currentLineIndex

                        val textColor by animateColorAsState(
                            targetValue = if (isCurrent) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            label = "lyric_text_color"
                        )

                        Text(
                            text = line.text,
                            fontSize = if (isCurrent) 22.sp else 18.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSeekTo(line.timestampMs) }
                        )
                    }
                }
            } else if (uiState.plainText.isNotEmpty()) {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 160.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = uiState.plainText,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 28.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                EmptyLyricsState(
                    onAddLrcFile = { showAddLyricsDialog = true },
                    onPasteLyrics = { showAddLyricsDialog = true }
                )
            }
        }

        // Add/Paste Lyrics Dialog
        if (showAddLyricsDialog) {
            AlertDialog(
                onDismissRequest = { showAddLyricsDialog = false },
                title = { Text("Add / Paste Lyrics") },
                text = {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Paste .lrc format or plain text lyrics here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        onSaveLyrics(song.id, inputText)
                        showAddLyricsDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddLyricsDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun EmptyLyricsIllustration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCC5A46))
            )
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CD19C))
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB174F5))
            )
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3892A6))
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8C7338))
            )
        }

        // Floating Microphone Icon Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-48).dp, y = (-8).dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Microphone",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun EmptyLyricsState(
    onAddLrcFile: () -> Unit,
    onPasteLyrics: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        EmptyLyricsIllustration()

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "No lyrics yet. Sing along anyway!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Add a .lrc file or paste the words, and they'll light up in time with the music.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onAddLrcFile,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Add .lrc file",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onPasteLyrics,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Paste lyrics",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
