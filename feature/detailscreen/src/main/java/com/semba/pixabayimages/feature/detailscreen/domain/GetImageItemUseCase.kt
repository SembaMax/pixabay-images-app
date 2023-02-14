package com.semba.pixabayimages.feature.detailscreen.domain

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.repository.SearchRepository
import com.semba.pixabayimages.feature.detailscreen.DetailUiState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetImageItemUseCase @Inject constructor(private val repository: SearchRepository) {

    operator fun invoke(imageId: Long): Flow<DetailUiState> =
        repository.loadImageItem(imageId)
            .map {
                if (it.errorCode != null)
                    DetailUiState.Error(it.errorCode)
                else {
                    DetailUiState.Success(it.data ?: ImageItem.empty())
                }
            }
            .catch { emit(DetailUiState.Error(ErrorCode.UNSPECIFIED)) }
            .onStart { emit(DetailUiState.Loading) }
}