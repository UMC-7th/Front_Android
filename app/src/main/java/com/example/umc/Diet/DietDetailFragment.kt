package com.example.umc.Diet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.umc.model.Nutrition
import com.example.umc.R
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.databinding.FragmentDietDetailBinding
import com.example.umc.model.request.PatchFavoriteRequest
import com.example.umc.model.request.PatchPreferenceRequest
import com.example.umc.model.request.PostCompleteMealRequest
import com.example.umc.model.response.PostCompleteMealResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DietDetailFragment : Fragment() {
    private var _binding: FragmentDietDetailBinding? = null
    private val binding get() = _binding!!

    private var isLiked = false  // 좋아요
    private var isDisliked = false // 싫어요
    private var isFavorited = false // 즐겨찾기
    private var isCompleted = false // 식단 완료
    private var isTooltipVisible = false // 툴팁
    private var popupWindow: PopupWindow? = null // 툴팁 열기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 설정
        setupButtons()
        loadMealImage("제육볶음 도시락")

        val name = arguments?.getString("name")
        val calories = arguments?.getString("calories")

        binding.tvRecipeTitle.text = name
        binding.tvCalories.text = calories

        if (!name.isNullOrEmpty()) {
            loadMealImage(name)
        }

        // 더미 데이터
        val nutritionList = listOf(
            Nutrition("계란", "70"),
            Nutrition("식빵", "80"),
            Nutrition("바나나", "100"),
            Nutrition("버터", "35"),
            Nutrition("올리브유", "45")
        )

        val recipeSteps = listOf(
            "식빵을 토스터기나 후라이팬에 약불로 데워주세요.",
            "후라이팬에 약간의 기름을 두른 후 계란을 올려주세요.",
            "바나나와 함께 토스트를 섭취"
        )

        // 열량 테이블
        binding.recyclerNutrition.layoutManager = LinearLayoutManager(context)
        binding.recyclerNutrition.adapter = DietDetailAdapter(nutritionList)

        // 필요 식재료 및 레시피 출력
        binding.tvIngredients.text = nutritionList.mapIndexed { index, nutrition ->
            "${index + 1}. ${nutrition.name}"
        }.joinToString("\n")

        binding.tvRecipe.text = recipeSteps.mapIndexed { index, step ->
            "${index + 1}. $step"
        }.joinToString("\n")
    }

    private fun setupButtons() {
        // 좋아요 버튼 설정
        binding.btLike.setOnClickListener {
            isLiked = !isLiked
            binding.btLike.setColorFilter(ContextCompat.getColor(requireContext(), if (isLiked) R.color.Primary_Orange1 else R.color.Gray7))

            // 싫어요가 눌린 상태라면 해제
            if (isLiked && isDisliked) {
                isDisliked = false
                binding.btDislike.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Gray7))
            }

            // 좋아요 상태가 true이면 선호도 API 요청
            if (isLiked) {
                // val mealId = getMealId()
                // val userId = getUserId()
                val preferenceRequest = PatchPreferenceRequest(userId = 1, mealId = 16)
                addToPreference(preferenceRequest)
            }
        }

        binding.btDislike.setOnClickListener {
            isDisliked = !isDisliked
            binding.btDislike.setColorFilter(ContextCompat.getColor(requireContext(), if (isDisliked) R.color.Primary_Orange1 else R.color.Gray7))

            if (isDisliked && isLiked) {
                isLiked = false
                binding.btLike.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Gray7))
            }
        }

        // 즐겨찾기 버튼 설정
        binding.btFavorite.setOnClickListener {
            isFavorited = !isFavorited
            binding.btFavorite.setColorFilter(ContextCompat.getColor(requireContext(), if (isFavorited) R.color.Primary_Orange1 else R.color.Gray7))

            if (isFavorited) {
                val favoriteRequest = PatchFavoriteRequest(userId = 1, mealId = 16)
                addToFavorite(favoriteRequest)
            }
        }

        // 식단 완료 버튼 설정
        binding.btDietComplete.setOnClickListener {
            isCompleted = !isCompleted
            binding.btDietComplete.setBackgroundColor(ContextCompat.getColor(requireContext(), if (isCompleted) R.color.Primary_Orange1 else R.color.Gray7))

            // API 요청
            if (isCompleted) {
                val completedRequest = PostCompleteMealRequest(userId = 1, mealId = 1, mealDate = "2025-02-01T00:00:00.000Z" )  // Replace with actual userId and mealId
                mealComplete(completedRequest)  // Fixed function call
            }
        }

        binding.btQuestion.setOnClickListener {
            if (popupWindow == null) {
                val tooltipView = layoutInflater.inflate(R.layout.dialog_tooltip, null)
                popupWindow = PopupWindow(tooltipView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    isOutsideTouchable = true // 다른 곳을 클릭하면 닫히도록 설정
                    setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent)) // 배경 투명 설정
                }
            }
            if (isTooltipVisible) {
                popupWindow?.dismiss()
            } else {
                popupWindow?.showAsDropDown(binding.btQuestion, -80, 0)
            }
            isTooltipVisible = !isTooltipVisible
        }
        popupWindow?.setTouchInterceptor { v, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                popupWindow?.dismiss()
                isTooltipVisible = false
                v.performClick() // 클릭 이벤트 호출
                true
            } else {
                false
            }
        }
    }

    private fun addToFavorite(favoriteRequest: PatchFavoriteRequest) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.mealApiService.favoriteMeal(favoriteRequest)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.resultType == "SUCCESS") {
                        // 성공 처리
                        Log.d("MealLogging", "식단을 즐겨찾기 목록에 추가 성공")
                        Toast.makeText(context, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 실패 처리
                        Log.e("MealLogging", "즐겨찾기 추가 실패: ${responseBody?.error?.reason}")
                        Toast.makeText(context, "즐겨찾기 추가 실패: ${responseBody?.error?.reason}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 실패 처리 (HTTP 에러)
                    Log.e("MealLogging", "API 호출 실패: ${response.message()}")
                    Toast.makeText(context, "즐겨찾기 추가 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.e("MealLogging", "네트워크 오류: ${e.message}")
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // API 호출 함수: 선호도 추가
    private fun addToPreference(preferenceRequest: PatchPreferenceRequest) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.mealApiService.preferenceMeal(preferenceRequest)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.resultType == "SUCCESS") {
                        // 성공 처리
                        Log.d("MealLogging", "식단을 선호도 목록에 추가 성공")
                        Toast.makeText(context, "선호도에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    } /*else {
                        // 실패 처리
                        Log.e("MealLogging", "선호도 추가 실패: ${responseBody?.error?.reason}")
                    }*/
                } else {
                    // 실패 처리 (HTTP 에러)
                    Log.e("MealLogging", "API 호출 실패:${response.message()}")
                    Toast.makeText(context, "선호도 추가 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.e("MealLogging", "네트워크 오류: ${e.message}")
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mealComplete(completedRequest: PostCompleteMealRequest) {
        lifecycleScope.launch {
            try {
                // Get the current date in the required format (ISO 8601)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val mealDate = dateFormat.format(Date())  // Get the current date and format it

                // Update the request to include the mealDate
                val updatedRequest = completedRequest.copy(mealDate = mealDate)

                val response = RetrofitClient.mealApiService.completeMeal(updatedRequest)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.resultType == "SUCCESS") {
                        // 성공 처리
                        Log.d("MealLogging", "식단 완료 처리 성공")
                        Toast.makeText(context, "식단 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 실패 처리
                        Log.e("MealLogging", "식단 완료 실패: ${responseBody?.error?.reason}")
                    }
                } else {
                    // 실패 처리 (HTTP 에러)
                    Log.e("MealLogging", "API 호출 실패: ${response.message()}")
                    Toast.makeText(context, "식단 완료 실패", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // 네트워크 오류 처리
                Log.e("MealLogging", "네트워크 오류: ${e.message}")
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMealImage(foodName: String) {
        // 이미지 초기화
        binding.imgSample.setImageDrawable(null)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.imageApiService.getMealImage(foodName)

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.success?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.imgSample) // 이미지 뷰에 적용
                        Log.d("FoodImage", "이미지 로드 성공: $imageUrl")
                    } else {
                        Log.e("FoodImage", "이미지 URL이 비어 있음")
                        Toast.makeText(context, "이미지 URL이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FoodImage", "API 호출 실패: ${response.message()}")
                    Toast.makeText(context, "API 호출 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("FoodImage", "네트워크 오류: ${e.message}")
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
