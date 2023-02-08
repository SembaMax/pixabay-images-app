package com.semba.pixabayimages.data.local.di

import com.semba.pixabayimages.data.local.PixabayImagesDatabase
import com.semba.pixabayimages.data.local.dao.SearchImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providesSearchImageDao(database: PixabayImagesDatabase): SearchImageDao = database.searchImageDao()
}