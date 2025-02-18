package com.example.umc.Diet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.model.repository.ManualMealsRepositoryImpl
import com.example.umc.model.request.PatchFavoriteDeleteRequest
import com.example.umc.model.request.PatchFavoriteRequest
import com.example.umc.model.request.PatchMealsDislikeDeleteRequest
import com.example.umc.model.request.PatchMealsDislikeRequest
import com.example.umc.model.request.PatchPreferenceRequest
import com.example.umc.model.request.PostCompleteMealRequest
import com.example.umc.model.response.GetMealsDetailSuccess
import kotlinx.coroutines.launch

class DietDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ManualMealsRepositoryImpl(application.applicationContext)

    private val _mealDetail = MutableLiveData<GetMealsDetailSuccess>()
    val mealDetail: LiveData<GetMealsDetailSuccess> get() = _mealDetail



    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> get() = _isLiked

    private val _isDisliked = MutableLiveData<Boolean>()
    val isDisliked: LiveData<Boolean> get() = _isDisliked

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> get() = _isFavorited

    fun fetchMealDetail(mealId: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.mealApiService.getMealsDetail(mealId, "Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    _mealDetail.value = response.body()?.success

                } else {
                    Log.e("MealLogging", "API 요청 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }

    fun addToPreference(request: PatchPreferenceRequest, token: String) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다..")
                    return@launch
                }

                val response = RetrofitClient.mealApiService.patchPreferenceMeal(request, "Bearer $token")
                if (!response.isSuccessful) {
                    Log.e("MealLogging", "선호도 업데이트 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }

    fun addToDislike(request: PatchMealsDislikeRequest, token: String){
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다..")
                    return@launch
                }
                val response = RetrofitClient.mealApiService.patchMealsDislike(request, "Bearer $token")
                if (!response.isSuccessful) {
                    Log.e("MealLogging", "싫어요 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }

    fun deleteDislike(request: PatchMealsDislikeDeleteRequest, token: String){
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다..")
                    return@launch
                }
                val response = RetrofitClient.mealApiService.patchMealsDislikeDelete(request, "Bearer $token")
                if (!response.isSuccessful) {
                    Log.e("MealLogging", "싫어요 취소 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }

    fun addToFavorite(request: PatchFavoriteRequest, token: String) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다..")
                    return@launch
                }

                val response = RetrofitClient.mealApiService.patchFavoriteMeal(request, "Bearer $token")

                if (!response.isSuccessful) {
                    Log.e("MealLogging", "즐겨찾기 업데이트 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }

    fun deleteFavorite(request: PatchFavoriteDeleteRequest, token: String) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다..")
                    return@launch
                }

                val response = RetrofitClient.mealApiService.patchFavoriteDelete(request, "Bearer $token")
                if (!response.isSuccessful) {
                    Log.e("MealLogging", "즐겨찾기 업데이트 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }



    fun mealComplete(request: PostCompleteMealRequest, token: String) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다..")
                    return@launch
                }
                val response = RetrofitClient.mealApiService.postCompleteMeal(request, "Bearer $token")
                if (!response.isSuccessful) {
                    Log.e("MealLogging", "식단 완료 업데이트 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }

    fun getAuthToken(): String {
        var token = repository.getAuthToken()
        token = token.trim()
        Log.d("DietDetailViewModel", "불러온 토큰: $token")
        return token
    }
}
