package com.semba.pixabayimages.feature.searchscreen

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {

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