package com.example.umc.model.request

data class PostDailyMealRequest (
    val userId: Int,
    val mealDate: String
)