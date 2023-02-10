package com.semba.pixabayimages.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.semba.pixabayimages.data.model.search.ImageItem

@Entity(
    tableName = "images"
)
data class ImageEntity (
    @PrimaryKey(autoGenerate = false)
    val imageId: Long,

    val type: String,
    val tags: String,
    val imageURL: String,
    val fullHDURL: String,
    val views: Int,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val user: String,
    val userImageURL: String,
        )


fun ImageEntity.toModel(): ImageItem = ImageItem(
    id = this.imageId,
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


fun ImageItem.toEntity(): ImageEntity = ImageEntity(
    imageId = this.id,
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