package com.example.strykerassignmentapp.network

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetworkViewModel(
    private val networkMonitor: NetworkMonitor
): ViewModel() {
    var bannerState by mutableStateOf(NetworkState.Hidden)
        private set

    private val disposables = CompositeDisposable()
    private var wasDisconnected = false

    init {
        observeNetworkChanges()
    }

    private fun observeNetworkChanges() {
        networkMonitor.observeNetwork()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .subscribe { state ->
                when (state) {
                    NetworkState.Disconnected -> {
                        wasDisconnected = true
                        bannerState = NetworkState.Disconnected
                    }

                    NetworkState.Connected -> {
                        if (wasDisconnected) {
                            bannerState = NetworkState.Connected
                            viewModelScope.launch {
                                delay(1000L)
                                bannerState = NetworkState.Hidden
                                wasDisconnected = false
                            }
                        } else {
                            bannerState = NetworkState.Hidden
                        }
                    }
                    else -> Unit
                }
            }
            .let(disposables::add)

        checkInitialNetworkState()
    }

    private fun checkInitialNetworkState() {
        val isOnline = networkMonitor.isCurrentlyConnected()
        if (!isOnline) {
            bannerState = NetworkState.Disconnected
            wasDisconnected = true
        }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}