package com.example.strykerassignmentapp.data.response

import com.google.gson.annotations.SerializedName

data class QuestionItem(
    val tags: List<String>,
    val owner: QuestionOwner,
    @SerializedName("is_answered") val isAnswered: Boolean,
    @SerializedName("view_count") val viewCount: Int,
    @SerializedName("accepted_answer_id") val acceptedAnswerId: Long,
    @SerializedName("answer_count") val answerCount: Int,
    val score: Int,
    @SerializedName("last_activity_date") val lastActivityDate: Long,
    @SerializedName("creation_date") val creationDate: Long,
    @SerializedName("question_id") val questionId: Long,
    @SerializedName("content_license") val contentLicense: String,
    val link: String,
    val title: String
)
