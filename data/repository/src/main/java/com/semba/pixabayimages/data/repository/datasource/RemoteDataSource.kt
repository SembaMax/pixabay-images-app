package com.semba.pixabayimages.data.repository.datasource

import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.core.common.Result

interface RemoteDataSource {
    suspend fun fetchImagesWithQuery(query: String, pageIndex: Int): Result<List<ImageItem>>
}