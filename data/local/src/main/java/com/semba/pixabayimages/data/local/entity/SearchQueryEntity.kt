package com.semba.pixabayimages.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "queries"
)
data class SearchQueryEntity (
    @PrimaryKey(autoGenerate = false)
    val searchQuery: String
        )