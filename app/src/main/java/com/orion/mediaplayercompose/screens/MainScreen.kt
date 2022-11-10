package com.orion.mediaplayercompose.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orion.mediaplayercompose.R
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.ui.theme.ThirdColor
import com.orion.mediaplayercompose.utils.PlaybackMode
import com.orion.mediaplayercompose.utils.SortingType
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.utils.toMinutesAndSeconds
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel


@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainScreen(viewModel: PlayerViewModel, onNavigateToPlaylists: () -> Unit) {

    if (viewModel.allSongs.value?.isNotEmpty() == true) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Card(modifier = Modifier.height(70.dp)) {
                UpperContainer(onNavigateToPlaylists = onNavigateToPlaylists, viewModel)
            }

            Card(modifier = Modifier.weight(1f)) {

                ListSongs(viewModel)
            }

            Card(modifier = Modifier.height(140.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DawnContainer(viewModel = viewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpperContainer(onNavigateToPlaylists: () -> Unit, viewModel: PlayerViewModel) {
    Row(modifier = Modifier.height(60.dp)) {
        PlaybackOrderMenu(viewModel)
        var text by remember { mutableStateOf(TextFieldValue("")) }

        OutlinedTextField(
            value = text,
            modifier = Modifier
                .height(55.dp)
                .padding(horizontal = 8.dp)
                .weight(1f)
                .align(alignment = Alignment.CenterVertically),
            maxLines = 1,
            singleLine = true,
            leadingIcon = {
                Icon(
                    painterResource(id = R.drawable.ic_find),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "emailIcon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            onValueChange = { text = it },
            label = {
                Text(
                    text = stringResource(R.string.find_song),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            shape = RoundedCornerShape(25.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 12.sp)
        )

        IconButton(
            onClick = onNavigateToPlaylists,
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp)
                .requiredSize(40.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_playlist),
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ListSongs(viewModel: PlayerViewModel) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val songs: List<Song> by viewModel.allSongs.observeAsState(mutableListOf())
    val playlists = viewModel.allPlaylists.observeAsState(mapOf())
    var state = viewModel.currentTabPosition.observeAsState(0).value
    val titles =
        mutableListOf(stringResource(R.string.all_songs), stringResource(R.string.playlist))

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        TabRow(selectedTabIndex = state!!, modifier = Modifier.padding(bottom = 9.dp)) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = viewModel.currentTabPosition.value == index,
                    onClick = {
                        state = index
                        viewModel.currentTabPosition.value = index
                    },
                    text = {
                        Text(
                            text = if (index == 1) {
                                if (viewModel.currentPlaylist.value != null) "$title: ${viewModel.currentPlaylist.value!!.name}" else "$title : ${
                                    stringResource(
                                        R.string.not_selected
                                    )
                                }"
                            } else title,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        if (state == 0) {
            AllSongsList(
                listState = listState,
                songs = songs,
                viewModel = viewModel
            )
        } else if (state == 1 && viewModel.currentPlaylist.value != null) {
            SongsFromCurrentPlaylist(
                listState = listState,
                playlists = playlists,
                viewModel = viewModel
            )
        }
        snappyLazyColumn(listState = listState, coroutineScope = coroutineScope)
    }
}

@Composable
fun AllSongsList(
    listState: LazyListState,
    songs: List<Song>,
    viewModel: PlayerViewModel
) {
    LazyColumn(state = listState) {
        items(songs) { song ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillParentMaxHeight(1.div(8f))
            ) {

                IconButton(
                    onClick = {
                        viewModel.getMediaPlayer().start(song)
                    },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .requiredSize(50.dp),
                ) {
                    Icon(
                        painterResource(id = if (!song.isPlayed) R.drawable.ic_play else R.drawable.ic_pause),
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .height(40.dp)
                        .align(alignment = Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally

                )
                {
                    Text(
                        text = song.title,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_semi_bold))

                    )
                    Text(
                        text = song.artist,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                    )
                }
                SongMenu()
            }
        }
    }
}


@Composable
fun SongsFromCurrentPlaylist(
    listState: LazyListState,
    playlists: State<Map<Playlist, MutableList<Song>>>,
    viewModel: PlayerViewModel
) {
    LazyColumn(state = listState) {
        items(playlists.value[viewModel.currentPlaylist.value]!!.toList()) { song ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillParentMaxHeight(1.div(8f))
            ) {

                IconButton(
                    onClick = {
                        viewModel.getMediaPlayer().start(song)
                    },
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .requiredSize(50.dp),
                ) {
                    Icon(
                        painterResource(id = if (!song.isPlayed) R.drawable.ic_play else R.drawable.ic_pause),
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .height(40.dp)
                        .align(alignment = Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally

                )
                {
                    Text(
                        text = song.title,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_semi_bold))

                    )
                    Text(
                        text = song.artist,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                    )

                }
                SongMenu()
            }
        }
    }
}


@Composable
fun DawnContainer(viewModel: PlayerViewModel) {
    val playbackMode = viewModel.playbackMode.observeAsState(PlaybackMode.LOOP)
    val isPlayed = viewModel.isMediaPlayerPlayed.observeAsState(false).value
    val currentSong = viewModel.currentSong.observeAsState().value
    var sliderPosition = viewModel.sliderThumbPosition.observeAsState(0f).value
    val currentPositionText = viewModel.currentPosition.observeAsState().value
    val tempSliderPosition = remember { mutableStateOf(sliderPosition) }

    var isTouch = false

    Text(
        text = currentSong?.title ?: "MUSIC SONG ",
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp),
        color = MaterialTheme.colorScheme.primary,
        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_semi_bold))
    )

    Slider(
        value = if(!isTouch) sliderPosition else return,
        onValueChange = {
            isTouch = true
            viewModel.sliderThumbPosition.value = it
            tempSliderPosition.value = it
        },
        onValueChangeFinished = {
            viewModel.getMediaPlayer().setCurrentPosition((tempSliderPosition.value * viewModel.currentSong.value!!.duration).toLong())
            sliderPosition = tempSliderPosition.value
            isTouch = false
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(20.dp),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = ThirdColor,
            inactiveTickColor = ThirdColor,
            activeTickColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(4.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = currentPositionText ?: "00:00",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = if (viewModel.currentSong.value != null) viewModel.currentSong.value!!.duration.toMinutesAndSeconds() else "00:00",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }

    Row {

        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_to_start
                ),
                contentDescription = "To Start",
                modifier = Modifier.requiredSize(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {
                viewModel.getMediaPlayer().previous()
            }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_previous),
                contentDescription = "Preview",
                modifier = Modifier.requiredSize(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {
                viewModel.currentSong.value?.let { viewModel.getMediaPlayer().start(it) }
            },
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Icon(
                painterResource(id = if (!isPlayed) R.drawable.ic_play else R.drawable.ic_pause),
                contentDescription = "Play",
                modifier = Modifier.requiredSize(50.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {
                viewModel.getMediaPlayer().next()
            }
        ) {
            Icon(
                painterResource(id = R.drawable.ic_next),
                contentDescription = "Next",
                modifier = Modifier.requiredSize(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = { viewModel.getStateModeChanger().changeStateMode() },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                painterResource(
                    id = when (playbackMode.value) {
                        PlaybackMode.LOOP -> R.drawable.ic_loop
                        PlaybackMode.REPEAT -> R.drawable.ic_repeat_one
                        else -> R.drawable.ic_random
                    }
                ),
                contentDescription = "Change play mode",
                modifier = Modifier.requiredSize(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SongMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }, modifier = Modifier.size(48.dp)) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Показать меню",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Text(
                "Скопировать",  //TODO
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
            Text(
                "Вставить",     //TODO
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
            Divider()
            Text(
                "Настройки",    //TODO
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun PlaybackOrderMenu(viewModel: PlayerViewModel) {
    var playbackOrder by remember { mutableStateOf(false) }
    Box {
        IconButton(
            onClick = { playbackOrder = true },
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp)
                .requiredSize(40.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.ic_sort),
                contentDescription = "Sorting",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        DropdownMenu(
            expanded = playbackOrder,
            onDismissRequest = { playbackOrder = false }
        ) {

            Text(
                stringResource(R.string.sort_by),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.primary

            )
            Divider(color = MaterialTheme.colorScheme.primary)

            Text(
                stringResource(R.string.date_of_addition),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.getSongSorter().changeSortingType(SortingType.DATE)
                        playbackOrder = false
                    }),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                stringResource(R.string.rating),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.getSongSorter().changeSortingType(SortingType.RATING)
                        playbackOrder = false
                    }),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                stringResource(R.string.listening_frequency),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.getSongSorter().changeSortingType(SortingType.REPEATABILITY)
                        playbackOrder = false
                    }),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
