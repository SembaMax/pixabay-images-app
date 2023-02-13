package com.semba.pixabayimages.data.local.di

import com.semba.pixabayimages.data.local.datasource.DatabaseDataSource
import com.semba.pixabayimages.data.local.datasource.LocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindsLocalDataSource(dataSource: DatabaseDataSource): LocalDataSource

}