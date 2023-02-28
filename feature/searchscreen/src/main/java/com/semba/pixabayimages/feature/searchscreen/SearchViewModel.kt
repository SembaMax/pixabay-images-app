package com.semba.pixabayimages.feature.searchscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.common.errorMessage
import com.semba.pixabayimages.core.design.navigation.*
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.feature.searchscreen.domain.SearchScreenContract
import com.semba.pixabayimages.feature.searchscreen.domain.SearchUseCase
import com.semba.pixabayimages.feature.searchscreen.state.ResultState
import com.semba.pixabayimages.feature.searchscreen.state.SearchUiState
import com.semba.pixabayimages.feature.searchscreen.state.reset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase: SearchUseCase) : ViewModel(),
    SearchScreenContract {

    private val _uiState = MutableStateFlow(SearchUiState())

    val uiState =_uiState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SearchUiState()
    )

    private val _queryState = MutableStateFlow("fruits")
    val queryState = _queryState.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        "fruits"
    )

    init {
        searchQuery(queryState.value, _uiState.value.currentPage)
    }

    override fun onSearchClick() {
        _uiState.reset()
        searchQuery(queryState.value, _uiState.value.currentPage)
    }

    override fun loadNextPage() {
        increasePage()
        searchQuery(queryState.value, _uiState.value.currentPage)
    }

    private fun searchQuery(query: String, page: Int) {
        searchUseCase(query, page)
            .onEach { result ->
                when(result)
                {
                    is ResultState.Success -> {
                        checkIfLimitReached(result.imageItems)
                        _uiState.value = _uiState.value.copy(imageItems = _uiState.value.imageItems + result.imageItems)
                    }
                    is ResultState.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(errorMsg = result.errorCode?.errorMessage())
                        checkIfLimitReached(result.errorCode)
                    }
                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    override fun showConfirmationDialog(imageItem: ImageItem) {
        _uiState.value = _uiState.value.copy(currentClickedImage = imageItem, showDialog = true)
    }

    override fun dismissConfirmationDialog() {
        _uiState.value = _uiState.value.copy(currentClickedImage = ImageItem.empty(), showDialog = false)
    }

    override fun updateQuery(query: String) {
        _queryState.value = query
    }

    private fun increasePage() {
        _uiState.value = _uiState.value.copy(currentPage = getNextPage(_uiState.value.currentPage))
    }

    private fun checkIfLimitReached(errorCode: ErrorCode?) {
        val isLimitReached = errorCode == ErrorCode.NOT_FOUND
        _uiState.value = _uiState.value.copy(limitReached = isLimitReached)
    }

    private fun checkIfLimitReached(items: List<ImageItem>) {
        val isLimitReached = items.isEmpty()
        _uiState.value = _uiState.value.copy(limitReached = isLimitReached)
    }

    private fun getNextPage(page: Int): Int = page + 1
}

fun ImageItem.toArgs(): Map<String,String> = mapOf(
    IMAGE_ID_ARG to this.id.toString()
)