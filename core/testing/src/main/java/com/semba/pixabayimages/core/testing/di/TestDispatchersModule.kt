package com.semba.pixabayimages.core.testing.di

import com.semba.pixabayimages.core.common.AppDispatcher
import com.semba.pixabayimages.core.common.Dispatcher
import com.semba.pixabayimages.core.common.di.DispatchersModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
object TestDispatchersModule {

    @Provides
    @Dispatcher(AppDispatcher.IO)
    fun provideTestIODispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
}