package com.semba.pixabayimages.core.testing.data

import com.semba.pixabayimages.core.common.DataResponse
import com.semba.pixabayimages.core.common.ErrorCode
import com.semba.pixabayimages.core.testing.TestingUtils
import com.semba.pixabayimages.data.remote.datasource.NetworkDataSource
import com.semba.pixabayimages.data.remote.di.apiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    lateinit var mockWebServer: MockWebServer

    private val api by lazy {
        apiService(baseUrl = mockWebServer.url("/"), OkHttpClient())
    }

    private lateinit var remoteDataSource: NetworkDataSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        remoteDataSource = NetworkDataSource(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `get images with success result`() = runTest {
        val response = MockResponse()
            .setBody(TestingUtils.imagesJsonAsString())
            .setResponseCode(200)

        mockWebServer.enqueue(response)

        val result = remoteDataSource.fetchImagesWithQuery("", 1, 60)

        assert(result is DataResponse.Success && result.data?.size == 60)
    }

    @Test
    fun `get images with Bad Request Error`() = runTest {
        val response = MockResponse()
            .setBody("")
            .setResponseCode(400)

        mockWebServer.enqueue(response)

        val result = remoteDataSource.fetchImagesWithQuery("", 1, 60)

        assert(result is DataResponse.Failure && result.errorCode == ErrorCode.BAD_REQUEST)
    }

    @Test
    fun `get images with Bad Not Found Error`() = runTest {
        val response = MockResponse()
            .setBody("")
            .setResponseCode(404)

        mockWebServer.enqueue(response)

        val result = remoteDataSource.fetchImagesWithQuery("", 1, 60)

        assert(result is DataResponse.Failure && result.errorCode == ErrorCode.NOT_FOUND)
    }

}