package com.semba.pixabayimages.data.repository

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.common.Result
import com.semba.pixabayimages.core.common.connectivity.NetworkMonitor
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.local.datasource.DatabaseDataSource
import com.semba.pixabayimages.data.remote.datasource.NetworkDataSource
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class ImageSearchRepository @Inject constructor(private val remoteDataSource: NetworkDataSource,
                                                private val localDataSource: DatabaseDataSource) : SearchRepository {

    override fun loadImages(query: String, pageIndex: Int): Flow<Result<List<ImageItem>>> = flow {
        var result = checkForRemoteData(query, pageIndex)
        if (result.errorCode != null && pageIndex == 1) {
            //try to fetch from database
            result = checkForLocalData(query)
        }
        emit(result)
    }

    override fun loadImageItem(imageId: Long): Flow<Result<ImageItem>> = flow<Result<ImageItem>> {
        val item = localDataSource.fetchImageItemWithId(imageId)
        emit(Result.Success(item))
    }.catch {
        emit(Result.Failure(ErrorCode.DATABASE_ERROR))
    }

    private suspend fun checkForRemoteData(query: String, pageIndex: Int): Result<List<ImageItem>> {
        val networkResult = remoteDataSource.fetchImagesWithQuery(query, pageIndex)
        return when (networkResult) {
            is Result.Failure -> {
                Timber.d("Repository emits error ${networkResult.errorCode}")
                Result.Failure(networkResult.errorCode)
            }
            is Result.Success -> {
                networkResult.data?.let { localDataSource.insertImagesOfQuery(query, it) }
                Timber.d("Repository emits remote items ${networkResult.data?.size}")
                Result.Success(networkResult.data ?: emptyList())
            }
        }
    }

    private suspend fun checkForLocalData(query: String): Result<List<ImageItem>>  {
            val localData = localDataSource.fetchImagesWithQuery(query)
            return Result.Success(localData)
    }
}