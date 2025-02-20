package com.example.umc.Subscribe.Repository

import android.content.Context
import com.example.umc.Subscribe.Retrofit.RetrofitClient
import com.example.umc.Subscribe.SubscribeRequest.DeliveryAddressRequest
import com.example.umc.Subscribe.SubscribeRequest.DeliveryAddressputRequest
import com.example.umc.Subscribe.SubscribeResponse.Get.DeliveryAddressGetresponse
import com.example.umc.Subscribe.SubscribeResponse.Post.DeliveryAddressresponse

import com.example.umc.UserApi.UserRepository
import com.example.umc.Subscribe.SubscribeResponse.Get.SuccessDeliveryResponse as SuccessDeliveryResponse1

class DeliveryAddressRepository(private val context: Context) {
    // api를 생성자에서 전달된 context를 통해 초기화
    private val api = RetrofitClient.getDeliveryAddressApi(context)

    // GET 관련 기능
    object Get {
        suspend fun getDeliveryAddresses(context: Context): Result<DeliveryAddressGetresponse> {
            val token = UserRepository.getAuthToken(context)
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }

            return try {
                val response = RetrofitClient.getDeliveryAddressApi(context).getDeliveryAddresses("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!! // 원본 응답 (List<DeliveryGetResponse>)

                    // 변환해서 DeliveryAddressGetresponse 형식으로 맞추기
                    val convertedResponse = DeliveryAddressGetresponse(
                        resultType = "SUCCESS",  // 성공 여부 지정
                        error = null,  // 실패가 없으면 null
                        success = SuccessDeliveryResponse1(result = responseBody) // 직접 responseBody를 전달
                    )

                    Result.success(convertedResponse)  // 🔹 DeliveryAddressGetresponse 객체 반환
                } else {
                    Result.failure(Exception("배송지 목록 조회 실패: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        suspend fun getDefaultDeliveryAddress(context: Context): Result<DeliveryAddressresponse> {
            val token = UserRepository.getAuthToken(context)
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }

            return try {
                val response = RetrofitClient.getDeliveryAddressApi(context).getDefaultDeliveryAddress("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("기본 배송지 조회 실패: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // POST 관련 기능
    object Post {
        suspend fun addDeliveryAddress(
            context: Context,
            request: DeliveryAddressRequest
        ): Result<DeliveryAddressresponse> {
            val token = UserRepository.getAuthToken(context)
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }

            return try {
                val response = RetrofitClient.getDeliveryAddressApi(context).addDeliveryAddress("Bearer $token", request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("배송지 추가 실패: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    object Put {
        suspend fun updateDeliveryAddress(
            context: Context,
            addressId: String?,
            request: DeliveryAddressputRequest
        ): Result<DeliveryAddressresponse> {
            val token = UserRepository.getAuthToken(context)
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }

            return try {
                // addressId를 URL 경로에 포함시켜 PUT 요청을 보냄
                val response = RetrofitClient.getDeliveryAddressApi(context).updateDeliveryAddress(
                    "Bearer $token", addressId, request
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("배송지 수정 실패: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


}
