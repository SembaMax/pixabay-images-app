package com.semba.pixabayimages.data.model.search.network

import com.semba.pixabayimages.data.model.search.ImageSearch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse (
    @SerialName("total") val total: Int,
    @SerialName("totalHits") val totalItems: Int,
    @SerialName("hits") val items: List<SearchResultItem>,
        )


fun SearchResponse.toModel(): ImageSearch = ImageSearch(
    total = this.total,
    totalItems = this.totalItems,
    items = this.items.map { it.toModel() }
)