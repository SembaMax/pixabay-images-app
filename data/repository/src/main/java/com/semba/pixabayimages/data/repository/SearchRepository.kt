package com.semba.pixabayimages.data.repository

import com.semba.pixabayimages.core.common.Result
import com.semba.pixabayimages.data.model.search.ImageItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun loadImages(query: String, pageIndex: Int): Flow<Result<List<ImageItem>>>
    fun loadImageItem(imageItem: Long): Flow<Result<ImageItem>>
}