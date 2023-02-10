package com.semba.pixabayimages.data.local.di

import android.content.Context
import androidx.room.Room
import com.semba.pixabayimages.data.local.PixabayImagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(context: Context): PixabayImagesDatabase {
        return Room.databaseBuilder(context, PixabayImagesDatabase::class.java, "pixabay-database").build()
    }
}
