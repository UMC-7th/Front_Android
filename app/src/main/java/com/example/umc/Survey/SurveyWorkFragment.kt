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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.umc.R
import com.google.android.material.button.MaterialButton

class SurveyWorkFragment : Fragment() {
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var progressBar: ProgressBar
    private var progressValue = 100  // SurveyBmiFragment에서 증가된 값 유지

    private var selectedWorkButton: MaterialButton? = null
    private var selectedExercise: String? = null // 운동 횟수 선택값 저장


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey_work, container, false)

        // "일, 운동 횟수" 부분만 주황색으로 변경
        val textView: TextView = view.findViewById(R.id.textView)
        val fullText = "현재 하시는 일과 운동 횟수를 알려주세요!"
        val spannable = SpannableString(fullText)

        val startIndex = fullText.indexOf("일")
        if (startIndex >= 0) {
            val endIndex = startIndex + "일".length
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF7300")),
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val startIndexWork = fullText.indexOf("운동 횟수")
        if (startIndexWork >= 0) {
            val endIndexWeight = startIndexWork + "운동 횟수".length
            spannable.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF7300")),
                startIndexWork, endIndexWeight,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textView.text = spannable

        // ProgressBar 가져오기
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.progress = progressValue

        // 버튼 목록
        val workButtons = listOf(
            view.findViewById<MaterialButton>(R.id.no_work_button),
            view.findViewById<MaterialButton>(R.id.house_work_button),
            view.findViewById<MaterialButton>(R.id.sit_down_button),
            view.findViewById<MaterialButton>(R.id.student_button),
            view.findViewById<MaterialButton>(R.id.sit_up_button),
            view.findViewById<MaterialButton>(R.id.activity_work_button)
        )

        nextButton = view.findViewById(R.id.next_button)
        previousButton = view.findViewById(R.id.previous_button)

        // 초기 상태에서 "다음" 버튼 비활성화
        nextButton.isEnabled = false
        nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD"))


        // 모든 버튼에 클릭 이벤트 추가 (하나만 선택 가능)
        for (button in workButtons) {
            button.setOnClickListener {
                selectSingleWorkButton(button)

                // 운동 횟수 선택 `BottomSheetDialogFragment` 띄우기
                val exerciseBottomSheet = SurveyExerciseBottomSheetFragment { selected ->
                    selectedExercise = selected  // 운동 횟수 선택 저장
                    updateNextButtonState()
                }
                exerciseBottomSheet.show(parentFragmentManager, "exercise_bottom_sheet")

                // "운동 횟수"를 선택하기 전까지 다음 버튼 비활성화
                nextButton.isEnabled = false
                nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD"))
            }
        }

        // "다음 버튼" 클릭 시 선택 여부 확인 후 이동
        nextButton.setOnClickListener {
            if (selectedWorkButton != null && selectedExercise != null) {
                updateProgressBar()
                //goToSurveyGoalFragment()
            } else {
                Toast.makeText(requireContext(), "하나의 항목을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // "이전 버튼" 클릭 시 SurveyBmiFragment로 이동
        previousButton.setOnClickListener {
            goToSurveyBmiFragment()
        }

        return view
    }

    // **하나의 버튼만 선택 가능하도록 설정**
    private fun selectSingleWorkButton(button: MaterialButton) {
        selectedWorkButton?.let {
            it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F0F0F0")) // 기본 배경
            it.setTextColor(Color.parseColor("#9A9A9A")) // 기본 글씨 색
            it.strokeColor = ColorStateList.valueOf(Color.parseColor("#F0F0F0")) // 기본 테두리 색
        }

        if (selectedWorkButton == button) {
            selectedWorkButton = null  // 동일한 버튼을 다시 클릭하면 해제
        } else {
            selectedWorkButton = button
            button.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFEAD9")) // 선택된 배경
            button.setTextColor(Color.parseColor("#FF7300")) // 선택된 글씨 색
            button.strokeColor = ColorStateList.valueOf(Color.parseColor("#FF7300")) // 선택된 테두리
        }

        // "다음 버튼" 활성화/비활성화 업데이트
        updateNextButtonState()
    }

    // "다음 버튼" 활성화 여부 설정
    private fun updateNextButtonState() {
        nextButton.isEnabled = selectedWorkButton != null && selectedExercise != null
        nextButton.backgroundTintList = ColorStateList.valueOf(
            if (selectedWorkButton != null && selectedExercise != null) Color.parseColor("#FF7300") else Color.parseColor("#CDCDCD")
        )
    }

    // ProgressBar 증가 애니메이션 적용
    private fun updateProgressBar() {
        if (progressValue < 100) {
            progressValue += 10
            setProgressWithAnimation(progressBar, progressValue)
        }
    }

    private fun setProgressWithAnimation(progressBar: ProgressBar, progress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        animator.duration = 500
        animator.start()
    }

//    // SurveyGoalFragment로 이동
//    private fun goToSurveyGoalFragment() {
//        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.main_container, SurveyGoalFragment())
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//    }

    // SurveyBmiFragment로 이동
    private fun goToSurveyBmiFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_container, SurveyBmiFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
