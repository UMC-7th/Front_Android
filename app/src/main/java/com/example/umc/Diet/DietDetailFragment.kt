package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Nutrition
import com.example.umc.R
import com.example.umc.databinding.FragmentDietDetailBinding

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

        val dummyPrice = 4500 // 예상 가격
        val dummyCalories = 350 // 예상 열량

        // 가격, 열량 연결
        binding.tvPrice.text = getString(R.string.diet_price_contents, dummyPrice)
        binding.tvCalories.text = getString(R.string.diet_calories_contents, dummyCalories)

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
            if (isLiked && isDisliked) {
                isDisliked = false
                binding.btDislike.setColorFilter(ContextCompat.getColor(requireContext(),
                    R.color.Gray4
                ))
            }
        }
        // 싫어요 버튼 설정
        binding.btDislike.setOnClickListener {
            isDisliked = !isDisliked
            binding.btDislike.setColorFilter(ContextCompat.getColor(requireContext(), if (isDisliked) R.color.Primary_Orange1 else R.color.Gray7))
            if (isDisliked && isLiked) {
                isLiked = false
                binding.btLike.setColorFilter(ContextCompat.getColor(requireContext(),
                    R.color.Gray7
                ))
            }
        }

        // 즐겨찾기 버튼 설정
        binding.btFavorite.setOnClickListener {
            isFavorited = !isFavorited
            binding.btFavorite.setColorFilter(ContextCompat.getColor(requireContext(), if (isFavorited) R.color.Primary_Orange1 else R.color.Gray7))
        }

        // 식단 완료 버튼 설정
        binding.btDietComplete.setOnClickListener {
            isCompleted = !isCompleted
            binding.btDietComplete.setBackgroundColor(ContextCompat.getColor(requireContext(), if (isCompleted) R.color.Primary_Orange1 else R.color.Gray7))
        }

        // 물음표 버튼 설정
        binding.btQuestion.setOnClickListener {
            if (popupWindow == null) {
                val tooltipView = layoutInflater.inflate(R.layout.dialog_tooltip, null)
                popupWindow = PopupWindow(tooltipView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            if (isTooltipVisible) {
                popupWindow?.dismiss()
            } else {
                popupWindow?.showAsDropDown(binding.btQuestion, -180, 0)
            }
            isTooltipVisible = !isTooltipVisible
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
