package com.example.strykerassignmentapp.data.response

import com.google.gson.annotations.SerializedName

data class QuestionOwner(
    @SerializedName("account_id") val accountId: Int,
    val reputation: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_type") val userType: String,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("display_name") val displayName: String,
    val link: String
)
