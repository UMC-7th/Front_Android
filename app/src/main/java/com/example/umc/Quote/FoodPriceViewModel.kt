package com.example.umc.Quote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.model.request.DeleteMaterialMarkRequest
import com.example.umc.model.request.PostMaterialMarkRequest
import com.example.umc.model.response.KamisPriceResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodPriceViewModel : ViewModel() {
    private val _isMark = MutableLiveData<Boolean>()
    val isMark: LiveData<Boolean> get() = _isMark

    private val _priceData = MutableLiveData<KamisPriceResponse>()
    val priceData: LiveData<KamisPriceResponse> get() = _priceData

    fun postMaterialMark(request: PostMaterialMarkRequest, token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.materialApiService.postMaterialMark(request, "Bearer $token")
                if (response.isSuccessful) {
                    _isMark.postValue(true)
                } else {
                    Log.e("MaterialMark", "API 요청 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MaterialMark", "오류 발생: ${e.message}")
            }
        }
    }

    fun deleteMaterialMark(request: DeleteMaterialMarkRequest, token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.materialApiService.deleteMaterialMark(request, "Bearer $token")
                if (response.isSuccessful) {
                    _isMark.postValue(false)
                } else {
                    Log.e("MaterialMark", "즐겨찾기 삭제 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MaterialMark", "오류 발생: ${e.message}")
            }
        }
    }


    fun fetchRecentlyPriceTrendList(
        regDay: String = "2025-02-20",
        certKey: String = "1177e9f8-8f03-45ec-9cef-318101246a8d",
        certId: String = "rmarkdalswn@naver.com",
        returnType: String = "Json",
        productNo: String = "321"
    ) {
        Log.d("KamisAPI", "Fetching recently price trend list for product $productNo...")

        val call = RetrofitClient.kamisService.getDailySalesList(
            productNo = productNo,
            regDay = regDay,
            certKey = certKey,
            certId = certId
        )

        call.enqueue(object : Callback<KamisPriceResponse> {
            override fun onResponse(call: Call<KamisPriceResponse>, response: Response<KamisPriceResponse>) {
                if (response.isSuccessful) {
                    Log.d("KamisAPI", "Response received successfully: ${response.body()}")
                    _priceData.postValue(response.body())
                } else {
                    Log.e("KamisAPI", "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<KamisPriceResponse>, t: Throwable) {
                Log.e("KamisAPI", "Failed to connect: ${t.message}")
            }
        })
    }
}
