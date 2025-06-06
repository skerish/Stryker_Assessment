package com.example.strykerassignmentapp.viewModel

import com.example.strykerassignmentapp.network.NetworkMonitor
import com.example.strykerassignmentapp.network.NetworkState
import com.example.strykerassignmentapp.network.NetworkViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkViewModelTest {

    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var viewModel: NetworkViewModel
    private val networkSubject = PublishSubject.create<NetworkState>()

    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setUp() {

        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        networkMonitor = mockk(relaxed = true)
        every { networkMonitor.observeNetwork() } returns networkSubject
        every { networkMonitor.isCurrentlyConnected() } returns true

        viewModel = NetworkViewModel(networkMonitor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `show disconnected state when offline`() = runTest(testDispatcher){
        networkSubject.onNext(NetworkState.Disconnected)
        advanceUntilIdle()
        assertEquals(NetworkState.Disconnected, viewModel.bannerState)
    }

    @Test
    fun `show connected banner after reconnect and hides after delay`() = runTest(testDispatcher) {
        every { networkMonitor.isCurrentlyConnected() } returns false
        viewModel = NetworkViewModel(networkMonitor)

        advanceUntilIdle()
        assertEquals(NetworkState.Disconnected, viewModel.bannerState)

        networkSubject.onNext(NetworkState.Connected)
        advanceTimeBy(100L)
        assertEquals(NetworkState.Connected, viewModel.bannerState)

        advanceTimeBy(1000L)
        assertEquals(NetworkState.Hidden, viewModel.bannerState)
    }

    @Test
    fun `no banner if there was no disconnection on start`() = runTest(testDispatcher) {
        networkSubject.onNext(NetworkState.Connected)
        assertEquals(NetworkState.Hidden, viewModel.bannerState)
    }
}