/*
package com.example.umc.Diet.manual

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.model.ManualMeals
import com.example.umc.model.response.GetManualMealsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class DietAddConfirmViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _mealList = MutableLiveData<List<ManualMeals>>()
    val mealList: LiveData<List<ManualMeals>> get() = _mealList

    // SharedPreferences에서 token 가져오기
    private fun getAuthToken(): String {
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    fun fetchManualMeals(userId: Int) {
        viewModelScope.launch {
            try {
                // token을 가져와서 Authorization 헤더에 추가
                val token = getAuthToken()
                if (token.isEmpty()) {
                    Log.e("MealLogging", "토큰이 없습니다. 다시 로그인해주세요.")
                    return@launch
                }

                // API 요청에 Authorization 헤더 추가
                val response: Response<GetManualMealsResponse> = RetrofitClient.mealApiService.getManualMeals(userId, token)

                if (response.isSuccessful && response.body() != null) {
                    val successList = response.body()?.success ?: emptyList()
                    val mealList = successList.map { success ->
                        ManualMeals(
                            calorieTotal = success.calorieTotal,
                            foods = success.food.split(", ").map { it.trim() },
                            time = "",
                            mealDate = ""
                        )
                    }
                    _mealList.value = mealList
                } else {
                    Log.e("MealLogging", "API 요청 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MealLogging", "오류 발생: ${e.message}")
            }
        }
    }
}
*/
