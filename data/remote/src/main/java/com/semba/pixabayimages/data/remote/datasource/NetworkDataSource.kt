package com.semba.pixabayimages.data.remote.datasource

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.remote.network.PixabayNetworkService
import com.semba.pixabayimages.core.common.DataResponse
import com.semba.pixabayimages.data.model.search.network.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val service: PixabayNetworkService):
    RemoteDataSource {

    override suspend fun fetchImagesWithQuery(
        query: String,
        pageIndex: Int,
        pageSize: Int
    ): DataResponse<List<ImageItem>> = withContext(Dispatchers.IO) {
       try {
           val result = service.search(query = query, page = pageIndex, pageSize = pageSize)
           if (result.isSuccessful) {
               Timber.d("Http request is successful. Items size = ${result.body()?.items?.size}")
               val items = result.body()?.items?.map { it.toModel() } ?: emptyList()
               DataResponse.Success(items)
           } else {
               val errorCode = result.code()
               Timber.d("Http request is failed. ErrorCode is = $errorCode")
               DataResponse.Failure(ErrorCode.from(errorCode))
           }
        }
       catch (e: Exception)
        {
            DataResponse.Failure(ErrorCode.SERVER_ERROR)
        }
    }
}