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
            Log.d("SubRepository", "API 호출 시작 - 카테고리: $category, 토큰: $token")

            if (token.isEmpty()) {
                return Result.failure(Exception("인증 토큰이 없습니다."))
            }

            val response = api.getMealSubscriptions("Bearer $token", category)

            if (response.isSuccessful) {
                val body = response.body()
                when (body?.resultType) {
                    "SUCCESS" -> Result.success(body.success ?: emptyList())
                    else -> Result.failure(Exception(body?.error?.reason ?: "알 수 없는 오류"))
                }
            } else {
                Result.failure(Exception("서버 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SubRepository", "네트워크 오류", e)
            Result.failure(e)
        }
    }
}