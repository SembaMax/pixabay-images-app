package com.semba.feature.detailscreen

import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.testing.MainDispatcherRule
import com.semba.pixabayimages.core.testing.TestImagesRepository
import com.semba.pixabayimages.core.testing.TestingUtils
import com.semba.pixabayimages.data.model.search.network.toModel
import com.semba.pixabayimages.feature.detailscreen.DetailUiState
import com.semba.pixabayimages.feature.detailscreen.DetailViewModel
import com.semba.pixabayimages.feature.detailscreen.domain.GetImageItemUseCase
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: TestImagesRepository
    private lateinit var getImageItemUseCase: GetImageItemUseCase
    private lateinit var viewModel: DetailViewModel

    private val firstImageId = 2277L

    @Before
    fun setup() {
        repository = TestImagesRepository()
        getImageItemUseCase = GetImageItemUseCase(repository)
        viewModel = DetailViewModel(getImageItemUseCase)
    }

    @Test
    fun `state is initially loading`() = runTest {
        assertEquals(DetailUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `state is success on successful request`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val result = TestingUtils.imagesJsonAsItems()
        repository.setIsSuccessful(true)
        repository.setImageItems(result)

        viewModel.fetchImageItem(firstImageId)

        assert(viewModel.uiState.value is DetailUiState.Success)

        collectJob.cancel()
    }

    @Test
    fun `state is error on failure request`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        repository.setIsSuccessful(false)
        repository.setImageItems(emptyList())

        viewModel.fetchImageItem(firstImageId)

        assert(viewModel.uiState.value is DetailUiState.Error)

        collectJob.cancel()
    }

    @Test
    fun `emit correct data on successful request`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        val result = TestingUtils.imagesJsonAsItems()
        repository.setIsSuccessful(true)
        repository.setImageItems(result)

        viewModel.fetchImageItem(firstImageId)
        val expectedModel = result.firstOrNull{ it.id == firstImageId }?.toModel()

        assert(viewModel.uiState.value is DetailUiState.Success)
        assertEquals(expectedModel, (viewModel.uiState.value as DetailUiState.Success).imageItem)

        collectJob.cancel()
    }

    @Test
    fun `emit correct data on failure request`() = runTest {

        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        repository.setIsSuccessful(false, ErrorCode.DATABASE_ERROR)
        repository.setImageItems(emptyList())

        viewModel.fetchImageItem(firstImageId)

        assert(viewModel.uiState.value is DetailUiState.Error)
        assertEquals(ErrorCode.DATABASE_ERROR, (viewModel.uiState.value as DetailUiState.Error).errorCode)

        collectJob.cancel()
    }

}