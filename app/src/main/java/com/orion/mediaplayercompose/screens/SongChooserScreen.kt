package com.orion.mediaplayercompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orion.mediaplayercompose.R
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SongChooserScreen(
    viewModel: PlayerViewModel,
    onNavigateToPlaylists: () -> Unit
) {
    val songs: List<Song> by viewModel.allSongs.observeAsState(mutableListOf())
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val isChooses = viewModel.isChosenSongListFromUi.value!!.toMutableStateList()

    if (songs.isNotEmpty()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Card(modifier = Modifier.weight(1f)) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = viewModel.playlistName.value!!,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Divider(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ListSongs(
                        songs = songs,
                        isChooses = isChooses,
                        viewModel = viewModel,
                        listState = listState
                    )

                    snappyLazyColumn(listState = listState, coroutineScope = coroutineScope)
                }
            }

            Card(modifier = Modifier.height(140.dp)) {
                BottomContainer(
                    viewModel = viewModel,
                    isChooses = isChooses,
                    onNavigateToPlaylists = onNavigateToPlaylists
                )
            }
        }
    }

}

@Composable
fun BottomContainer(
    viewModel: PlayerViewModel,
    isChooses: MutableList<Boolean>,
    onNavigateToPlaylists: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)

    ) {
        Text(
            text = stringResource(R.string.save_playlist).uppercase(),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light)),
            color = MaterialTheme.colorScheme.primary
        )
        Divider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Button(onClick = onNavigateToPlaylists) {
                Text(
                    text = stringResource(R.string.back).uppercase(),
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                )
            }

            Button(
                onClick = {
                    if (isChooses.any { it }) viewModel.getPlaylistController().createPlaylist(isChooses)
                    viewModel.getPlaylistController().savePlaylist()
                    viewModel.isChosenSongListFromUi.value = isChooses
                    val value = viewModel.currentPlaylist.value
                    viewModel.currentPlaylist.value = null
                    viewModel.currentPlaylist.value = value
                    onNavigateToPlaylists.invoke()
                },
                enabled = isChooses.any { it }

            ) {
                Text(
                    text = stringResource(R.string.save).uppercase(),
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                )
            }
        }
    }
}

@Composable
fun ListSongs(songs: List<Song>, isChooses: MutableList<Boolean>, viewModel: PlayerViewModel, listState: LazyListState) {

    LazyColumn(state = listState, modifier = Modifier.padding(vertical = 8.dp)) {
        itemsIndexed(songs) { index: Int, song ->

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillParentMaxHeight(1.div(9f))
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
                Checkbox(
                    checked = when {
                        isChooses.isNotEmpty() -> isChooses[index]
                        else -> false
                    },
                    onCheckedChange = {
                        isChooses[index] = it
                        viewModel.isChosenSongListFromUi.value!![index] = it
                    },
                    colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}



