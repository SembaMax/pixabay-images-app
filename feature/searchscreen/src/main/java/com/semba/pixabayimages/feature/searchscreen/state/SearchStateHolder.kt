package com.semba.pixabayimages.feature.searchscreen.state

import androidx.compose.runtime.Stable
import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.model.search.ImageItem
import kotlinx.coroutines.flow.MutableStateFlow

sealed class ResultState {
    object Empty : ResultState()
    class Loading(val isLoading: Boolean): ResultState()
    class Success(val imageItems: List<ImageItem> = emptyList()): ResultState()
    class Error(val errorCode: ErrorCode?): ResultState()
}

data class SearchUiState(
    val currentPage: Int = 1,
    val imageItems: List<ImageItem> = emptyList(),
    val limitReached: Boolean = false,
    val isLoading: Boolean = false,
    val currentClickedImage: ImageItem = ImageItem.empty(),
    val errorMsg: String? = null
)

fun MutableStateFlow<SearchUiState>.reset() {
    this.value = SearchUiState()
}

@Stable
interface TopBarState {
    val offset: Float
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
}

data class ScaleAndAlphaArgs(
    val fromScale: Float,
    val toScale: Float,
    val fromAlpha: Float,
    val toAlpha: Float
)

enum class TransitionState { PLACING, PLACED }