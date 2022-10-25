package com.orion.mediaplayercompose

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
import com.orion.mediaplayercompose.ui.theme.ThirdColor
import com.orion.mediaplayercompose.utils.snappyLazyColumn
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainScreen(viewModel: PlayerViewModel, onNavigateToPlaylists: () -> Unit) {

    val songs = viewModel.songs.observeAsState(listOf())
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Card(modifier = Modifier.height(60.dp)) {
            UpperContainer(onNavigateToPlaylists = onNavigateToPlaylists)
        }

        Card(modifier = Modifier.weight(1f)) {
            Column {
                LazyColumn(state = listState, modifier = Modifier.padding(vertical = 12.dp)) {
                    itemsIndexed(songs.value) { index: Int, song ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillParentMaxHeight(0.1f)
                        ) {
                            IconButton(
                                onClick = { println(songs.value) },
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .requiredSize(40.dp),
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_play),
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
                            painterResource(id = R.drawable.ic_to_start),
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
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_play),
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
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_loop),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpperContainer(onNavigateToPlaylists: () -> Unit) {
    Row(modifier = Modifier.height(50.dp)) {

        PlaybackOrderMenu()

        var text by remember { mutableStateOf(TextFieldValue("")) }

        OutlinedTextField(
            value = text,
            modifier = Modifier
                .height(50.dp)
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
            onClick =  onNavigateToPlaylists,
            modifier = Modifier.padding(top = 8.dp, end = 16.dp).requiredSize(40.dp)
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
fun PlaybackOrderMenu() {
    var playbackOrder by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { playbackOrder = true },
            modifier = Modifier.padding(top = 8.dp, start = 16.dp).requiredSize(40.dp)
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
                    .clickable(onClick = {}),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "- по рейтингу",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {}),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                "- по частоте прослушивания",
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = {}),
                color = MaterialTheme.colorScheme.primary
            )


        }
    }
}
