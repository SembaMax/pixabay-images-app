package com.semba.pixabayimages.data.remote.datasource

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.remote.model.toModel
import com.semba.pixabayimages.data.remote.network.PixabayNetworkService
import com.semba.pixabayimages.core.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val service: PixabayNetworkService):
    RemoteDataSource {

    override suspend fun fetchImagesWithQuery(
        query: String,
        pageIndex: Int
    ): Result<List<ImageItem>> = withContext(Dispatchers.IO) {
        val result = service.search(query = query, page = pageIndex)
        if (result.isSuccessful) {
            Timber.d("Http request is successful. Items size = ${result.body()?.items?.size}")
            val items = result.body()?.items?.map { it.toModel() } ?: emptyList()
            Result.Success(items)
        } else {
            val errorCode = result.code()
            Timber.d("Http request is failed. ErrorCode is = $errorCode")
            Result.Failure(ErrorCode.from(errorCode))
        }
    }
}