package com.example.strykerassignmentapp.domain.usecase

import com.example.strykerassignmentapp.data.response.QuestionItem
import com.example.strykerassignmentapp.domain.repository.SearchRepository

class SearchQuestionUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): Result<List<QuestionItem>>{
        return repository.searchQuestion(query)
    }
}