package com.example.umc.UserApi

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val success: Boolean,          // 성공 여부
    val resultType: String?,       // 결과 타입
    val status: Int,
    val message: String,
    val user: UserProfileData
)

data class UserProfileData(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("name") val name: String,
    @SerializedName("birth") val birth: String,
    @SerializedName("email") val email: String,
    @SerializedName("phoneNum") val phoneNum: String  // phone -> phoneNum으로 통일
)
