package com.semba.pixabayimages.core.testing.di

import com.semba.pixabayimages.core.testing.TestImagesRepository
import com.semba.pixabayimages.data.repository.SearchRepository
import com.semba.pixabayimages.data.repository.di.RepositoryModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Binds
    abstract fun bindTestRepository(repository: TestImagesRepository): SearchRepository
}