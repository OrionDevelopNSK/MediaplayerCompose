package com.orion.mediaplayercompose.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.orion.mediaplayercompose.R
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    viewModel: PlayerViewModel,
    onNavigateToSongChooser: () -> Unit,
    onNavigateToMainScreen: () -> Unit
) {

    val playlists: State<Map<Playlist, MutableList<Song>>> =
        viewModel.allPlaylists.observeAsState(mapOf())
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Card {
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.create_playlist).uppercase(),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp, top = 8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Divider(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(
                            8.dp
                        )
                    )

                    OutlinedTextField(
                        value = text,
                        modifier = Modifier
                            .requiredHeight(60.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_playlist),
                                modifier = Modifier.size(30.dp),
                                contentDescription = "emailIcon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        onValueChange = { text = it },
                        label = {
                            Text(
                                text = stringResource(R.string.playlist_name).uppercase(),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        },
                        shape = RoundedCornerShape(25.dp),
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 12.sp)
                    )

                    Button(
                        onClick = {
                            viewModel.playlistName.value = text.text
                            viewModel.clearChosenSongList()
                            onNavigateToSongChooser.invoke()
                        },
                        enabled = text.text.isNotEmpty(),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.create_playlist).uppercase(),
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        Card(modifier = Modifier.weight(1f)) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { openDialog.value = true }) {
                    Text(
                        text = stringResource(R.string.create_playlist).uppercase(),
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                    )
                }

                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(
                        bottom = 16.dp,
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp
                    )
                )

                LazyColumn(state = listState, modifier = Modifier.padding(vertical = 8.dp)) {

                    items(items = playlists.value.keys.toList()) { playlist ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillParentMaxHeight(1.div(12f))
                        ) {
                            IconButton(
                                onClick = {
                                    viewModel.currentPlaylist.value = playlist
                                    onNavigateToMainScreen.invoke()
                                },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .requiredSize(48.dp)
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_playlist_songs),
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
                                    text = playlist.name,
                                    modifier = Modifier
                                        .weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_semi_bold))

                                )
                                Text(
                                    text = playlist.songs.size.toString(),
                                    modifier = Modifier
                                        .weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                                )
                            }

                            PlaylistMenu(
                                playlist = playlist,
                                viewModel = viewModel,
                                onNavigateToSongChooser = onNavigateToSongChooser
                            )
                        }
                    }
                }
                snappyLazyColumn(listState = listState, coroutineScope = coroutineScope)
            }
        }
    }
}


@Composable
fun PlaylistMenu(
    playlist: Playlist,
    viewModel: PlayerViewModel,
    onNavigateToSongChooser: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
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
                stringResource(R.string.Delete),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.currentPlaylist.value = null
                        viewModel.deletePlaylist(
                            currentPlaylist = playlist
                        )
                        expanded = false
                    })
            )
            Text(
                stringResource(R.string.edit),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        onNavigateToSongChooser.invoke()
                        viewModel.playlistName.value = playlist.name
                        viewModel.createChosenSongList(playlist)
                        expanded = false
                    })
            )
        }
    }
}


