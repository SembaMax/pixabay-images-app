package com.semba.pixabayimages.data.repository

import com.semba.pixabayimages.core.common.Result
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.repository.datasource.DatabaseDataSource
import com.semba.pixabayimages.data.repository.datasource.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageSearchRepository @Inject constructor(private val remoteDataSource: NetworkDataSource,
                                                private val localDataSource: DatabaseDataSource) : SearchRepository {

    override suspend fun loadImages(query: String, pageIndex: Int): List<ImageItem> {
        val networkResult = remoteDataSource.fetchImagesWithQuery(query, pageIndex)
        when (networkResult) {
            is Result.Failure -> {
                val localData = localDataSource.fetchImagesWithQuery(query)
                return localData
            }
            is Result.Success -> {
                networkResult.data?.let { localDataSource.insertImagesOfQuery(query, it) }
                return networkResult.data ?: emptyList()
            }
        }
    }
}