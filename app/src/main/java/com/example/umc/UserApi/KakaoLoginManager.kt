package com.example.umc.UserApi

import android.content.Context
import android.provider.Settings
import com.example.umc.Signin.LoginResult
import com.example.umc.UserApi.APi.KakaoAuthService
import com.example.umc.UserApi.Request.KakaoAuthRequest
import com.example.umc.UserApi.Response.AuthResponse

// KakaoLoginManager는 카카오 로그인 프로세스를 관리하는 클래스입니다.
// 인증 URL 생성, 로그인 처리, 토큰 저장 등의 기능을 담당합니다.
class KakaoLoginManager(
    private val authService: KakaoAuthService,
    private val context: Context
) {
    companion object {
        // 카카오 개발자 콘솔에서 받은 네이티브 앱 키
        private const val KAKAO_NATIVE_APP_KEY = "3467c5d19149e86652d623f529cc95c1"
        // 서버의 카카오 로그인 콜백 처리 엔드포인트
        private const val REDIRECT_URI = "http://3.38.39.238:3000/auth/kakao/callback"
    }

    // 카카오 로그인 인증 페이지 URL을 생성합니다.
    fun getKakaoAuthUrl(): String {
        return "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=$KAKAO_NATIVE_APP_KEY" +
                "&redirect_uri=$REDIRECT_URI" +
                "&response_type=code"
    }

    // 사용자 인증 후 카카오가 리다이렉트할 URI를 반환합니다.
    private fun getRedirectUri(): String {
        return REDIRECT_URI
    }

    // 카카오 로그인 프로세스를 처리합니다.
    // 인증 코드를 받아 서버에 전송하고, 응답으로 받은 토큰을 저장합니다.
    suspend fun handleKakaoLogin(authCode: String): LoginResult<AuthResponse> {
        return try {
            // 기기의 고유 식별자를 가져옵니다.
            val deviceId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )

            val result = authService.loginWithKakao(
                KakaoAuthRequest(
                    authCode = authCode,
                    deviceId = deviceId
                )
            )

            if (result.isSuccessful) {
                result.body()?.let {
                    saveAuthTokens(it)
                    LoginResult.Success(it)
                } ?: LoginResult.Error(Exception("Empty response body"))
            } else {
                LoginResult.Error(Exception("Login failed: ${result.code()}"))
            }
        } catch (e: Exception) {
            LoginResult.Error(e)
        }
    }

    // 서버로부터 받은 인증 토큰을 SharedPreferences에 저장합니다.
    private fun saveAuthTokens(authResponse: AuthResponse) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("access_token", authResponse.accessToken)
            putString("refresh_token", authResponse.refreshToken)
            apply()
        }
    }
}