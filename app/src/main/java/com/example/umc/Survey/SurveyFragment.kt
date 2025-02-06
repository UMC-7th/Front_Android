package com.example.umc.Survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.example.umc.databinding.FragmentSurveyBinding
import com.google.android.material.button.MaterialButton

class SurveyFragment : Fragment() {

    private var _binding: FragmentSurveyBinding? = null
    private val binding get() = _binding!!

    private var selectedButton: MaterialButton? = null
    private val totalScreens = 10
    private var currentScreen = 1 // 현재 화면 번호

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 Progress 설정
        updateProgress()

        // 버튼 클릭 이벤트 처리
        setupButtonClickListener(binding.suv1)
        setupButtonClickListener(binding.suv2)
        setupButtonClickListener(binding.suv3)
        setupButtonClickListener(binding.suv4)
        setupButtonClickListener(binding.suv5)
        setupButtonClickListener(binding.suv6)

        // 이전 & 다음 버튼 클릭 이벤트
        binding.prev1.setOnClickListener { navigateToPreviousScreen() }
        binding.next1.setOnClickListener { navigateToNextScreen() }
    }

    private fun setupButtonClickListener(button: MaterialButton) {
        button.setOnClickListener {
            selectedButton?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray8)) // 기존 선택 해제
            button.setBackgroundResource(R.drawable.select_survey)
            selectedButton = button

            // 이전 & 다음 버튼 색상 활성화
            binding.prev1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray7))
            binding.next1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Primary_Orange1))
        }
    }

    private fun navigateToPreviousScreen() {
        if (currentScreen > 1) {
            currentScreen--
            updateProgress()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SurveyFragment()) // 이전 Fragment로 변경
                .commit()
        }
    }

    private fun navigateToNextScreen() {
        if (currentScreen < totalScreens) {
            currentScreen++
            updateProgress()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SurveyFragment()) // 다음 Fragment로 변경
                .commit()
        }
    }

    private fun updateProgress() {
        val progress = (currentScreen.toFloat() / totalScreens.toFloat()) * 100
        binding.scrollIndicator.progress = progress.toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
