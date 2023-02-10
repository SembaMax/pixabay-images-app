package com.semba.pixabayimages.data.repository.datasource

import com.semba.pixabayimages.data.model.search.ImageItem

interface LocalDataSource {
    suspend fun fetchImagesWithQuery(query: String): List<ImageItem>
    suspend fun insertImagesOfQuery(query: String, images: List<ImageItem>)
}