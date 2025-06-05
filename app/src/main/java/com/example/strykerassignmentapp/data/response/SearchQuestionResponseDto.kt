package com.example.strykerassignmentapp.data.response

import com.google.gson.annotations.SerializedName

data class SearchQuestionResponseDto(
    val items: List<QuestionItem>,
    @SerializedName("has_more") val hasMore: Boolean,
    @SerializedName("quota_max") val quotaMax: Int,
    @SerializedName("quota_remaining") val quotaRemaining: Int
)
