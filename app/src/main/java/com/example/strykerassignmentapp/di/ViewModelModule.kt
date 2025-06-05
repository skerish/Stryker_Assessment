package com.example.strykerassignmentapp.di

import com.example.strykerassignmentapp.feature.SearchScreenViewModel
import com.example.strykerassignmentapp.network.NetworkViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchScreenViewModel(get()) }
    viewModel { NetworkViewModel(get()) }
}