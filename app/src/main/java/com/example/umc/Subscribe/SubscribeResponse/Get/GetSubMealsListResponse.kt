package com.example.umc.Subscribe.SubscribeResponse.Get

data class GetSubMealsListResponse(
    val error: GetSubMealsListError,
    val resultType: String,
    val success: List<GetSubMealsListSuccess>
)

data class GetSubMealsListSuccess(
    val addedByUser: Boolean,
    val calorieDetail: String,
    val calorieTotal: Int,
    val difficulty: Int,
    val food: String,
    val material: String,
    val mealId: Int,
    val mealSubs: List<MealSub>,
    val price: Int,
    val recipe: String
)

data class MealSub(
    val categoryId: Int,
    val mealDate: String,
    val mealId: Int,
    val mealSubId: Int,
    val time: String
)

data class GetSubMealsListError(
    val `data`: String,
    val errorCode: String,
    val reason: String
)