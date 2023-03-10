package com.semba.pixabayimages.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.semba.pixabayimages.core.design.navigation.ScreenDestination
import com.semba.pixabayimages.feature.detailscreen.detailScreen
import com.semba.pixabayimages.feature.searchscreen.searchRoute
import com.semba.pixabayimages.feature.searchscreen.searchScreen

@Composable
fun PixabayNavHost (
    navController: NavHostController,
    onBackClick: () -> Unit,
    navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = searchRoute
)
{
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        searchScreen(navigateTo)
        detailScreen()
    }
}