package com.semba.pixabayimages.core.testing

import com.semba.pixabayimages.core.common.DataResponse
import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.model.search.network.SearchResultItem
import com.semba.pixabayimages.data.model.search.network.toModel
import com.semba.pixabayimages.data.repository.SearchRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TestImagesRepository: SearchRepository {

    private val imagesFlow: MutableSharedFlow<List<SearchResultItem>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    private var shouldSuccess = true
    private var error: ErrorCode = ErrorCode.UNSPECIFIED

    override fun loadImages(query: String, pageIndex: Int, pageSize: Int): Flow<DataResponse<List<ImageItem>>> {
        return if (shouldSuccess)
            imagesFlow.map { images -> DataResponse.Success(data = images.map { it.toModel()}) }
        else
            flow { emit(DataResponse.Failure(errorCode = error)) }
    }

    override fun loadImageItem(imageId: Long): Flow<DataResponse<ImageItem>> {
        return if (shouldSuccess)
            imagesFlow.map { images -> DataResponse.Success(data = images.first().toModel()) }
        else
            flow { emit(DataResponse.Failure(errorCode = error)) }
    }

    fun setImageItems(images: List<SearchResultItem>)
    {
        imagesFlow.tryEmit(images)
    }

    fun setIsSuccessful(isSuccessful: Boolean, errorCode: ErrorCode? = null)
    {
        shouldSuccess = isSuccessful
        error = errorCode ?: ErrorCode.UNSPECIFIED
    }
}