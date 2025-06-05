package com.example.strykerassignmentapp.domain.repository

import com.example.strykerassignmentapp.data.response.QuestionItem

interface SearchRepository {
    suspend fun searchQuestion(query: String): Result<List<QuestionItem>>
}