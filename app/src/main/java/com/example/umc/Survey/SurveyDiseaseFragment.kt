package com.example.umc.Survey

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.google.android.material.button.MaterialButton

class SurveyDiseaseFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var progressBar: ProgressBar
    private var progressValue = 40  // SurveyAllergyFragment에서 증가된 값 유지
    private var selectedButton: MaterialButton? = null  // 하나만 선택 가능

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey_disease, container, false)

        // "지병" 부분만 주황색으로 변경
        val textView: TextView = view.findViewById(R.id.textView)
        val fullText = "지병이 있다면 말씀해주세요!"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf("지병")
        if (startIndex >= 0) {
            val endIndex = startIndex + "지병".length
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF7300")),
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.text = spannable

        // ProgressBar 설정
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.progress = progressValue

        // "있음", "없음" 버튼 가져오기
        val yesButton = view.findViewById<MaterialButton>(R.id.yes_button)
        val noButton = view.findViewById<MaterialButton>(R.id.no_button)

        nextButton = view.findViewById(R.id.next_button)
        previousButton = view.findViewById(R.id.previous_button)

        // 초기 상태에서 "다음" 버튼 비활성화
        nextButton.isEnabled = false
        nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD"))

        // 버튼 클릭 이벤트 추가 (하나만 선택 가능)
        yesButton.setOnClickListener { selectSingleButton(yesButton) }
        noButton.setOnClickListener { selectSingleButton(noButton) }

        // "다음 버튼" 클릭 시 이동
        nextButton.setOnClickListener {
            goToSurveyPeopleFragment()
        }

        // "이전 버튼" 클릭 시 이동
        previousButton.setOnClickListener {
            goToSurveyAllergyFragment()
        }

        return view
    }

    // 하나의 버튼만 선택 가능
    private fun selectSingleButton(button: MaterialButton) {
        selectedButton?.let {
            it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F0F0F0")) // 기본 배경
            it.setTextColor(Color.parseColor("#9A9A9A")) // 기본 글씨 색
            it.strokeColor = ColorStateList.valueOf(Color.parseColor("#F0F0F0")) // 기본 테두리
        }

        if (selectedButton == button) {
            selectedButton = null // 다시 클릭하면 선택 해제
            nextButton.isEnabled = false
            nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD"))
        } else {
            selectedButton = button
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFEAD9")) // 선택된 배경
            button.setTextColor(Color.parseColor("#FF7300")) // 선택된 글씨 색
            button.strokeColor = ColorStateList.valueOf(Color.parseColor("#FF7300")) // 선택된 테두리

            if (button.id == R.id.yes_button) {
                // "있음" 선택 시 지병 선택 창 띄우기
                val diseaseBottomSheet = SurveyDiseaseBottomSheetFragment { isSelected ->
                    if (isSelected) {
                        nextButton.isEnabled = true
                        nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7300"))
                    }
                }
                diseaseBottomSheet.show(parentFragmentManager, "disease_bottom_sheet")

                // "있음" 선택 시 다음 버튼 비활성화 유지
                nextButton.isEnabled = false
                nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD"))
            } else {
                // "없음" 선택 시 바로 다음 버튼 활성화
                nextButton.isEnabled = true
                nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7300"))
            }
        }
    }

    private fun goToSurveyAllergyFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.survey_container, SurveyAllergyFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun goToSurveyPeopleFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.survey_container, SurveyPeopleFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
