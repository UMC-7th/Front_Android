package com.example.umc.model.service

import com.example.umc.model.CartRequest
import com.example.umc.model.CartResponse
import com.example.umc.model.request.MealRefreshRequest
import com.example.umc.model.request.PatchFavoriteRequest
import com.example.umc.model.request.PatchPreferenceRequest
import com.example.umc.model.request.PostCompleteMealRequest
import com.example.umc.model.request.PostDailyMealRequest
import com.example.umc.model.request.PostManualMealsRequest
import com.example.umc.model.request.PostRefreshMealRequest
import com.example.umc.model.response.GetManualMealsResponse
import com.example.umc.model.response.MealRefreshResponse
import com.example.umc.model.response.PostManualMealsResponse
import com.example.umc.model.response.PatchFavoriteResponse
import com.example.umc.model.response.PatchPreferenceResponse
import com.example.umc.model.response.PostCompleteMealResponse
import com.example.umc.model.response.PostDailyMealResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MealApiService {
    @POST("api/v1/meals/manual")
    suspend fun addManualMeal(
        @Body request: PostManualMealsRequest,
        @Header("Authorization") token: String
    ): Response<PostManualMealsResponse>

    @GET("api/v1/meals/manual/list")
    suspend fun getManualMeals(
        @Query("userId") userId: Int,
        @Header("Authorization") token: String
    ): Response<GetManualMealsResponse>

    @POST("api/v1/meals/complete")
    suspend fun completeMeal(
        @Body body: PostCompleteMealRequest
    ): Response<PostCompleteMealResponse>

    @PATCH("api/v1/meals/favorite")
    suspend fun favoriteMeal(
        @Body favoriteRequest: PatchFavoriteRequest
    ): Response<PatchFavoriteResponse>

    @PATCH("api/v1/meals/preference")
    suspend fun preferenceMeal(
        @Body preferenceRequest: PatchPreferenceRequest
    ): Response<PatchPreferenceResponse>

    @POST("/api/v1/meals/refresh")
    suspend fun refreshMeal(
        @Body request: MealRefreshRequest
    ): MealRefreshResponse

    @POST("api/v1/meals/daily")
    suspend fun getDailyMeal(
        @Body dailyMealRequest: PostDailyMealRequest,
        @Header("Authorization") token: String
    ): Response<PostDailyMealResponse>

    @POST("/api/v1/subscribes/meals/cart")
    suspend fun addToCart(@Body request: CartRequest): Response<CartResponse>
}