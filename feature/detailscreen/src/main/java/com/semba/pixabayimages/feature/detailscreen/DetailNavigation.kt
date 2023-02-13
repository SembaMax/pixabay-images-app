package com.semba.pixabayimages.feature.detailscreen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val userNameArg = "userName"
const val userImageArg = "userImage"
const val imageUrlArg = "imageUrl"
const val likesArg = "likes"
const val commentsArg = "comments"
const val viewsArg = "views"
const val tagsArg = "tags"
const val downloadsRoute = "downloads"
const val detailRoute = "detail_screen_route"

fun NavController.navigateToDetailScreen(navOptions: NavOptions? = null) {
    this.navigate(detailRoute, navOptions)
}

fun NavGraphBuilder.detailScreen() {
    composable(route = detailRoute) { navBackStackEntry ->
        val userName = navBackStackEntry.arguments?.getString(userNameArg) ?: ""
        val userImage = navBackStackEntry.arguments?.getString(userImageArg) ?: ""
        val imageUrl = navBackStackEntry.arguments?.getString(imageUrlArg) ?: ""
        val likes = navBackStackEntry.arguments?.getInt(likesArg) ?: 0
        val comments = navBackStackEntry.arguments?.getInt(commentsArg) ?: 0
        val views = navBackStackEntry.arguments?.getInt(viewsArg) ?: 0
        val downloads = navBackStackEntry.arguments?.getInt(downloadsRoute) ?: 0
        val tags = navBackStackEntry.arguments?.getStringArrayList(tagsArg) ?: arrayListOf()
        DetailScreen(userName, userImage, imageUrl, likes, comments, views, downloads, tags)
    }
}