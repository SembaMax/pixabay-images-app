package com.semba.pixabayimages.data.local.entity.relationship

import androidx.room.Entity

@Entity
data class SearchQueryImagesCrossRef (
    val searchQuery: String,
    val imageId: String,
        )