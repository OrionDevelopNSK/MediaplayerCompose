package com.orion.mediaplayercompose

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orion.mediaplayercompose.data.database.AppDataBase
import com.orion.mediaplayercompose.data.database.AudioReader
import com.orion.mediaplayercompose.data.repositories.RoomSongRepository
import com.orion.mediaplayercompose.ui.theme.MediaplayerComposeTheme
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {

    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MediaplayerComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AppNavHost(viewModel = viewModel)
//                    PlaylistScreen(viewModel = viewModel)
//                    SongChooserScreen(viewModel)
//                    MainScreen(viewModel)
                }
            }
        }
        checkPermissions()
    }

    @Composable
    fun AppNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
        startDestination: String = "mainScreen",
        viewModel: PlayerViewModel
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable("mainScreen") {
                MainScreen(
                    viewModel = viewModel,
                    onNavigateToPlaylists = { navController.navigate("playlistScreen") }
                )
            }
            composable("playlistScreen") {
                PlaylistScreen(
                    viewModel = viewModel, onNavigateToSongChooser = {
                        navController.navigate("songChooserScreen")
                    }
                )
            }
            composable("songChooserScreen") {
                SongChooserScreen(
                    viewModel = viewModel, onNavigateToPlaylists = { navController.popBackStack() }
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSongs()
            } else {
                Toast.makeText(this, "В доступе отказано", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() {
        val requestCode =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (requestCode != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_MEDIA_LOCATION
                    ),
                    1
                )
            }
        } else {
            readSongs()
        }
    }

    private fun readSongs() {
        val database = AppDataBase.getDatabase(this)
        val audioReader = AudioReader(this)

        runBlocking {
            launch {
                val roomSongRepository = RoomSongRepository(database.songDao())
                val readMediaData = audioReader.readMediaData()
//                viewModel.songs.postValue(readMediaData)
                viewModel.saveSongs(readMediaData)
                AsyncTask.execute { roomSongRepository.insertAllSongs(readMediaData) }

            }
        }
    }
}

