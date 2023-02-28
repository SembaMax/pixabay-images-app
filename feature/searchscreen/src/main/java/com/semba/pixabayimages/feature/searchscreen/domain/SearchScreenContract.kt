package com.semba.pixabayimages.feature.searchscreen.domain

import com.semba.pixabayimages.data.model.search.ImageItem

interface SearchScreenContract {
    fun loadNextPage()
    fun onSearchClick()
    fun showConfirmationDialog(imageItem: ImageItem)
    fun dismissConfirmationDialog()
    fun updateQuery(query: String)
}