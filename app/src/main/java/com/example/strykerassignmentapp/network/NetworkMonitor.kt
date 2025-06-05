package com.example.strykerassignmentapp.network

import io.reactivex.rxjava3.core.Observable

interface NetworkMonitor {
    fun observeNetwork(): Observable<NetworkState>
    fun isCurrentlyConnected(): Boolean
}