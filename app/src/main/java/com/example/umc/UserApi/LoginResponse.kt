package com.example.umc.UserApi

//login response 처리
data class LoginResponse(
    val token: String, // 로그인 성공 시 반환되는 토큰
    val userId: Int,   // 사용자 ID
    val message: String // 응답 메시지
)
