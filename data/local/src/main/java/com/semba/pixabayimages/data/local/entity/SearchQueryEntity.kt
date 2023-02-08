package com.semba.pixabayimages.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "queries"
)
data class SearchQueryEntity (
    @PrimaryKey
    val searchQuery: String
        )