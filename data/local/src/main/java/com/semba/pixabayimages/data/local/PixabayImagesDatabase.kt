package com.semba.pixabayimages.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.semba.pixabayimages.data.local.dao.SearchImageDao
import com.semba.pixabayimages.data.local.entity.ImageEntity
import com.semba.pixabayimages.data.local.entity.SearchQueryEntity
import com.semba.pixabayimages.data.local.entity.relationship.SearchQueryImagesCrossRef

@Database(
    entities = [ImageEntity::class, SearchQueryEntity::class, SearchQueryImagesCrossRef::class],
    version = 1
)
abstract class PixabayImagesDatabase: RoomDatabase() {

    abstract fun searchImageDao(): SearchImageDao
}
