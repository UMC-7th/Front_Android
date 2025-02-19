package com.example.umc.Subscribe

import SubMealList
import SubRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc.model.SubItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubViewModel(private val repository: SubRepository) : ViewModel() {
    private val _mealCategories = MutableStateFlow<List<SubItem>>(emptyList())
    val mealCategories: StateFlow<List<SubItem>> = _mealCategories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // 각 카테고리별 데이터를 저장할 StateFlow들
    private val _dailyMeals = MutableStateFlow<List<SubMealList>>(emptyList())
    val dailyMeals: StateFlow<List<SubMealList>> = _dailyMeals.asStateFlow()

    private val _dietMeals = MutableStateFlow<List<SubMealList>>(emptyList())
    val dietMeals: StateFlow<List<SubMealList>> = _dietMeals.asStateFlow()

    private val _healthMeals = MutableStateFlow<List<SubMealList>>(emptyList())
    val healthMeals: StateFlow<List<SubMealList>> = _healthMeals.asStateFlow()

    // 카테고리 정보와 조회를 위한 매핑
    private val categories = listOf(
        Category("맛있는 일상 음식", "누구나 좋아하는 맛있는 일상 음식을 구독해보세요!", true),
        Category("다이어트 식단", "맛있는 다이어트 식단을 정기 배송받아 보세요!", true),
        Category("건강 음식", "당뇨, 고혈압 등 지병이 있는 분들께 추천해요!", true)
    )

    init {
        loadAllCategories()
    }

    private fun loadAllCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // UI에 카테고리 목록 표시
                _mealCategories.value = categories.map { category ->
                    SubItem(
                        item1 = category.title,
                        item2 = category.description,
                        isClickable = category.isClickable
                    )
                }

                // 각 카테고리별 데이터 로드
                categories.forEach { category ->
                    loadMealsForCategory(category.title)
                }
            } catch (e: Exception) {
                _error.value = "카테고리 로드 중 오류: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMealsForCategory(category: String) {
        viewModelScope.launch {
            try {
                repository.getMealSubscriptions(category)
                    .onSuccess { meals ->
                        when (category) {
                            "맛있는 일상 음식" -> _dailyMeals.value = meals
                            "다이어트 식단" -> _dietMeals.value = meals
                            "건강 음식" -> _healthMeals.value = meals
                        }
                        Log.d("SubViewModel", "$category 데이터 로드 성공: ${meals.size}개")
                    }
                    .onFailure { exception ->
                        _error.value = "$category 로드 실패: ${exception.message}"
                        Log.e("SubViewModel", "$category 로드 실패", exception)
                    }
            } catch (e: Exception) {
                _error.value = "$category 로드 중 오류: ${e.message}"
                Log.e("SubViewModel", "$category 로드 중 예외", e)
            }
        }
    }

    fun getMealsForCategory(category: String): StateFlow<List<SubMealList>> {
        return when (category) {
            "맛있는 일상 음식" -> dailyMeals
            "다이어트 식단" -> dietMeals
            "건강 음식" -> healthMeals
            else -> MutableStateFlow(emptyList())
        }
    }

    data class Category(
        val title: String,
        val description: String,
        val isClickable: Boolean
    )
}