package com.semba.pixabayimages.data.remote.datasource

import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.core.common.DataResponse

interface RemoteDataSource {
    suspend fun fetchImagesWithQuery(query: String, pageIndex: Int, pageSize: Int): DataResponse<List<ImageItem>>
}