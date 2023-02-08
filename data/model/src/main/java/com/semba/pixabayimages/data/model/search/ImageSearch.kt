package com.semba.pixabayimages.data.model.search

data class ImageSearch (
    val total: Int? = null,
    val totalItems: Int? = null,
    val items: List<ImageItem>? = null
        )