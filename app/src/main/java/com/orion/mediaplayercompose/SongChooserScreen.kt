package com.orion.mediaplayercompose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun SongChooserScreen(viewModel: PlayerViewModel, onNavigateToPlaylists: () -> Unit) {

    val songs = viewModel.songs.observeAsState(listOf())
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    if (songs.value?.isNotEmpty() == true) {
        val isChooses: MutableList<Boolean> = MutableList(songs.value.size) { false }.toMutableStateList()

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
                            text = "MUSIC SONG",
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

                    LazyColumn(state = listState, modifier = Modifier.padding(vertical = 8.dp)) {
                        itemsIndexed(songs.value) { index: Int, song ->
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillParentMaxHeight(0.1f)
                            ) {
                                IconButton(
                                    onClick = { println(song) },
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_play),
                                        contentDescription = "Play",
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .requiredSize(40.dp),
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
                                    //TODO
                                    checked = isChooses[index],
                                    //TODO
                                    onCheckedChange = {
                                        isChooses[index] = it
                                    },
                                    colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                    }
                    snappyLazyColumn(listState = listState, coroutineScope = coroutineScope)
                }
            }

            Card(modifier = Modifier.height(140.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)

                ) {
                    Text(
                        text = "SAVE PLAYLIST?",
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
                                text = "BACK",
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                            )
                        }

                        Button(
                            onClick = {
                                if (isChooses.all { !it }) viewModel.savePlaylist(isChooses)
                                onNavigateToPlaylists.invoke()
                            },
                            enabled = !isChooses.all { !it }

                        ) {
                            Text(
                                text = "SAVE",
                                fontSize = 30.sp,
                                fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_light))
                            )
                        }
                    }
                }
            }
        }
    }

}

