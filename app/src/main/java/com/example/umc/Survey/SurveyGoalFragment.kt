package com.example.umc.Survey

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import android.widget.Button
import com.example.umc.R

class SurveyGoalFragment : Fragment() {

    private lateinit var nextButton: Button
    private val selectedButtons = mutableSetOf<MaterialButton>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey_goal, container, false)

        // 버튼 목록
        val goalButtons = listOf(
            view.findViewById<MaterialButton>(R.id.weight_down_button),
            view.findViewById<MaterialButton>(R.id.weight_up_button),
            view.findViewById<MaterialButton>(R.id.weight_less_down_button),
            view.findViewById<MaterialButton>(R.id.weight_less_up_button),
            view.findViewById<MaterialButton>(R.id.health_meal_button),
            view.findViewById<MaterialButton>(R.id.normal_meal_button)
        )

        nextButton = view.findViewById(R.id.next_button)

        // 모든 버튼에 클릭 이벤트 추가
        for (button in goalButtons) {
            button.setOnClickListener {
                toggleButtonState(button)
            }
        }

        return view
    }

    private fun toggleButtonState(button: MaterialButton) {
        if (selectedButtons.contains(button)) {
            // 버튼이 이미 선택된 상태라면 원래 상태로 되돌림
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F0F0F0")) // 원래 배경색
            button.setTextColor(Color.parseColor("#9A9A9A")) // 원래 글씨 색
            button.strokeColor = ColorStateList.valueOf(Color.parseColor("#F0F0F0")) // 원래 테두리
            selectedButtons.remove(button)
        } else {
            // 버튼 선택됨
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFEAD9")) // 배경 변경
            button.setTextColor(Color.parseColor("#FF7300")) // 글씨색 변경
            button.strokeColor = ColorStateList.valueOf(Color.parseColor("#FF7300")) // 테두리 변경
            selectedButtons.add(button)
        }

        // "다음" 버튼 활성화/비활성화
        if (selectedButtons.isNotEmpty()) {
            nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7300")) // 활성화 색상
            nextButton.isEnabled = true
        } else {
            nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD")) // 비활성화 색상
            nextButton.isEnabled = false
        }
    }
}
