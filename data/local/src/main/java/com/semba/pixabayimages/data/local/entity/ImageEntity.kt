package com.semba.pixabayimages.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "images"
)
data class ImageEntity (
    @PrimaryKey
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