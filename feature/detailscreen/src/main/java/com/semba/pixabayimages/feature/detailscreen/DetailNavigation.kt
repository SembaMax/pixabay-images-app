package com.semba.pixabayimages.feature.detailscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val detailRoute = "detail_screen_route"

fun NavController.navigateToDetailScreen(navOptions: NavOptions? = null) {
    this.navigate(detailRoute, navOptions)
}

fun NavGraphBuilder.detailScreen() {
    composable(route = detailRoute) {
        DetailScreen()
    }
}