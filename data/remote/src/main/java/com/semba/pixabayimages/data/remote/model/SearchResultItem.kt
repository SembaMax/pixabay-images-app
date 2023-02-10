package com.semba.pixabayimages.data.remote.model

import com.semba.pixabayimages.data.model.search.ImageItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.semba.pixabayimages.core.common.Result

@Serializable
data class SearchResultItem (
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("tags") val tags: String,
    @SerialName("imageURL") val imageURL: String,
    @SerialName("fullHDURL") val fullHDURL: String,
    @SerialName("views") val views: Int,
    @SerialName("downloads") val downloads: Int,
    @SerialName("likes") val likes: Int,
    @SerialName("comments") val comments: Int,
    @SerialName("user") val user: String,
    @SerialName("userImageURL") val userImageURL: String,
        )


fun SearchResultItem.toModel(): ImageItem = ImageItem(
    id = this.id,
    type = this.type,
    tags = this.tags,
    imageURL = this.imageURL,
    fullHDURL = this.fullHDURL,
    views = this.views,
    downloads = this.downloads,
    likes = this.likes,
    comments = this.comments,
    user = this.user,
    userImageURL = this.userImageURL,
)
