package com.example.umc.UserApi

import android.content.Context
import android.util.Log
import com.example.umc.model.service.MealApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://3.38.39.238:3000/"

    // 기본 로깅 인터셉터
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 인증 인터셉터 (컨텍스트 필요)
    private fun createAuthInterceptor(context: Context) = Interceptor { chain ->
        val originalRequest = chain.request()

        // 토큰 가져오기
        val token = UserRepository.getAuthToken(context)

        // 요청에 토큰 추가
        val modifiedRequest = token?.let {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()
        } ?: originalRequest

        // 로깅
        Log.d("AuthInterceptor", "요청 URL: ${modifiedRequest.url}")
        Log.d("AuthInterceptor", "요청 헤더: ${modifiedRequest.headers}")

        val startTime = System.currentTimeMillis()
        val response = chain.proceed(modifiedRequest)
        val endTime = System.currentTimeMillis()

        Log.d("AuthInterceptor", "요청 소요 시간: ${endTime - startTime}ms")
        Log.d("AuthInterceptor", "응답 코드: ${response.code}")

        response
    }

    // 기본 OkHttpClient 빌더
    private fun createOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // 컨텍스트와 함께 인증이 추가된 OkHttpClient 생성
    private fun createAuthOkHttpClient(context: Context) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(createAuthInterceptor(context))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // 기존 인스턴스들 유지
    val instance: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    val mealApiService: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }

    val getApiService: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    // 컨텍스트와 함께 사용할 수 있는 메서드 추가
    fun createUserApiWithAuth(context: Context): UserApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createAuthOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }
}