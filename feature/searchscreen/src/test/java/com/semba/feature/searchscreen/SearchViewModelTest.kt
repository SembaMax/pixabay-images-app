package com.semba.feature.searchscreen

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.common.errorMessage
import com.semba.pixabayimages.core.testing.MainDispatcherRule
import com.semba.pixabayimages.core.testing.TestImagesRepository
import com.semba.pixabayimages.core.testing.TestingUtils
import com.semba.pixabayimages.data.model.search.ImageItem
import com.semba.pixabayimages.data.model.search.network.toModel
import com.semba.pixabayimages.feature.searchscreen.SearchViewModel
import com.semba.pixabayimages.feature.searchscreen.domain.SearchUseCase
import com.semba.pixabayimages.feature.searchscreen.state.SearchUiState
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: TestImagesRepository
    private lateinit var searchUseCase: SearchUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        repository = TestImagesRepository()
        searchUseCase = SearchUseCase(repository)
        viewModel = SearchViewModel(searchUseCase)
    }

    @Test
    fun `fetching the correct page index`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        repository.setIsSuccessful(true)
        repository.setImageItems(emptyList())

        viewModel.loadNextPage()

        assertEquals(viewModel.uiState.value.currentPage, 2)

        collectJob.cancel()
    }

    @Test
    fun `state is error on failure request`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        repository.setIsSuccessful(false)

        viewModel.onSearchClick()

        assertEquals(viewModel.uiState.value.errorMsg, ErrorCode.UNSPECIFIED.errorMessage())

        collectJob.cancel()
    }

    @Test
    fun `limit is reached on 404 errors`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        repository.setIsSuccessful(false, ErrorCode.NOT_FOUND)

        viewModel.onSearchClick()

        assertEquals(viewModel.uiState.value.errorMsg, ErrorCode.NOT_FOUND.errorMessage())
        assertEquals(viewModel.uiState.value.limitReached, true)

        collectJob.cancel()
    }

    @Test
    fun `limit is reached on empty response`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        repository.setIsSuccessful(true)
        repository.setImageItems(emptyList())

        viewModel.onSearchClick()

        assertEquals(viewModel.uiState.value.errorMsg, null)
        assertEquals(viewModel.uiState.value.limitReached, true)

        collectJob.cancel()
    }

    @Test
    fun `emit correct data on successful request`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val result = TestingUtils.imagesJsonAsItems()
        repository.setIsSuccessful(true)
        repository.setImageItems(result)

        viewModel.onSearchClick()

        assertEquals(result.map { it.toModel() }, viewModel.uiState.value.imageItems)

        collectJob.cancel()
    }

    @Test
    fun `show confirmation dialog state`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val imageItem = mockk<ImageItem>()
        viewModel.showConfirmationDialog(imageItem)

        assertEquals(viewModel.uiState.value.currentClickedImage, imageItem)
        assertEquals(viewModel.uiState.value.showDialog, true)

        collectJob.cancel()
    }

    @Test
    fun `dismiss confirmation dialog state`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val imageItem = mockk<ImageItem>()
        viewModel.showConfirmationDialog(imageItem)
        viewModel.dismissConfirmationDialog()

        assertEquals(viewModel.uiState.value.currentClickedImage, ImageItem.empty())
        assertEquals(viewModel.uiState.value.showDialog, false)

        collectJob.cancel()
    }

}