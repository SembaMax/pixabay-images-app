package com.semba.pixabayimages.core.testing.data

import com.semba.pixabayimages.data.local.entity.ImageEntity
import com.semba.pixabayimages.data.local.entity.toEntity
import com.semba.pixabayimages.data.local.entity.toModel
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.model.search.network.SearchResultItem
import com.semba.pixabayimages.data.model.search.network.toModel
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ModelTest {

    @Test
    fun `map image response to image model`() {
        val imageResultItem = SearchResultItem(
            id = 1,
            type = "type",
            tags = "tag1, tag2",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 1,
            downloads = 1,
            likes = 1,
            comments = 1,
            user = "user",
            userImageURL = "userImageURL"
        )

        val imageModel = imageResultItem.toModel()

        assertEquals(1, imageModel.id)
        assertEquals("type", imageModel.type)
        assertEquals("tag1, tag2", imageModel.tags)
        assertEquals("imageURL", imageModel.imageURL)
        assertEquals("fullHDURL", imageModel.fullHDURL)
        assertEquals(1, imageModel.views)
        assertEquals(1, imageModel.downloads)
        assertEquals(1, imageModel.likes)
        assertEquals(1, imageModel.comments)
        assertEquals("user", imageModel.user)
        assertEquals("userImageURL", imageModel.userImageURL)
    }

    @Test
    fun `map image entity to image model`() {
        val imageEntity = ImageEntity(
            imageId = 1,
            type = "type",
            tags = "tag1, tag2",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 1,
            downloads = 1,
            likes = 1,
            comments = 1,
            user = "user",
            userImageURL = "userImageURL"
        )

        val imageModel = imageEntity.toModel()

        assertEquals(1, imageModel.id)
        assertEquals("type", imageModel.type)
        assertEquals("tag1, tag2", imageModel.tags)
        assertEquals("imageURL", imageModel.imageURL)
        assertEquals("fullHDURL", imageModel.fullHDURL)
        assertEquals(1, imageModel.views)
        assertEquals(1, imageModel.downloads)
        assertEquals(1, imageModel.likes)
        assertEquals(1, imageModel.comments)
        assertEquals("user", imageModel.user)
        assertEquals("userImageURL", imageModel.userImageURL)
    }

    @Test
    fun `map image model to image entity`() {
        val imageItem = ImageItem(
            id = 1,
            type = "type",
            tags = "tag1, tag2",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 1,
            downloads = 1,
            likes = 1,
            comments = 1,
            user = "user",
            userImageURL = "userImageURL"
        )

        val imageEntity = imageItem.toEntity()

        assertEquals(1, imageEntity.imageId)
        assertEquals("type", imageEntity.type)
        assertEquals("tag1, tag2", imageEntity.tags)
        assertEquals("imageURL", imageEntity.imageURL)
        assertEquals("fullHDURL", imageEntity.fullHDURL)
        assertEquals(1, imageEntity.views)
        assertEquals(1, imageEntity.downloads)
        assertEquals(1, imageEntity.likes)
        assertEquals(1, imageEntity.comments)
        assertEquals("user", imageEntity.user)
        assertEquals("userImageURL", imageEntity.userImageURL)
    }
}