package com.example.umc.UserApi

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = UserRepository.getAuthToken(context) // 저장된 accessToken 가져오기

        val newRequest = chain.request().newBuilder().apply {
            token?.let {
                Log.d("AuthInterceptor", "토큰이 존재합니다: $it")
                addHeader("Authorization", "Bearer $it") // 헤더에 accessToken 추가
                Log.d("AuthInterceptor", "Authorization 헤더에 추가된 토큰: Bearer $it")
            } ?: Log.e("AuthInterceptor", "토큰이 존재하지 않습니다")
        }.build()

        Log.d("AuthInterceptor", "Request URL: ${newRequest.url}")
        return chain.proceed(newRequest)
    }
}
