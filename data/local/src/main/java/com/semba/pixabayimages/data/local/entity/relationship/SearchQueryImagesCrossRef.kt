package com.semba.pixabayimages.data.local.entity.relationship

import androidx.room.Entity

@Entity(primaryKeys = ["searchQuery", "imageId"])
data class SearchQueryImagesCrossRef (
    val searchQuery: String,
    val imageId: Long,
        )