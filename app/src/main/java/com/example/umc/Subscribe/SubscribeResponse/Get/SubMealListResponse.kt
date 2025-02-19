// API 응답의 전체 구조를 나타내는 클래스
data class ApiResponse<T>(
    val resultType: String,       // "SUCCESS" 또는 "ERROR"
    val error: ErrorResponse?,    // 에러 발생 시 에러 정보
    val success: T?              // 성공 시 실제 데이터
)

// 에러 응답을 위한 클래스
data class ErrorResponse(
    val errorCode: String,
    val reason: String,
    val data: String
)

// 개별 식사 정보를 담는 클래스
data class SubMealList(
    val mealId: Int,
    val date: String?,     // nullable로 변경
    val week: String?,     // day 대신 week로 변경
    val food: String?,     // breakfast로 사용
    val lunch: String?,    // 점심 메뉴 추가
    val dinner: String?,   // 저녁 메뉴 추가
    val calorieTotal: Int,
    val material: String?,
    val calorieDetail: String?,
    val price: Int,
    val difficulty: Int,
    val recipe: String?,
    val addedByUser: Boolean,
    val mealSubs: List<MealSub>?
)

data class MealSub(
    val mealSubId: Int,
    val mealId: Int,
    val categoryId: Int,
    val time: String,
    val mealDate: String
)