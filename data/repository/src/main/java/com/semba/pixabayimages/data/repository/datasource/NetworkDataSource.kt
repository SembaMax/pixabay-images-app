package com.semba.pixabayimages.data.repository.datasource

import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.remote.model.toModel
import com.semba.pixabayimages.data.remote.network.PixabayNetworkService
import com.semba.pixabayimages.core.common.Result
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val service: PixabayNetworkService): RemoteDataSource {

    override suspend fun fetchImagesWithQuery(
        query: String,
        pageIndex: Int
    ): Result<List<ImageItem>> {
        val result = service.search(query, query)
        if (result.isSuccessful && !result.body()?.items.isNullOrEmpty()) {
            val items = result.body()!!.items.map { it.toModel() }
            return Result.Success(items)
        } else {
            val errorCode = result.code()
            return Result.Failure(errorCode)
        }
    }
}