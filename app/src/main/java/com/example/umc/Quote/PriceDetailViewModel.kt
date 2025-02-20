package com.example.umc.Quote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.model.response.GetMaterialVariety
import kotlinx.coroutines.launch

class PriceDetailViewModel: ViewModel() {
    val varietyMaterialList = MutableLiveData<List<FoodItem>>()

    fun fetchVarietyMaterialList(variety: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.materialApiService.getMaterialsVariety(variety)

                Log.d("PriceDetailViewModel", "API Response: ${response.body()}")
                if (response.isSuccessful) {
                    val data = response.body()?.success?.data ?: emptyList()
                    Log.d("PriceDetailViewModel", "API Success: Received ${data.size} items for variety: $variety")

                    val mappedFoodItems = data.map { item ->
                        FoodItem(
                            variety = item.variety.name,  // ✅ 품종명
                            name = item.name, // ✅ "/" 앞부분만 가져오기
                            price = "0",
                            unit = item.unit,
                            imageUrl = "", // ✅ 이미지 URL 추가 가능
                        )
                    }

                    varietyMaterialList.postValue(mappedFoodItems)
                } else {
                    Log.e("PriceDetailViewModel", "API Error: ${response.code()} - ${response.message()}")
                    varietyMaterialList.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.e("PriceDetailViewModel", "API Exception: ${e.message}", e)
                varietyMaterialList.postValue(emptyList())
            }
        }
    }
}