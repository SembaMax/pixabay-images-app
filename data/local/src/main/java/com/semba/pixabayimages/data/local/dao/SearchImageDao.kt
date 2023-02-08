package com.semba.pixabayimages.data.local.dao

import androidx.room.*
import com.semba.pixabayimages.data.local.entity.ImageEntity
import com.semba.pixabayimages.data.local.entity.SearchQueryEntity
import com.semba.pixabayimages.data.local.entity.relationship.SearchQueryImagesCrossRef
import com.semba.pixabayimages.data.local.entity.relationship.SearchQueryWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(entity: ImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQuery(entity: SearchQueryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQueryImageCrossRef(entity: SearchQueryImagesCrossRef)

    @Transaction
    @Query("SELECT * FROM queries WHERE searchQuery = :query")
    suspend fun getAllImagesOfQuery(query: String): Flow<List<SearchQueryWithImages>>

}