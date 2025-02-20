package com.example.umc.Survey

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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

class SurveyAllergyFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private var selectedButton: MaterialButton? = null  // 하나만 선택 가능
    private lateinit var progressBar: ProgressBar
    private var progressValue = 30  // SurveyMealFragment에서 증가된 값 유지

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey_allergy, container, false)

        // "알레르기" 부분만 주황색으로 변경
        val textView: TextView = view.findViewById(R.id.textView)
        val fullText = "알레르기가 있다면 말씀해주세요!"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf("알레르기")
        val endIndex = startIndex + "알레르기".length
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF7300")),
            startIndex, endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
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
            goToSurveyDiseaseFragment()
        }

        // "이전 버튼" 클릭 시 이동
        previousButton.setOnClickListener {
            goToSurveyMealFragment()
        }

        return view
    }

    private fun updateProgressBar() {
        if (progressValue < 100) {
            progressValue += 10  // 진행도를 10씩 증가
            progressBar.progress = progressValue  // ProgressBar 즉시 업데이트
            setProgressWithAnimation(progressBar, progressValue)
        }
    }

    private fun setProgressWithAnimation(progressBar: ProgressBar, progress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        animator.duration = 500 // 0.5초 동안 애니메이션
        animator.start()
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
                // "있음" 선택 시 알레르기 선택 창 띄우기
                val allergyBottomSheet = SurveyAllergyBottomSheetFragment { isSelected ->
                    if (isSelected) {
                        nextButton.isEnabled = true
                        nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7300"))
                    }
                }
                allergyBottomSheet.show(parentFragmentManager, "allergy_bottom_sheet")

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

    private fun goToSurveyDiseaseFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.survey_container, SurveyDiseaseFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun goToSurveyMealFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.survey_container, SurveyMealFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}