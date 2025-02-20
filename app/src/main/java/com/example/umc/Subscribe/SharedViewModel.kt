package com.example.umc.Subscribe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.umc.Diet.DietItem
import com.example.umc.model.CartItem

class SubscribeViewModel : ViewModel() {
    private val _selectedDiets = MutableLiveData<List<DietItem>>()
    val selectedDiets: LiveData<List<DietItem>> = _selectedDiets

    fun setSelectedDiets(diets: List<DietItem>) {
        _selectedDiets.value = diets.filter { it.isChecked }
    }

    fun convertToCartItems(selectedDiets: List<DietItem>): List<CartItem> {
        return selectedDiets.map { diet ->
            CartItem(
                date = diet.mealDate,
                time = diet.time,
                menu = diet.food,
                serving = 1, // 기본값 설정
                isChecked = true // 카트에 추가될 때 기본적으로 체크됨
            )
        }
    }
}