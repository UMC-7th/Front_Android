package com.example.umc.UserApi.Request

// 서버에 보낼 카카오 인증 정보
data class KakaoAuthRequest(
    val authCode: String,    // 카카오로부터 받은 인증 코드
    val deviceId: String?,     // 필요한 경우 디바이스 식별자
)