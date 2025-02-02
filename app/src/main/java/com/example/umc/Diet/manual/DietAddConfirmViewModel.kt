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

class DietAddConfirmViewModel : ViewModel() {
    private val _mealList = MutableLiveData<List<ManualMeals>>()
    val mealList: LiveData<List<ManualMeals>> get() = _mealList

    fun fetchManualMeals(userId: Int) {
        viewModelScope.launch {
            try {
                val response: Response<GetManualMealsResponse> = RetrofitClient.mealApiService.getManualMeals(userId)
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
