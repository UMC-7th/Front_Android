package com.example.umc.Quote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.model.RankingItem
import kotlinx.coroutines.launch

class PriceViewModel: ViewModel() {
    val hotMaterialList = MutableLiveData<List<RankingItem>>()

    fun fetchHotMaterialList() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.materialApiService.getMaterialRankAll()
                Log.d("QuoteViewModel", "API Response: ${response.body()}")
                if (response.isSuccessful) {
                    val data = response.body()?.success?.data ?: emptyList()

                    val mappedRankingItems = data.mapIndexed { index, item ->
                        RankingItem(
                            variety = item.variety.name,
                            name = item.name,
                            rank = (index + 1).toString(),
                            imgUrl = "https://example.com/default.jpg"
                        )
                    }

                    hotMaterialList.postValue(mappedRankingItems)
                } else {
                    Log.e("QuoteViewModel", "API Error: ${response.code()} - ${response.message()}")
                    hotMaterialList.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.e("QuoteViewModel", "API Exception: ${e.message}", e)
                hotMaterialList.postValue(emptyList())
            }
        }
    }
}