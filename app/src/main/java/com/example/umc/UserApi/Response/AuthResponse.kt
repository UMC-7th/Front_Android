package com.example.umc.UserApi.Response

// 서버로부터 받을 응답
data class AuthResponse(
    val accessToken: String?,    // 서버에서 발급한 액세스 토큰
    val refreshToken: String?,    // 서버에서 발급한 리프레시 토큰
    val userId: String? = null         // 사용자 식별자
    // 기타 필요한 사용자 정보
)