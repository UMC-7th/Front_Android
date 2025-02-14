package com.example.umc.model.repository

import com.example.umc.model.ManualMeals
import com.example.umc.model.request.PostManualMealsRequest
import com.example.umc.model.response.PostManualMealsResponse
import retrofit2.Response

interface ManualMealsRepository {
    suspend fun addManualMeal(request: PostManualMealsRequest): Response<PostManualMealsResponse>
    suspend fun getManualMeals(userId: Int): List<ManualMeals>
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorMessage: String) : Result<Nothing>()
}
