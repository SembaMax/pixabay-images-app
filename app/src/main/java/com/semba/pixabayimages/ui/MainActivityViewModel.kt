package com.semba.pixabayimages.ui

import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

}

sealed interface MainActivityUiState {
    object Loading: MainActivityUiState
    object Success: MainActivityUiState
}