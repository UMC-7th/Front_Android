package com.example.umc.Subscribe.SubcribeApi

import com.example.umc.Subscribe.SubscribeResponse.Get.GetSubMealsListResponse
import com.example.umc.Subscribe.SubscribeResponse.Post.DeliveryAddressresponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SubMealService {
    @GET("api/v1/subscribes/meals/list")
    suspend fun getSubMealsList(
        @Query("Category") category: String,
        @Header("Authorization") token: String
    ): Response<GetSubMealsListResponse>
}