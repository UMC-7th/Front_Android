package com.example.umc.Quote.Sub

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pricefruit.FoodPriceReFragment

import com.example.umc.databinding.FragmentQuoteSubBinding
import com.example.umc.R

class QuoteFragmentSub : Fragment() {

    private var _binding: FragmentQuoteSubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuoteSubBinding.inflate(inflater, container, false)

        // SpannableString을 사용하여 특정 텍스트 색상 변경
        val fullText = "제철 중 과일의 시세를 순위로 확인하세요"
        val spannableString = SpannableString(fullText)

        // "과일의 시세" 텍스트의 시작과 끝 위치 찾기
        val startIndex = fullText.indexOf("과일의 시세")
        val endIndex = startIndex + "과일의 시세".length

        // Primary_Orange1 색상 적용
        val colorSpan = ForegroundColorSpan(resources.getColor(R.color.Primary_Orange1))
        spannableString.setSpan(
            colorSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // 변경된 텍스트를 TextView에 설정
        binding.textView36.text = spannableString

        // 바나나 이미지 클릭 이벤트
        binding.banana.setOnClickListener {
            // 현재 Fragment에서 다른 Fragment로 전환
            val fragmentTransaction = parentFragmentManager.beginTransaction()

            // 새로운 Fragment 생성
            val foodPriceFragment = FoodPriceReFragment()

            // Fragment 교체
            fragmentTransaction.replace(R.id.main_container, foodPriceFragment)

            // 백 스택에 추가 (뒤로 가기 기능을 위해)
            fragmentTransaction.addToBackStack(null)

            // 트랜잭션 실행
            fragmentTransaction.commit()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}