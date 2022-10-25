package com.orion.mediaplayercompose

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orion.mediaplayercompose.data.models.Playlist
import com.orion.mediaplayercompose.data.models.Song
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(viewModel: PlayerViewModel, onNavigateToSongChooser: () -> Unit) {

    val playlists: State<Map<Playlist, MutableList<Song>>> =
        viewModel.playlists.observeAsState(mapOf())

    val currentPlaylist = viewModel.currentPlaylist.observeAsState()
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val states: MutableList<MutableState<Boolean>> = mutableListOf()

    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {

        Card {
            Column(verticalArrangement = Arrangement.Top, modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "CREATE PLAYLIST",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    OutlinedTextField(
                        value = text,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 40.dp)
                            .weight(1f)
                            .padding(end = 16.dp),
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
                                text = "Playlist name",
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        },
                        shape = RoundedCornerShape(25.dp),
                        textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 14.sp)
                    )

                    IconButton(
                        onClick = onNavigateToSongChooser,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .requiredSize(45.dp),
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_playlist_add),
                            contentDescription = "Play",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Card(modifier = Modifier.weight(1f)) {
            Column(verticalArrangement = Arrangement.Top, modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "PLAYLISTS",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                LazyColumn(state = listState, modifier = Modifier.padding(vertical = 8.dp)) {
                    items(items = playlists.value.keys.toList()) { playlist ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillParentMaxHeight(0.1f)
                        ) {
                            IconButton(
                                onClick = { },
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_playlist_songs),
                                    contentDescription = "Play",
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .requiredSize(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Text(
                                text = playlist.name,
                                modifier = Modifier
                                    .weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_semi_bold))
                            )
                            PlaylistMenu()
                        }
                    }
                }
                snappyLazyColumn(listState = listState, coroutineScope = coroutineScope)
            }
        }
    }
}


@Composable
fun PlaylistMenu() {
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
                "Удалить",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
            Text(
                "Редактировать",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
        }
    }
}

