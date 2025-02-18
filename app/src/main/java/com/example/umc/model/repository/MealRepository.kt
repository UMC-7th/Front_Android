package com.example.umc.model.repository

import android.content.Context
import android.util.Log
import com.example.umc.UserApi.UserRepository
import com.example.umc.model.request.PostDailyMealRequest
import com.example.umc.model.request.PostRefreshMealRequest
import com.example.umc.model.response.MealRefreshSuccess
import com.example.umc.model.response.PostDailyMealSuccess
import com.example.umc.model.response.PostDailyMealSuccessWrapper
import com.example.umc.model.service.MealApiService

class MealRepository(
    private val mealApiService: MealApiService,
    private val context: Context
) {
    suspend fun getDailyMeal(mealDate: String): Result<PostDailyMealSuccessWrapper> {
        return try {
            Log.e("ERROR_CHECK", "1. Repository - API 호출 시작")
            val token = UserRepository.getAuthToken(context)

            val request = PostDailyMealRequest(mealDate = mealDate)

            val response = mealApiService.getDailyMeal(
                dailyMealRequest = request,
                token = "Bearer $token"
            )

            Log.e("ERROR_CHECK", "2. Repository - 응답 수신: ${response.code()}")
            Log.e("ERROR_CHECK", "3. Repository - 응답 본문: ${response.body()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                when {
                    responseBody?.resultType == "SUCCESS" -> {
                        Log.e("ERROR_CHECK", "4. Repository - 성공: ${responseBody.success}")
                        Result.Success(responseBody.success ?: PostDailyMealSuccessWrapper())
                    }
                    responseBody?.error != null -> {
                        Log.e("ERROR_CHECK", "4. Repository - 에러: ${responseBody.error}")
                        Result.Error("서버 오류: ${responseBody.error.reason}")
                    }
                    else -> Result.Error("알 수 없는 응답 형식")
                }
            } else {
                Result.Error("API 호출 실패: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ERROR_CHECK", "Repository - 예외 발생", e)
            Result.Error("네트워크 오류: ${e.message}")
        }
    }

    suspend fun refreshMeal(
        mealDate: String,
        mealId: Int,
        time: String
    ): Result<MealRefreshSuccess> {
        return try {
            val token = UserRepository.getAuthToken(context)

            val request = PostRefreshMealRequest(
                mealDate = mealDate,
                mealId = mealId,
                time = time,
                userId = 1 // 우선 하드코딩. 추후 userId 저장/관리 로직 추가 필요
            )

            val response = mealApiService.refreshMeal(refreshRequest = request)

            if (response.isSuccessful) {
                val body = response.body()
                when (body?.resultType) {
                    "SUCCESS" -> Result.Success(body.success)
                    else -> Result.Error("Refresh failed")
                }
            } else {
                Result.Error("Network call failed")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }
}