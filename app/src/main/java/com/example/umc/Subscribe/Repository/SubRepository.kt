import android.content.Context
import android.util.Log
import com.example.umc.Subscribe.SubcribeApi.DeliveryAddressApi
import com.example.umc.UserApi.UserRepository

class SubRepository(
    private val api: DeliveryAddressApi,
    private val context: Context
) {
    suspend fun getMealSubscriptions(category: String? = "맛있는 일상 음식"): Result<List<SubMealList>> {
        return try {
            val token = UserRepository.getAuthToken(context)
            Log.d("SubRepository", "토큰: $token")

            if (token.isEmpty()) {
                Log.e("SubRepository", "토큰이 비어있음")
                return Result.failure(Exception("인증 토큰이 없습니다. 다시 로그인해주세요."))
            }

            Log.d("SubRepository", "API 호출 시작 - 카테고리: $category")
            val response = api.getMealSubscriptions("Bearer $token", category)
            Log.d("SubRepository", "API 응답 코드: ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("SubRepository", "응답 바디: $body")

                when (body?.resultType) {
                    "SUCCESS" -> {
                        val meals = body.success ?: emptyList()
                        Log.d("SubRepository", "성공적으로 데이터 수신: ${meals.size}개의 식단")
                        Result.success(meals)
                    }
                    "ERROR" -> {
                        val errorMessage = body.error?.reason ?: "알 수 없는 오류가 발생했습니다."
                        Log.e("SubRepository", "API 에러: $errorMessage")
                        Result.failure(Exception(errorMessage))
                    }
                    else -> {
                        Log.e("SubRepository", "알 수 없는 응답 타입: ${body?.resultType}")
                        Result.failure(Exception("알 수 없는 응답 타입입니다."))
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("SubRepository", "API 호출 실패 - 코드: ${response.code()}, 에러: $errorBody")

                if (response.code() == 401) {
                    Result.failure(Exception("인증이 만료되었습니다. 다시 로그인해주세요."))
                } else {
                    Result.failure(Exception("서버 오류가 발생했습니다: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e("SubRepository", "네트워크 오류: ${e.message}", e)
            Result.failure(Exception("네트워크 오류가 발생했습니다: ${e.message}"))
        }
    }
}