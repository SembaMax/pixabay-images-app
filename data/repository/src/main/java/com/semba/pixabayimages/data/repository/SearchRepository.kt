package com.semba.pixabayimages.data.repository

import com.semba.pixabayimages.data.model.search.ImageItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun loadImages(query: String, pageIndex: Int): List<ImageItem>
}