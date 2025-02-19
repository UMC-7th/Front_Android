package com.example.umc.Subscribe.SubcribeApi

import ApiResponse
import SubMealList
import com.example.umc.Subscribe.SubscribeRequest.DeliveryAddressRequest
import com.example.umc.Subscribe.SubscribeRequest.DeliveryAddressputRequest
import com.example.umc.Subscribe.SubscribeResponse.Get.CalendarResponse
import com.example.umc.Subscribe.SubscribeResponse.Get.DeliveryAddressGetresponse
import com.example.umc.Subscribe.SubscribeResponse.Patch.DeliveryAddressPatchresponse
import com.example.umc.Subscribe.SubscribeResponse.Post.DeliveryAddressresponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DeliveryAddressApi {
    @GET("api/v1/deliveryAddress")
    suspend fun getDeliveryAddresses(
        @Header("Authorization") token: String
    ): Response<List<DeliveryAddressresponse>>

    @POST("api/v1/deliveryAddress")
    suspend fun addDeliveryAddress(
        @Header("Authorization") token: String,
        @Body request: DeliveryAddressRequest
    ): Response<DeliveryAddressresponse>

    @PUT("api/v1/deliveryAddress/")
    suspend fun updateDeliveryAddress(
        @Header("Authorization") token: String,
        @Path("addressId") addressId: String?,  // addressId를 URL 경로에 포함시킴
        @Body request: DeliveryAddressputRequest
    ): Response<DeliveryAddressresponse>

    @PATCH("api/v1/deliveryAddress/default")
    suspend fun setDefaultDeliveryAddress(
        @Header("Authorization") token: String,
        @Body request: DefaultDeliveryAddressRequest // ✅ Body로 전달
    ): Response<DeliveryAddressresponse>

    @GET("api/v1/deliveryAddress/default")
    suspend fun getDefaultDeliveryAddress(
        @Header("Authorization") token: String
    ): Response<DeliveryAddressresponse>

    @GET("/api/v1/subscribes/meals/list")
    suspend fun getMealSubscriptions(
        @Header("Authorization") token: String,
        @Query("category") category: String? = "맛있는 일상 음식" // 기본값 설정
    ): Response<ApiResponse<List<SubMealList>>>


//    @GET("/api/v1/subscribes/meals/list")
//    suspend fun getDefaultDeliveryAddress(
//        @Header("Authorization") token: String
//    ): Response<DeliveryAddressresponse>


}