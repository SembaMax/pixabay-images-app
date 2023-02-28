package com.semba.feature.detailscreen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.design.theme.PixabayImagesTheme
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.model.search.TestTags
import com.semba.pixabayimages.feature.detailscreen.DetailScreen
import com.semba.pixabayimages.feature.detailscreen.DetailUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var fakeUiState: DetailUiState

    @Before
    fun init() {
        fakeUiState = DetailUiState.Loading
    }

    @Test
    fun testLoadingUIState() {
        fakeUiState = DetailUiState.Loading

        composeTestRule.setContent {
            PixabayImagesTheme() {
                DetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testFailureUIState() {
        fakeUiState = DetailUiState.Error(ErrorCode.DATABASE_ERROR)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                DetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testSuccessUIState() {
        fakeUiState = DetailUiState.Success(imageItem)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                DetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.LOADING_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun testDetailScreenDisplaysCorrectData() {
        fakeUiState = DetailUiState.Success(imageItem)

        composeTestRule.setContent {
            PixabayImagesTheme() {
                DetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithText("user1").assertIsDisplayed()
        composeTestRule.onNodeWithText("tag1").assertIsDisplayed()
        composeTestRule.onNodeWithText("tag2").assertIsDisplayed()
        composeTestRule.onNodeWithText("11").assertIsDisplayed()
        composeTestRule.onNodeWithText("31000").assertIsDisplayed()
        composeTestRule.onNodeWithText("1000").assertIsDisplayed()
        composeTestRule.onNodeWithText("20").assertIsDisplayed()
    }

    @Test
    fun testBackButtonDisplayedAndClickable() {

        composeTestRule.setContent {
            PixabayImagesTheme() {
                DetailScreen(uiState = fakeUiState)
            }
        }

        composeTestRule.onNodeWithContentDescription(TestTags.DETAIL_BACK_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(TestTags.DETAIL_BACK_TEST_TAG).assertHasClickAction()
    }

    private val imageItem = ImageItem(
        id = 1,
        type = "type1",
        tags = "tag1, tag2",
        imageURL = "imageURL",
        fullHDURL = "fullHDURL",
        views = 1000,
        downloads = 20,
        likes = 31000,
        comments = 11,
        user = "user1",
        userImageURL = "userImageURL"
    )
}