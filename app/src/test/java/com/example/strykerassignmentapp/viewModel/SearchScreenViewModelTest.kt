package com.example.strykerassignmentapp.viewModel

import com.example.strykerassignmentapp.data.response.QuestionItem
import com.example.strykerassignmentapp.domain.usecase.SearchQuestionUseCase
import com.example.strykerassignmentapp.feature.SearchScreenViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: SearchQuestionUseCase
    private lateinit var viewModel: SearchScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = SearchScreenViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `show helper message for short queries`() = runTest {
        viewModel.onQueryChange("a")
        assertEquals("Two more characters required", viewModel.uiState.message)

        viewModel.onQueryChange("ab")
        assertEquals("One more character required", viewModel.uiState.message)
    }

    @Test
    fun `search on valid query`() = runTest {
        val resultList = listOf(mockk<QuestionItem>())
        coEvery { useCase("android") } returns Result.success(resultList)

        viewModel.onQueryChange("android")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(resultList, viewModel.uiState.results)
        assertFalse(viewModel.uiState.isLoading)
    }

    @Test
    fun `set error message on IOException`() = runTest {
        coEvery { useCase("offline") } returns Result.failure(IOException())

        viewModel.onQueryChange("offline")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("You're offline. Please check your internet connection.", viewModel.uiState.error)
    }

    @Test
    fun `retries when network reconnects`() = runTest {
        val resultList = listOf(mockk<QuestionItem>())
        coEvery { useCase("android") } returns Result.failure(IOException()) andThen Result.success(resultList)

        viewModel.onQueryChange("android")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.retryOnNetworkReconnect()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(resultList, viewModel.uiState.results)
    }
}