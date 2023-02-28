package com.semba.pixabayimages.data.remote.network

import com.semba.pixabayimages.data.model.search.Constants
import com.semba.pixabayimages.data.model.search.network.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayNetworkService {

    @GET(Routes.SEARCH_IMAGES_ENDPOINT)
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        @Query("image_type") imageType: String = "photo",
        @Query("order") order: String = "popular",
        @Query("lang") lang: String = "en"
    ): Response<SearchResponse>
}