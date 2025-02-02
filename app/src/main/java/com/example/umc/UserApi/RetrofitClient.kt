package com.example.umc.UserApi

import com.example.umc.model.service.MealApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// swagger 연결 http
object RetrofitClient {
    private const val BASE_URL = "http://3.38.39.238:3000/"  // 여기에 API 서버 주소 입력

    val instance: UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환
            .build()
            .create(UserApi::class.java)
    }


    val mealApiService : MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }

    val getApiService : UserApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }
}