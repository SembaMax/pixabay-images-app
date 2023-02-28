package com.semba.pixabayimages.feature.detailscreen

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.semba.pixabayimages.core.design.navigation.*


const val detailRoute = "detail_screen_route/{${IMAGE_ID_ARG}}"

fun NavController.navigateToDetailScreen(args: Map<String, String>, navOptions: NavOptions? = null) {
    this.navigate(detailRoute.withArgs(args), navOptions)
}

private val args = listOf(
    navArgument(IMAGE_ID_ARG) {
        type = NavType.LongType
    },
)

fun NavGraphBuilder.detailScreen() {
    composable(route = detailRoute, arguments = args) { navBackStackEntry ->
        val imageId = navBackStackEntry.arguments?.getLong(IMAGE_ID_ARG) ?: 0L
        DetailRoute(imageId = imageId)
    }
}