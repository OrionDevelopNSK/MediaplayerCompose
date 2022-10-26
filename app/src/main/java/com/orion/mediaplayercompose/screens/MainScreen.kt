package com.orion.mediaplayercompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.orion.mediaplayercompose.R
import com.orion.mediaplayercompose.ui.theme.ThirdColor
import com.orion.mediaplayercompose.utils.SortingType
import com.orion.mediaplayercompose.utils.StateMode
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainScreen(viewModel: PlayerViewModel, onNavigateToPlaylists: () -> Unit) {

    val songs = viewModel.songs.observeAsState(mutableListOf())
    val stateMode = viewModel.stateMode.observeAsState(StateMode.LOOP)

    val isPlayed: State<Boolean> = viewModel.playerPlayed.observeAsState(false)
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    if (songs.value?.isNotEmpty() == true) {

        Column(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, top = 16.dp, end = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Card(modifier = Modifier.height(70.dp)) {
                UpperContainer(onNavigateToPlaylists = onNavigateToPlaylists, viewModel)
            }

            Card(modifier = Modifier.weight(1f)) {
                Column {
                    LazyColumn(state = listState, modifier = Modifier.padding(vertical = 9.dp)) {
                        itemsIndexed(songs.value) { index: Int, song ->
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillParentMaxHeight(0.1f)
                            ) {
                                IconButton(
                                    onClick = {
                                        val isState = songs.value[index].isPlayed
                                        songs.value.forEach { it.isPlayed = false }
                                        songs.value[index].isPlayed = !isState
                                    },
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .requiredSize(48.dp),
                                ) {
                                    Icon(
                                        painterResource(id = if (!songs.value[index].isPlayed) R.drawable.ic_play else R.drawable.ic_pause),
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
                            snappyLazyColumn(listState = listState, coroutineScope = coroutineScope)
                        }
                    }
                }
            }

            Card(modifier = Modifier.height(140.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    var sliderPosition by remember { mutableStateOf(0f) }

                    Text(
                        text = "MUSIC SONG ",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.yanone_kaffeesatz_semi_bold))
                    )

                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
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
                            text = "00:00",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "00:00",
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
                            onClick = { /*TODO*/ }
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
                                viewModel.playerPlayed.value = !viewModel.playerPlayed.value!!
                            },
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Icon(
                                painterResource(id = if (!isPlayed.value) R.drawable.ic_play else R.drawable.ic_pause),
                                contentDescription = "Play",
                                modifier = Modifier.requiredSize(50.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.ic_next),
                                contentDescription = "Next",
                                modifier = Modifier.requiredSize(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { viewModel.changeStateMode() },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                painterResource(
                                    id = when (stateMode.value) {
                                        StateMode.LOOP -> R.drawable.ic_loop
                                        StateMode.REPEAT -> R.drawable.ic_repeat_one
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
            }
        }
    }
}

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
                    text = "Find song",
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
fun SongMenu() {
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
                "Скопировать",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
            Text(
                "Вставить",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
            Divider()
            Text(
                "Настройки",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {})
            )
        }
    }
}

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
                "Сортировать по:",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.primary

            )
            Divider(color = MaterialTheme.colorScheme.primary)

            Text(
                "- по дате добавления",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.changeSortingType(SortingType.DATE)
                        playbackOrder = false
                    }),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "- по рейтингу",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.changeSortingType(SortingType.RATING)
                        playbackOrder = false
                    }),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                "- по частоте прослушивания",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {
                        viewModel.changeSortingType(SortingType.REPEATABILITY)
                        playbackOrder = false
                    }),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
