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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.example.umc.databinding.FragmentSurveyGoalBinding
import com.google.android.material.button.MaterialButton

class SurveyGoalFragment : Fragment() {
    private var _binding: FragmentSurveyGoalBinding? = null
    private val binding get() = _binding!!
    private val selectedButtons = mutableSetOf<MaterialButton>()
    private var progressValue = 0
    private var currentSelectedButton: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextColor()
        setupProgressBar()
        setupButtons()
        setupNavigationButtons()
    }

    private fun setupTextColor() {
        // "이루고 싶은 목표를 설정해주세요" 텍스트에서 "목표를" 부분만 강조
        val fullText = binding.textView.text.toString()
        val targetText = "목표를"
        val startIndex = fullText.indexOf(targetText)
        val endIndex = startIndex + targetText.length

        val spannableString = SpannableString(fullText)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.Primary_Orange1)),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.textView.text = spannableString
    }

    private fun setupProgressBar() {
        binding.progressBar.progress = progressValue
    }

    private fun setupButtons() {
        val goalButtons = listOf(
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6
        )

        goalButtons.forEach { button ->
            button.setOnClickListener {
                handleButtonSelection(button)
            }
        }
    }

    private fun handleButtonSelection(selectedButton: MaterialButton) {
        // 이전에 선택된 버튼이 있다면 선택 해제
        currentSelectedButton?.let {
            it.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F0F0F0"))
            it.setTextColor(Color.parseColor("#9A9A9A"))
            it.strokeColor = ColorStateList.valueOf(Color.parseColor("#F0F0F0"))
        }

        // 새로운 버튼 선택
        if (currentSelectedButton != selectedButton) {
            selectedButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFEAD9"))
            selectedButton.setTextColor(Color.parseColor("#FF7300"))
            selectedButton.strokeColor = ColorStateList.valueOf(Color.parseColor("#FF7300"))
            currentSelectedButton = selectedButton
            binding.nextButton.isEnabled = true
            binding.nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF7300"))
        } else {
            // 같은 버튼을 다시 클릭한 경우 선택 해제
            currentSelectedButton = null
            binding.nextButton.isEnabled = false
            binding.nextButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CDCDCD"))
        }
    }

    private fun setupNavigationButtons() {
        binding.nextButton.setOnClickListener {
            if (currentSelectedButton != null) {
                updateProgressBar()
                goToSurveyMealFragment()
            }
        }

        binding.previousButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updateProgressBar() {
        if (progressValue < 100) {
            progressValue += 10
            setProgressWithAnimation(binding.progressBar, progressValue)
        }
    }

    private fun setProgressWithAnimation(progressBar: android.widget.ProgressBar, progress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, progress)
        animator.duration = 500
        animator.start()
    }

    private fun goToSurveyMealFragment() {
        // survey_container를 사용하도록 변경
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.survey_container, SurveyMealFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}