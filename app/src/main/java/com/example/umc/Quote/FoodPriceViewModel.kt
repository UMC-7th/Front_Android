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

    fun getKamisPriceData() {
        val call = RetrofitClient.kamisService.getDailySalesList(
            productNo = "212",
            regDay = "2025-02-20",
            certKey = "1177e9f8-8f03-45ec-9cef-318101246a8d",
            certId = "rmarkdalswn@naver.com"
        )

        call.enqueue(object : Callback<KamisPriceResponse> {
            override fun onResponse(call: Call<KamisPriceResponse>, response: Response<KamisPriceResponse>) {
                if (response.isSuccessful) {
                    // 응답이 성공적일 경우, 데이터를 처리
                    val priceData = response.body()
                    priceData?.let {
                        // 여기서 priceData를 사용하여 UI 업데이트 또는 추가 작업을 할 수 있습니다.
                        println(it)
                    }
                } else {
                    // 응답이 실패했을 경우 처리
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<KamisPriceResponse>, t: Throwable) {
                // 네트워크 요청 실패 시 처리
                println("Failed to connect: ${t.message}")
            }
        })
    }

    /*fun getMaterialMark(token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.materialApiService.getMaterialMark("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _isMark.postValue(body.isMark)
                    } ?: Log.e("MaterialMark", "응답 본문이 비어 있음")
                } else {
                    Log.e("MaterialMark", "즐겨찾기 조회 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MaterialMark", "오류 발생: ${e.message}")
            }
        }
    }*/
}
