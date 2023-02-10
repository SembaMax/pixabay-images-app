package com.semba.pixabayimages.core.common.pagination

interface Paginator<Item> {
    suspend fun loadNextItems()
    fun reset()
}