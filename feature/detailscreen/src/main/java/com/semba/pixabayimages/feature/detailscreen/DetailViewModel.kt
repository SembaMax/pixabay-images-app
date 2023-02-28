package com.semba.pixabayimages.feature.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.feature.detailscreen.domain.GetImageItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val getImageItemUseCase: GetImageItemUseCase): ViewModel() {

    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState.Loading)

    val uiState =_uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        DetailUiState.Loading
    )

    fun fetchImageItem(imageId: Long) {
        getImageItemUseCase(imageId)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }
}

fun String.splitTagsString(): List<String> = this.split(", ")

sealed interface DetailUiState {
    object Loading: DetailUiState
    data class Success(val imageItem: ImageItem): DetailUiState
    data class Error(val errorCode: ErrorCode?): DetailUiState
}