package com.semba.pixabayimages.data.local.entity.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.semba.pixabayimages.data.local.entity.ImageEntity
import com.semba.pixabayimages.data.local.entity.SearchQueryEntity

data class SearchQueryWithImages (
    @Embedded
    val searchQuery: SearchQueryEntity,
    @Relation(
        parentColumn = "searchQuery",
        entityColumn = "imageId",
        associateBy = Junction(SearchQueryImagesCrossRef::class)
    )
    val images: List<ImageEntity>
        )