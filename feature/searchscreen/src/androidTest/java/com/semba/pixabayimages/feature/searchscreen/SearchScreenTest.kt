package com.semba.pixabayimages.feature.searchscreen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.semba.pixabayimages.core.design.theme.PixabayImagesTheme
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.model.search.TestTags
import com.semba.pixabayimages.feature.searchscreen.state.SearchUiState
import com.semba.pixabayimages.core.design.R as DesignR
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var fakeUiState: SearchUiState

    @Before
    fun init() {
        fakeUiState = SearchUiState()
    }

    @Test
    fun testLoadingUIState() {
        fakeUiState = fakeUiState.copy(isLoading = true)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                SearchScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testFailureUIState() {
        fakeUiState = fakeUiState.copy(errorMsg = "Error")

        composeTestRule.setContent {
            PixabayImagesTheme() {
                SearchScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testSuccessUIState() {
        fakeUiState = fakeUiState.copy(imageItems = images)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                SearchScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.SEARCH_GRID_TEST_TAG).assertExists()
    }

    @Test
    fun testGridDisplaysCorrectData() {
        fakeUiState = fakeUiState.copy(imageItems = images)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                SearchScreen(uiState = fakeUiState)
            }
        }

        val firstItem = composeTestRule.onNodeWithTag(TestTags.SEARCH_GRID_TEST_TAG).onChildAt(0)
        val secondItem = composeTestRule.onNodeWithTag(TestTags.SEARCH_GRID_TEST_TAG).onChildAt(1)

        firstItem.assertIsDisplayed()
        firstItem.assert(hasText("user1"))
        firstItem.assert(hasText("tag1, tag2"))

        secondItem.assertIsDisplayed()
        secondItem.assert(hasText("user2"))
        secondItem.assert(hasText("tag2, tag3"))
    }

    @Test
    fun testTopBarIsDisplayedOnSuccessUIState() {

        val headerTitle = composeTestRule.activity.getString(DesignR.string.search_headline)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                SearchScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithText(headerTitle).assertIsDisplayed()
    }

    private val images = listOf(
        ImageItem(
            id = 1,
            type = "type1",
            tags = "tag1, tag2",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 1,
            downloads = 1,
            likes = 1,
            comments = 1,
            user = "user1",
            userImageURL = "userImageURL"
        ),
        ImageItem(
            id = 2,
            type = "type2",
            tags = "tag2, tag3",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 2,
            downloads = 2,
            likes = 2,
            comments = 2,
            user = "user2",
            userImageURL = "userImageURL"
        ),
        ImageItem(
            id = 3,
            type = "type3",
            tags = "tag3, tag4",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 3,
            downloads = 3,
            likes = 3,
            comments = 3,
            user = "user3",
            userImageURL = "userImageURL"
        ),
        ImageItem(
            id = 4,
            type = "type4",
            tags = "tag4, tag5",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 4,
            downloads = 4,
            likes = 4,
            comments = 4,
            user = "user4",
            userImageURL = "userImageURL"
        ),
        ImageItem(
            id = 5,
            type = "type5",
            tags = "tag5, tag6",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 5,
            downloads = 5,
            likes = 5,
            comments = 5,
            user = "user5",
            userImageURL = "userImageURL"
        ),
        ImageItem(
            id = 6,
            type = "type6",
            tags = "tag1, tag2",
            imageURL = "imageURL",
            fullHDURL = "fullHDURL",
            views = 6,
            downloads = 6,
            likes = 6,
            comments = 6,
            user = "user6",
            userImageURL = "userImageURL"
        ),
    )

}