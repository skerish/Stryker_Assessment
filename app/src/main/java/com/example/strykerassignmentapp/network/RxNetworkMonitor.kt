package com.example.strykerassignmentapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.reactivex.rxjava3.core.Observable

class RxNetworkMonitor(private val context: Context): NetworkMonitor {

    override fun observeNetwork(): Observable<NetworkState> {
        return Observable.create<NetworkState>{ emitter ->
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    emitter.onNext(NetworkState.Connected)
                }

                override fun onLost(network: Network) {
                    emitter.onNext(NetworkState.Disconnected)
                }

                override fun onUnavailable() {
                    emitter.onNext(NetworkState.Disconnected)
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(request, callback)

            emitter.setCancellable {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    override fun isCurrentlyConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}