package com.example.umc.model.response

data class GetManualMealsResponse(
    val resultType: String,
    val error: ErrorDetails?,
    val success: List<Success>?
)

data class ErrorDetails(
    val errorCode: String,
    val reason: String,
    val data: String
)

data class Success(
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
