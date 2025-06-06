package com.example.strykerassignmentapp.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strykerassignmentapp.domain.usecase.SearchQuestionUseCase
import kotlinx.coroutines.launch
import java.io.IOException

class SearchScreenViewModel(
    private val searchQuestionsUseCase: SearchQuestionUseCase
): ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    private var lastQuery: String? = null
    private var retryOnNetworkReconnect: Boolean = false

    private fun getMessageForQuery(query: String): String? {
        return when (query.length) {
            0 -> "Enter three or more characters"
            1 -> "Two more characters required"
            2 -> "One more character required"
            else -> null
        }
    }

    fun onQueryChange(newQuery: String) {
        val message = getMessageForQuery(newQuery.trim())
        uiState = if (newQuery.length < 3) {
            uiState.copy(
                query = newQuery,
                message = message,
                results = emptyList(),
                isLoading = false,
                error = null
            )
        } else {
            uiState.copy(
                query = newQuery,
                message = message
            )
        }

        if (newQuery.length >= 3) {
            search(newQuery)
        }
    }

    private fun search(query: String) {

        if (!query.matches(Regex(".*[a-zA-Z0-9]+.*"))) {
            uiState = uiState.copy(
                isLoading = false,
                error = null,
                results = emptyList(),
                message = "Please enter a valid search query"
            )
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null, results = emptyList())

            lastQuery = query
            retryOnNetworkReconnect = false

            val result = searchQuestionsUseCase(query)

            uiState = if (result.isSuccess){
                val results = result.getOrNull().orEmpty()
                if (results.isEmpty()){
                    uiState.copy(
                        isLoading = false,
                        results = emptyList(),
                        message = "No result found!"
                    )
                }else {
                    uiState.copy(
                        isLoading = false,
                        results = results,
                        message = null
                    )
                }
            }else{
                val exception = result.exceptionOrNull()
                val message = when {
                    exception is IOException -> {
                        retryOnNetworkReconnect = true
                        "You're offline. Please check your internet connection."
                    }
                    else -> exception?.localizedMessage ?: "Something went wrong."
                }

                uiState.copy(
                    isLoading = false,
                    error = message,
                    results = emptyList()
                )
            }
        }
    }

    fun retryOnNetworkReconnect() {
        if (retryOnNetworkReconnect && (lastQuery?.length ?: 0) >= 3) {
            lastQuery?.let { search(it) }
        }
    }
}