package com.example.strykerassignmentapp.di

import com.example.strykerassignmentapp.data.api.SearchApi
import com.example.strykerassignmentapp.network.NetworkMonitor
import com.example.strykerassignmentapp.network.RxNetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.stackexchange.com/2.3/"

val networkModule = module{
    single{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<SearchApi> { get<Retrofit>().create(SearchApi::class.java) }

    single<NetworkMonitor> { RxNetworkMonitor(androidContext()) }
}