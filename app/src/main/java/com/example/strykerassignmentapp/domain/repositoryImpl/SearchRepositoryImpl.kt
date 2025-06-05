package com.example.strykerassignmentapp.domain.repositoryImpl

import com.example.strykerassignmentapp.data.api.SearchApi
import com.example.strykerassignmentapp.data.response.QuestionItem
import com.example.strykerassignmentapp.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val searchApi: SearchApi
) : SearchRepository {

    override suspend fun searchQuestion(query: String): Result<List<QuestionItem>> {
        return try {
            val response = searchApi.searchQuestion(query = query)
            Result.success(response.items)
        }catch (ex: Exception){
            Result.failure(ex)
        }
    }

}