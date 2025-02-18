package com.example.umc.model.response

data class GetMealsDetailResponse(
    val error: GetMealsDetailError,
    val resultType: String,
    val success: GetMealsDetailSuccess
)

data class GetMealsDetailSuccess(
    val addedByUser: Boolean,
    val calorieDetail: String,
    val calorieTotal: Int,
    val difficulty: Int,
    val food: String,
    val material: String,
    val mealId: Int,
    val price: Int,
    val recipe: String
)

data class GetMealsDetailError(
    val `data`: String,
    val errorCode: String,
    val reason: String
)