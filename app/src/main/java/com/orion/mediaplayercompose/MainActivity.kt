package com.orion.mediaplayercompose

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orion.mediaplayercompose.screens.MainScreen
import com.orion.mediaplayercompose.screens.PlaylistScreen
import com.orion.mediaplayercompose.screens.SongChooserScreen
import com.orion.mediaplayercompose.ui.theme.MediaplayerComposeTheme
import com.orion.mediaplayercompose.viewmodels.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: PlayerViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        )
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
                    viewModel = viewModel,
                    onNavigateToSongChooser = { navController.navigate("songChooserScreen") },
                    onNavigateToMainScreen = { navController.popBackStack() }
                )
            }
            composable("songChooserScreen") {
                SongChooserScreen(
                    viewModel = viewModel,
                    onNavigateToPlaylists = { navController.popBackStack() }
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = false
            permissions.entries.forEach {
                isGranted = it.value
            }

            if (isGranted) {
                viewModel.readSongs()
                viewModel.loadPlaylist()

                setContent {
                    MediaplayerComposeTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AppNavHost(viewModel = viewModel)
                        }
                    }
                }

            } else {
                Toast.makeText(this, "В доступе отказано", Toast.LENGTH_SHORT).show()
            }
        }

}

