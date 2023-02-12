package com.semba.pixabayimages.feature.searchscreen.domain

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.data.repository.SearchRepository
import com.semba.pixabayimages.feature.searchscreen.state.ResultState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: SearchRepository) {

    operator fun invoke(query: String, page: Int): Flow<ResultState> =
        repository.loadImages(getQueryParameter(query), page)
            .map {
                if (it.errorCode != null)
                    ResultState.Error(it.errorCode)
                else {
                    ResultState.Success(it.data ?: emptyList())
                }
            }
            .catch { emit(ResultState.Error(ErrorCode.UNSPECIFIED)) }
            .onStart { emit(ResultState.Loading(true)) }
            .onCompletion { emit(ResultState.Loading(false)) }

    private fun getQueryParameter(query: String): String {
        return query.trim().replace(" ", "+")
    }
}