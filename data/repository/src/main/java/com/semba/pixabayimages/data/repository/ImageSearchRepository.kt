package com.semba.pixabayimages.data.repository

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.common.DataResponse
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.local.datasource.LocalDataSource
import com.semba.pixabayimages.data.model.search.Constants
import com.semba.pixabayimages.data.remote.datasource.RemoteDataSource
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class ImageSearchRepository @Inject constructor(private val remoteDataSource: RemoteDataSource,
                                                private val localDataSource: LocalDataSource) : SearchRepository {

    override fun loadImages(query: String, pageIndex: Int, pageSize: Int): Flow<DataResponse<List<ImageItem>>> = flow {
        var result = checkForRemoteData(query, pageIndex, pageSize)
        if (result.errorCode != null && pageIndex == 1) {
            //try to fetch from database
            result = checkForLocalData(query)
        }
        emit(result)
    }

    override fun loadImageItem(imageId: Long): Flow<DataResponse<ImageItem>> = flow<DataResponse<ImageItem>> {
        val item = localDataSource.fetchImageItemWithId(imageId)
        emit(DataResponse.Success(item))
    }.catch {
        emit(DataResponse.Failure(ErrorCode.DATABASE_ERROR))
    }

    private suspend fun checkForRemoteData(query: String, pageIndex: Int, pageSize: Int): DataResponse<List<ImageItem>> {
        val networkResult = remoteDataSource.fetchImagesWithQuery(query, pageIndex, pageSize)
        return when (networkResult) {
            is DataResponse.Failure -> {
                Timber.d("Repository emits error ${networkResult.errorCode}")
                DataResponse.Failure(networkResult.errorCode)
            }
            is DataResponse.Success -> {
                networkResult.data?.let { localDataSource.insertImagesOfQuery(query, it) }
                Timber.d("Repository emits remote items ${networkResult.data?.size}")
                DataResponse.Success(networkResult.data ?: emptyList())
            }
        }
    }

    private suspend fun checkForLocalData(query: String): DataResponse<List<ImageItem>>  {
            val localData = localDataSource.fetchImagesWithQuery(query)
            return DataResponse.Success(localData)
    }
}