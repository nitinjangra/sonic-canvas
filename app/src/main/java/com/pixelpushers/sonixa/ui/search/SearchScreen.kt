package com.pixelpushers.sonixa.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pixelpushers.sonixa.data.local.entity.PlaylistEntity
import com.pixelpushers.sonixa.data.local.entity.SongEntity
import com.pixelpushers.sonixa.ui.library.formatDuration
import com.pixelpushers.sonixa.ui.playlists.AddToPlaylistSheet
import com.pixelpushers.sonixa.ui.theme.SonixaTheme
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    query: String,
    searchResults: List<SongEntity>,
    currentPlayingSongId: Long?,
    playlists: List<PlaylistEntity>,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSongClick: (SongEntity) -> Unit,
    onAddToQueue: (SongEntity) -> Unit,
    onTogglePlaylist: (playlistId: Long, songId: Long) -> Unit,
    onCreatePlaylist: (String) -> Unit,
    onDeleteSong: (SongEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var userInteractedWithResults by remember { mutableStateOf(false) }
    var selectedSongForDetails by remember { mutableStateOf<SongEntity?>(null) }
    var selectedSongForDelete by remember { mutableStateOf<SongEntity?>(null) }
    var selectedSongForPlaylist by remember { mutableStateOf<SongEntity?>(null) }

    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 30 }
    }

    val imeBottomPadding = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val isImeVisible = imeBottomPadding > 0.dp
    val hasNowPlayingPill = currentPlayingSongId != null
    val searchBarBottomPadding = when {
        isImeVisible -> 16.dp
        hasNowPlayingPill -> 168.dp
        else -> 84.dp
    }

    val showSearchBar = !isScrolled && !userInteractedWithResults

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Search Library",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (query.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Type to search your music library",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
            } else if (searchResults.isEmpty()) {
                NoSearchResultsState(
                    query = query,
                    onClearSearch = onClearQuery
                )
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(bottom = 160.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(searchResults, key = { it.id }) { song ->
                        SearchResultRowItem(
                            song = song,
                            isPlaying = song.id == currentPlayingSongId,
                            onClick = {
                                userInteractedWithResults = true
                                onSongClick(song)
                            },
                            onShowDetails = { selectedSongForDetails = song },
                            onAddToPlaylist = { selectedSongForPlaylist = song },
                            onAddToQueue = {
                                onAddToQueue(song)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Added \"${song.title}\" to Now Playing list")
                                }
                            },
                            onDelete = { selectedSongForDelete = song }
                        )
                    }
                }
            }
        }

        // Search Bar pinned just above keyboard, hidden when scrolled/interacted with results
        AnimatedVisibility(
            visible = showSearchBar,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = searchBarBottomPadding)
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        userInteractedWithResults = false
                        onQueryChange(it)
                    },
                    placeholder = { Text("Songs, artists, albums...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Center aligned Top Anchor Button with filled up arrow icon when search bar is hidden
        AnimatedVisibility(
            visible = !showSearchBar && (isScrolled || userInteractedWithResults),
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
                .padding(bottom = searchBarBottomPadding)
        ) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                        userInteractedWithResults = false
                    }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    contentDescription = "Scroll to top",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 150.dp)
        )

        // Song Details Dialog
        selectedSongForDetails?.let { song ->
            SongDetailsDialog(
                song = song,
                onDismiss = { selectedSongForDetails = null }
            )
        }

        // Add to Playlist Sheet
        selectedSongForPlaylist?.let { song ->
            AddToPlaylistSheet(
                playlists = playlists,
                onTogglePlaylist = { playlistId, isChecked ->
                    if (isChecked) {
                        onTogglePlaylist(playlistId, song.id)
                    }
                },
                onCreateNewPlaylist = onCreatePlaylist,
                onDismiss = { selectedSongForPlaylist = null }
            )
        }

        // Delete Confirmation Dialog
        selectedSongForDelete?.let { song ->
            DeleteSongDialog(
                song = song,
                onConfirm = {
                    onDeleteSong(song)
                    selectedSongForDelete = null
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Deleted \"${song.title}\"")
                    }
                },
                onDismiss = { selectedSongForDelete = null }
            )
        }
    }
}

@Composable
fun SearchResultRowItem(
    song: SongEntity,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onShowDetails: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onAddToQueue: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = song.artworkUri,
            contentDescription = song.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = song.artist,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Standardized Right Aligned Audio Quality Pills + Duration
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            val bitDepthText = "${if (song.bitDepth > 0) song.bitDepth else 16}bit"

            if (song.isFlac) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "FLAC",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))
            }

            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = bitDepthText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = formatDuration(song.duration),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.widthIn(min = 36.dp),
                textAlign = TextAlign.End
            )
        }

        Box {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Song options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Details") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    onClick = {
                        showMenu = false
                        onShowDetails()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Add to playlist") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = null) },
                    onClick = {
                        showMenu = false
                        onAddToPlaylist()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Add to now playing list") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = null) },
                    onClick = {
                        showMenu = false
                        onAddToQueue()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    onClick = {
                        showMenu = false
                        onDelete()
                    }
                )
            }
        }
    }
}

@Composable
fun SongDetailsDialog(
    song: SongEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Details",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DetailRow(label = "Title", value = song.title)
                DetailRow(label = "Artist", value = song.artist)
                DetailRow(label = "Album", value = song.album)
                DetailRow(label = "Duration", value = formatDuration(song.duration))
                DetailRow(label = "Format", value = if (song.isFlac) "FLAC (Lossless Audio)" else "Standard Audio")
                DetailRow(label = "Location", value = song.path.ifEmpty { "Internal Storage" })
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DeleteSongDialog(
    song: SongEntity,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Song",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete \"${song.title}\" from your library?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun NoSearchResultsState(
    query: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
        ) {
            // Magnifying Glass with Question Mark Illustration
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                // Floating dots
                Text(
                    text = "•",
                    color = Color(0xFFF2783F),
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 28.dp, y = 20.dp)
                )
                Text(
                    text = "•",
                    color = Color(0xFF6380F9),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-30).dp, y = 32.dp)
                )
                Text(
                    text = "•",
                    color = Color(0xFFF6C445),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 32.dp, y = (-20).dp)
                )
                Text(
                    text = "•",
                    color = Color(0xFFF06CA6),
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-24).dp, y = (-12).dp)
                )

                // Magnifying Lens Circle with Question Mark
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF6C445)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hmm, nothing there!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We couldn't find \"$query\". Check the spelling or try another word.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedButton(
                onClick = onClearSearch,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Clear search",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SonixaTheme {
        SearchScreen(
            query = "Sonic",
            searchResults = listOf(
                SongEntity(
                    id = 1L,
                    mediaStoreId = 101L,
                    title = "Midnight Resonance",
                    artist = "Sonic Symphony",
                    album = "Canvas Vol. 1",
                    albumId = 201L,
                    duration = 210000L,
                    path = "Music/MidnightResonance.flac",
                    uri = "content://media/external/audio/media/101",
                    artworkUri = null,
                    mimeType = "audio/flac",
                    isFlac = true,
                    folderPath = "Music"
                )
            ),
            currentPlayingSongId = 1L,
            playlists = emptyList(),
            onQueryChange = {},
            onClearQuery = {},
            onSongClick = {},
            onAddToQueue = {},
            onTogglePlaylist = { _, _ -> },
            onCreatePlaylist = {},
            onDeleteSong = {}
        )
    }
}
