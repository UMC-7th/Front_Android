package com.example.umc.Subscribe

import SubRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umc.model.SubItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubViewModel(
    private val repository: SubRepository
) : ViewModel() {
    private val _mealCategories = MutableStateFlow<List<SubItem>>(emptyList())
    val mealCategories: StateFlow<List<SubItem>> = _mealCategories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMealCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 카테고리별로 데이터를 불러와서 UI에 표시할 형태로 변환합니다
                val categories = listOf(
                    Triple("맛있는 일상 음식", "누구나 좋아하는 맛있는 일상 음식을 구독해보세요!", true),
                    Triple("다이어트 식단", "맛있는 다이어트 식단을 정기 배송받아 보세요!", false),
                    Triple("건강 음식", "당뇨, 고혈압 등 지병이 있는 분들께 추천해요!", false)
                )

                _mealCategories.value = categories.map { (title, desc, isClickable) ->
                    SubItem(
                        item1 = title,
                        item2 = desc,
                        isClickable = isClickable
                    )
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMealsForCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("SubViewModel", "카테고리 로딩 시작: $category")

            try {
                repository.getMealSubscriptions(category)
                    .onSuccess { meals ->
                        Log.d("SubViewModel", "카테고리 데이터 로드 성공: ${meals.size}개 항목")
                        // 추가적인 처리
                    }
                    .onFailure { exception ->
                        Log.e("SubViewModel", "카테고리 데이터 로드 실패", exception)
                        _error.value = exception.message
                    }
            } finally {
                _isLoading.value = false
            }
        }
    }
}