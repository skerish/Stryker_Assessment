package com.example.strykerassignmentapp.di

import com.example.strykerassignmentapp.domain.repository.SearchRepository
import com.example.strykerassignmentapp.domain.repositoryImpl.SearchRepositoryImpl
import com.example.strykerassignmentapp.domain.usecase.SearchQuestionUseCase
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single { SearchQuestionUseCase(get()) }
}