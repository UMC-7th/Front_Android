package com.example.umc.model.response

data class PostDailyMealResponse(
    val error: PostDailyMealError,
    val resultType: String,
    val success: List<PostDailyMealSuccess>
)

data class PostDailyMealSuccess(
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

data class PostDailyMealError(
    val `data`: String,
    val errorCode: String,
    val reason: String
)