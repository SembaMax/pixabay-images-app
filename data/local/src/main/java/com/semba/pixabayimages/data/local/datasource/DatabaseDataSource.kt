package com.semba.pixabayimages.data.local.datasource

import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.local.dao.SearchImageDao
import com.semba.pixabayimages.data.local.entity.SearchQueryEntity
import com.semba.pixabayimages.data.local.entity.relationship.SearchQueryImagesCrossRef
import com.semba.pixabayimages.data.local.entity.toEntity
import com.semba.pixabayimages.data.local.entity.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DatabaseDataSource @Inject constructor(private val searchImageDao: SearchImageDao) :
    LocalDataSource {

    override suspend fun fetchImagesWithQuery(
        query: String
    ): List<ImageItem> = withContext(Dispatchers.IO) {
        val data = searchImageDao.getAllImagesOfQuery(query).firstOrNull()
        data?.images?.map { it.toModel() } ?: emptyList()
    }

    override suspend fun insertImagesOfQuery(query: String, images: List<ImageItem>) = withContext(Dispatchers.IO) {
        searchImageDao.insertSearchQuery(SearchQueryEntity(query))
        images.forEach {
            searchImageDao.insertImage(it.toEntity())
            searchImageDao.insertSearchQueryImageCrossRef(SearchQueryImagesCrossRef(query, it.id))
        }
    }

    override suspend fun fetchImageItemWithId(imageId: Long): ImageItem = withContext(Dispatchers.IO) {
        searchImageDao.getImageOfId(imageId).toModel()
    }
}