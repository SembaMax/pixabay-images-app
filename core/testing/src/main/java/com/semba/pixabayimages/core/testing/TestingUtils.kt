package com.semba.pixabayimages.core.testing

import com.semba.pixabayimages.data.model.search.network.SearchResponse
import com.semba.pixabayimages.data.model.search.network.SearchResultItem
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

object TestingUtils {
    fun imagesJsonAsString(): String {
        val resourceAsStream = javaClass.classLoader?.getResourceAsStream("PixabayImagesResponse.json")
        val reader = InputStreamReader(resourceAsStream)
        return reader.use { it.readText() }
    }

    fun imagesJsonAsItems(): List<SearchResultItem> {
        val jsonString = imagesJsonAsString()
        return Json {
            ignoreUnknownKeys = true
        }.decodeFromString(SearchResponse.serializer(), jsonString).items
    }
}