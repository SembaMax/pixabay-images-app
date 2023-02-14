package com.semba.pixabayimages.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.semba.pixabayimages.R
import com.semba.pixabayimages.core.common.connectivity.NetworkMonitor
import com.semba.pixabayimages.core.design.theme.PixabayImagesTheme
import com.semba.pixabayimages.navigation.PixabayNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixabayImagesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val coroutineScope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val appState = remember {
                        AppState(navController, coroutineScope, networkMonitor)
                    }

                    Box(
                        Modifier
                            .fillMaxSize()
                    ) {
                        PixabayNavHost(
                            navController = appState.navController,
                            onBackClick = appState::onBackClick,
                            navigateTo = appState::navigateToScreenDestination
                        )

                        val isOffline by appState.isOffline.collectAsState()

                        if (isOffline) {
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(MaterialTheme.colorScheme.error)
                                .align(Alignment.TopEnd))
                            {
                                Text(modifier = Modifier.padding(7.dp), text = stringResource(R.string.offline), fontSize = 10.sp, color = MaterialTheme.colorScheme.onError, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                }
            }
        }
    }
}