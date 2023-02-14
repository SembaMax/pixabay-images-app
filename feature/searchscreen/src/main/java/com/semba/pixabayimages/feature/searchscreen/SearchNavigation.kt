package com.semba.pixabayimages.feature.searchscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.semba.pixabayimages.core.design.navigation.ScreenDestination


const val searchRoute = "search_screen_route"

fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) {
    this.navigate(searchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(navigateTo: (screenDestination: ScreenDestination, args: Map<String, String>) -> Unit) {
    composable(route = searchRoute) {
        SearchScreen(navigateTo)
    }
}

