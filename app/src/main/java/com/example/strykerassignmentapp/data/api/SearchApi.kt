package com.example.strykerassignmentapp.data.api

import com.example.strykerassignmentapp.data.response.SearchQuestionResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun searchQuestion(
        @Query("site") site: String = "stackoverflow",
        @Query("sort") sort: String = "activity",
        @Query("order") order: String = "desc",
        @Query("intitle") query: String,
    ): SearchQuestionResponseDto
}