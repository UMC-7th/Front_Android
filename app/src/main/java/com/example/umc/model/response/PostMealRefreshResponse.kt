package com.example.umc.model.response

data class PostMealRefreshResponse(
    val error: Any,
    val resultType: String,
    val success: MealRefreshSuccess
)

data class MealRefreshSuccess(
    val addedByUser: Boolean,
    val calorieDetail: String,
    val calorieTotal: Int,
    val difficulty: String,
    val food: String,
    val material: String,
    val mealId: Int,
    val price: Int,
    val recipe: String
)

data class MealRefreshError(
    val data: String,
    val errorCode: String,
    val reason: String
)