package com.semba.pixabayimages.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var Instance: PixabayImagesDatabase? = null

        @Synchronized
        fun getInstance(context: Context) = synchronized(this) {
            Instance ?: buildDatabase(context)
        }

        fun buildDatabase(context: Context): PixabayImagesDatabase {
            return Room.databaseBuilder(context.applicationContext, PixabayImagesDatabase::class.java, "pixabay-database").build()
        }
    }
}