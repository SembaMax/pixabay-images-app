package com.semba.pixabayimages.core.design.navigation

import timber.log.Timber

const val IMAGE_ID_ARG = "imageId"

fun String.withArgs(args: Map<String, String>): String {
    var result = this
    args.forEach { (key, value) ->
        result = result.replace("{$key}", value)
    }
    Timber.d("Details screen route with args: $result")
    return result
}