package com.example.umc.model.service


import com.example.umc.model.CartRequest
import com.example.umc.model.CartResponse
import com.example.umc.model.request.PostManualMealsRequest
import com.example.umc.model.response.GetManualMealsResponse
import com.example.umc.model.response.ManualMealsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MealApiService {
    @POST("api/v1/meals/manual")
    suspend fun addManualMeal(
        @Body request: PostManualMealsRequest
    ): Response<ManualMealsResponse>

    @GET("api/v1/meals/manual/list")
    suspend fun getManualMeals(
        @Query("userId") userId: Int
    ): Response<GetManualMealsResponse>

    @POST("/api/v1/subscribes/meals/cart")
    suspend fun addToCart(@Body request: CartRequest): Response<CartResponse>
}
