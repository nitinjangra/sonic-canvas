package com.pixelpushers.sonixa.ui.nowplaying

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pixelpushers.sonixa.data.local.entity.SongEntity
import com.pixelpushers.sonixa.ui.components.GlassPill
import com.pixelpushers.sonixa.ui.library.formatDuration
import com.pixelpushers.sonixa.ui.lyrics.LyricsUiState
import com.pixelpushers.sonixa.ui.nowplaying.RepeatMode as AppRepeatMode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NowPlayingScreen(
    queue: List<SongEntity>,
    currentIndex: Int,
    isPlaying: Boolean,
    currentPositionMs: Long,
    totalDurationMs: Long,
    isShuffleEnabled: Boolean,
    isRepeatEnabled: Boolean,
    repeatMode: AppRepeatMode = AppRepeatMode.OFF,
    isSleepTimerActive: Boolean,
    sleepTimerRemainingMs: Long,
    isEqualizerEnabled: Boolean = false,
    vibrationEnabled: Boolean = true,
    lyricsState: LyricsUiState,
    onSaveLyrics: (Long, String) -> Unit,
    onSeekToLyric: (Long) -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSkipToNext: () -> Unit,
    onSkipToPrevious: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onOpenAddToPlaylist: () -> Unit,
    onSongSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    onOpenEqualizer: () -> Unit,
    onOpenSleepTimer: () -> Unit,
    onOpenLyrics: () -> Unit,
    onOpenQueue: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (queue.isEmpty()) return

    var showAddLyricsDialog by remember { mutableStateOf(false) }
    var lyricsInputText by remember { mutableStateOf("") }
    var showLyricsInThumbnailArea by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(initialPage = currentIndex) { queue.size }

    BackHandler {
        onDismiss()
    }

    LaunchedEffect(currentIndex) {
        if (pagerState.currentPage != currentIndex) {
            pagerState.animateScrollToPage(currentIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentIndex) {
            onSongSelected(pagerState.currentPage)
        }
    }

    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .offset { IntOffset(0, offsetY.toInt().coerceAtLeast(0)) }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        offsetY = (offsetY + dragAmount).coerceAtLeast(0f)
                    },
                    onDragEnd = {
                        if (offsetY > 180f) {
                            onDismiss()
                        } else {
                            offsetY = 0f
                        }
                    }
                )
            }
            .clickable { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "NOW PLAYING",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thumbnail & Track Details Carousel
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val song = queue[page]
                var showMenu by remember { mutableStateOf(false) }

                val hasLyrics = lyricsState.songId == song.id && (
                    (lyricsState.isSynced && lyricsState.lyricsLines.isNotEmpty()) || lyricsState.plainText.isNotEmpty()
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Thumbnail area / Lyrics View area first
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showLyricsInThumbnailArea) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (hasLyrics) {
                                    val currentLineIndex = remember(currentPositionMs, lyricsState.lyricsLines) {
                                        if (!lyricsState.isSynced || lyricsState.lyricsLines.isEmpty()) -1
                                        else {
                                            val index = lyricsState.lyricsLines.indexOfLast { it.timestampMs <= currentPositionMs }
                                            if (index >= 0) index else 0
                                        }
                                    }

                                    val lyricsListState = rememberLazyListState()
                                    LaunchedEffect(currentLineIndex) {
                                        if (currentLineIndex >= 0 && currentLineIndex < lyricsState.lyricsLines.size) {
                                            lyricsListState.animateScrollToItem(currentLineIndex)
                                        }
                                    }

                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (lyricsState.isSynced && lyricsState.lyricsLines.isNotEmpty()) {
                                            Text(
                                                text = "Tap a line to jump to it",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )

                                            LazyColumn(
                                                state = lyricsListState,
                                                contentPadding = PaddingValues(vertical = 40.dp),
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                itemsIndexed(lyricsState.lyricsLines) { index, line ->
                                                    val isCurrent = index == currentLineIndex
                                                    val textColor by animateColorAsState(
                                                        targetValue = if (isCurrent) MaterialTheme.colorScheme.primary
                                                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                                        label = "pager_lyric_color"
                                                    )

                                                    Text(
                                                        text = line.text,
                                                        fontSize = if (isCurrent) 20.sp else 16.sp,
                                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                                        color = textColor,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable { onSeekToLyric(line.timestampMs) }
                                                            .padding(horizontal = 8.dp)
                                                    )
                                                }
                                            }
                                        } else if (lyricsState.plainText.isNotEmpty()) {
                                            LazyColumn(
                                                contentPadding = PaddingValues(vertical = 16.dp),
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                item {
                                                    Text(
                                                        text = lyricsState.plainText,
                                                        fontSize = 16.sp,
                                                        color = MaterialTheme.colorScheme.onBackground,
                                                        lineHeight = 24.sp,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(
                                            text = "No lyrics found",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = {
                                                lyricsInputText = ""
                                                showAddLyricsDialog = true
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                        ) {
                                            Text("Add Lyrics", color = Color.Black)
                                        }
                                    }
                                }
                            }
                        } else {
                            AsyncImage(
                                model = song.artworkUri,
                                fallback = painterResource(com.pixelpushers.sonixa.R.drawable.placeholder),
                                error = painterResource(com.pixelpushers.sonixa.R.drawable.placeholder),
                                contentDescription = song.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onTap = { onDismiss() },
                                            onDoubleTap = { onToggleFavorite() }
                                        )
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Song Title section just after the thumbnail area
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = song.title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${song.artist} • ${song.album}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onOpenAddToPlaylist) {
                                Icon(
                                    imageVector = Icons.Default.PlaylistAdd,
                                    contentDescription = "Add to playlist",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(onClick = onToggleFavorite) {
                                Icon(
                                    imageVector = if (song.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (song.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit / Add Lyrics") },
                                        onClick = {
                                            showMenu = false
                                            lyricsInputText = if (lyricsState.isSynced) {
                                                lyricsState.lyricsLines.joinToString("\n") { "[${formatDuration(it.timestampMs)}] ${it.text}" }
                                            } else {
                                                lyricsState.plainText
                                            }
                                            showAddLyricsDialog = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Full Screen Lyrics") },
                                        onClick = {
                                            showMenu = false
                                            onOpenLyrics()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val qualityText = if (song.isFlac) {
                        "FLAC · ${song.bitDepth}-bit / ${song.sampleRate / 1000} kHz"
                    } else {
                        "${song.mimeType.substringAfter("/").uppercase()} · 320 kbps"
                    }

                    Box(
                        modifier = Modifier
                    ) {
                        Text(
                            text = qualityText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Add / Edit Lyrics Dialog
            if (showAddLyricsDialog) {
                val currentSong = queue.getOrNull(pagerState.currentPage) ?: queue.firstOrNull()
                if (currentSong != null) {
                    AlertDialog(
                        onDismissRequest = { showAddLyricsDialog = false },
                        title = { Text("Edit / Add Lyrics") },
                        text = {
                            OutlinedTextField(
                                value = lyricsInputText,
                                onValueChange = { lyricsInputText = it },
                                placeholder = { Text("Paste .lrc format or plain text lyrics here...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        },
                        confirmButton = {
                            Button(onClick = {
                                onSaveLyrics(currentSong.id, lyricsInputText)
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

            Spacer(modifier = Modifier.height(12.dp))

            // Seek Bar
            val sliderValue = if (totalDurationMs > 0) {
                (currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
            } else 0f

            Slider(
                value = sliderValue,
                onValueChange = { ratio ->
                    onSeekTo((ratio * totalDurationMs).toLong())
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDuration(currentPositionMs),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDuration(totalDurationMs),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            val haptic = LocalHapticFeedback.current

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (vibrationEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggleShuffle()
                }) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = {
                    if (vibrationEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSkipToPrevious()
                }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            if (vibrationEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onTogglePlayPause()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = {
                    if (vibrationEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSkipToNext()
                }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = {
                    if (vibrationEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggleRepeat()
                }) {
                    val (repeatIcon, repeatTint, contentDesc) = when (repeatMode) {
                        AppRepeatMode.OFF -> Triple(Icons.Default.Repeat, MaterialTheme.colorScheme.onSurfaceVariant, "Repeat Off")
                        AppRepeatMode.ALL -> Triple(Icons.Default.Repeat, MaterialTheme.colorScheme.primary, "Repeat All")
                        AppRepeatMode.ONE -> Triple(Icons.Default.RepeatOne, MaterialTheme.colorScheme.primary, "Repeat 1")
                    }
                    Icon(
                        imageVector = repeatIcon,
                        contentDescription = contentDesc,
                        tint = repeatTint
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Glass Shortcut Pill
            GlassPill(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally),
                cornerRadius = 30.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShortcutItem(
                        icon = Icons.Default.Equalizer,
                        label = "EQ",
                        onClick = onOpenEqualizer,
                        isActive = isEqualizerEnabled,
                        activeSubLabel = "ON"
                    )
                    ShortcutItem(
                        icon = Icons.Default.Timer,
                        label = "Timer",
                        onClick = onOpenSleepTimer,
                        isActive = isSleepTimerActive,
                        activeSubLabel = if (sleepTimerRemainingMs == -1L) "End" else formatDuration(sleepTimerRemainingMs)
                    )
                    ShortcutItem(
                        icon = Icons.Default.MusicNote,
                        label = "Lyrics",
                        onClick = { showLyricsInThumbnailArea = !showLyricsInThumbnailArea },
                        isActive = showLyricsInThumbnailArea,
                        activeSubLabel = "ON"
                    )
                    ShortcutItem(
                        icon = Icons.AutoMirrored.Filled.QueueMusic,
                        label = "Queue",
                        onClick = onOpenQueue
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ShortcutItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    isActive: Boolean = false,
    activeSubLabel: String? = null
) {
    val isTimer = label.lowercase().contains("timer") || activeSubLabel?.contains(":") == true || activeSubLabel == "End"
    val isLyrics = label.lowercase().contains("lyrics")
    val isEq = label.lowercase().contains("eq")
    val infiniteTransition = rememberInfiniteTransition(label = "hourglass_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = isActive && activeSubLabel != null,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "shortcut_transition"
        ) { active ->
            if (active) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isLyrics) {
                        com.pixelpushers.sonixa.ui.components.LyricsIcon(
                            tint = MaterialTheme.colorScheme.primary,
                            isActive = active,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (isEq) {
                        com.pixelpushers.sonixa.ui.components.WaveformIcon(
                            color = MaterialTheme.colorScheme.primary,
                            playing = true,
                            modifier = Modifier.size(24.dp, 20.dp)
                        )
                    } else {
                        val activeIcon = if (isTimer) Icons.Default.HourglassEmpty else icon
                        val iconModifier = if (isTimer) Modifier.size(20.dp).graphicsLayer(rotationZ = rotation) else Modifier.size(20.dp)

                        Icon(
                            imageVector = activeIcon,
                            contentDescription = label,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = iconModifier
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = activeSubLabel.orEmpty(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isLyrics) {
                        com.pixelpushers.sonixa.ui.components.LyricsIcon(
                            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            isActive = isActive,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (isEq) {
                        com.pixelpushers.sonixa.ui.components.WaveformIcon(
                            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            playing = isActive,
                            modifier = Modifier.size(24.dp, 20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
