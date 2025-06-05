package com.example.strykerassignmentapp.feature

import com.example.strykerassignmentapp.data.response.QuestionItem

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val results: List<QuestionItem> = emptyList(),
    val message: String? = null
)
