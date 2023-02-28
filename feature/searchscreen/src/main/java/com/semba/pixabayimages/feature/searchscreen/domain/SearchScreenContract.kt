package com.semba.pixabayimages.feature.searchscreen.domain

import com.semba.pixabayimages.data.model.search.ImageItem

interface SearchScreenContract {
    fun loadNextPage()
    fun onSearchClick()
    fun onImageItemClicked(imageItem: ImageItem)
    fun removeImageItemClicked()
    fun updateQuery(query: String)
}