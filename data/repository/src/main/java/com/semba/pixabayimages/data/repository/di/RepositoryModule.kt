package com.semba.pixabayimages.data.repository.di

import com.semba.pixabayimages.data.repository.ImageSearchRepository
import com.semba.pixabayimages.data.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsSearchRepository(repository: ImageSearchRepository): SearchRepository
}