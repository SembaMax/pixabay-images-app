package com.semba.pixabayimages.data.local.di

import android.content.Context
import androidx.room.Room
import com.semba.pixabayimages.data.local.PixabayImagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): PixabayImagesDatabase {
        return PixabayImagesDatabase.getInstance(context)
    }
}
