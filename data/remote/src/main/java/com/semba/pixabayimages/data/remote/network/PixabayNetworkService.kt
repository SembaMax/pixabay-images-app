package com.semba.pixabayimages.data.remote.network

import com.semba.pixabayimages.data.remote.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayNetworkService {

    @GET(Routes.SEARCH_ENDPOINT)
    fun search(@Query("key") key: String = "",
               @Query("q") query: String,
               @Query("page") page: Int = 1,
               @Query("per_page") pageSize: Int = 60,
               @Query("image_type") imageType: String = "photo",
               @Query("order") order: String = "popular",
               @Query("lang") lang: String = "en"
    ): Response<SearchResponse>
}