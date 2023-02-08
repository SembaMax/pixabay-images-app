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
        )