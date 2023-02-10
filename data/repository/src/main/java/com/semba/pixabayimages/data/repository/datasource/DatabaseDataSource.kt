package com.semba.pixabayimages.data.repository.datasource

import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.local.dao.SearchImageDao
import com.semba.pixabayimages.data.local.entity.SearchQueryEntity
import com.semba.pixabayimages.data.local.entity.toEntity
import com.semba.pixabayimages.data.local.entity.toModel
import javax.inject.Inject

class DatabaseDataSource @Inject constructor(private val searchImageDao: SearchImageDao) : LocalDataSource {

    override suspend fun fetchImagesWithQuery(
        query: String
    ): List<ImageItem> {
        val data = searchImageDao.getAllImagesOfQuery(query).firstOrNull()
        return data?.images?.map { it.toModel() } ?: emptyList()
    }

    override suspend fun insertImagesOfQuery(query: String, images: List<ImageItem>) {
        searchImageDao.insertSearchQuery(SearchQueryEntity(query))
        images.forEach {
            searchImageDao.insertImage(it.toEntity())
        }
    }
}