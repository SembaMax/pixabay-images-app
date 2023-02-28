package com.semba.pixabayimages.data.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.semba.pixabayimages.data.remote.BuildConfig
import com.semba.pixabayimages.data.remote.network.PixabayNetworkService
import com.semba.pixabayimages.data.remote.network.Routes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor())
            .addInterceptor(loggingInterceptor())
            .build()
    }

    private fun authorizationInterceptor() = Interceptor {
        val url: HttpUrl = it.request().url
            .newBuilder()
            .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
            .build()
        val request: Request = it.request().newBuilder().url(url).build()
        it.proceed(request)
    }

    private fun loggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    @Singleton
    fun providesRetrofitService(okHttpClient: OkHttpClient): PixabayNetworkService = apiService(Routes.BASE_URL.toHttpUrl(), okHttpClient)
}

fun apiService(baseUrl: HttpUrl, okHttpClient: OkHttpClient): PixabayNetworkService
{
    val json = Json {
        ignoreUnknownKeys = true
    }

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(PixabayNetworkService::class.java)
}