package com.semba.pixabayimages.data.model.search

data class ImageItem (
        val id: Long,
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
        ) {
    companion object {
        fun empty() = ImageItem(0L,"","","","",0,0,0,0,"","")
    }
}