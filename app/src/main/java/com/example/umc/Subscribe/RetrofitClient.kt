package com.example.umc.Subscribe

import com.example.umc.model.service.MealApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://3.38.39.238:3000"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NSwiZW1haWwiOiJ3bmRoa3MxNzFAbmF2ZXIuY29tIiwiaWF0IjoxNzM4NDkyNzQ0LCJleHAiOjE3Mzg0OTYzNDR9.NMOrjx28A3FpNPzP5NsGc3xhoFLtEkseP8BrjmpNJe0")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val mealApiService: MealApiService = retrofit.create(MealApiService::class.java)
}