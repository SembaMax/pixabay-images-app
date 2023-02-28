package com.semba.pixabayimages.data.repository

import com.semba.pixabayimages.core.common.DataResponse
import com.semba.pixabayimages.data.model.search.Constants
import com.semba.pixabayimages.data.model.search.ImageItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun loadImages(query: String, pageIndex: Int, pageSize: Int = Constants.DEFAULT_PAGE_SIZE): Flow<DataResponse<List<ImageItem>>>
    fun loadImageItem(imageId: Long): Flow<DataResponse<ImageItem>>
}