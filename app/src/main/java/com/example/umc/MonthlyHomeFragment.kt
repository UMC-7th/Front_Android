package com.example.umc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.databinding.FragmentHomeBinding
import com.example.umc.databinding.FragmentMonthlyHomeBinding

// MonthlyHomeFragment.kt
class MonthlyHomeFragment : Fragment() {
    private var _binding: FragmentMonthlyHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupCalendarView()
    }

//    private fun setupCalendarView() {
//        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            // 선택된 날짜의 메뉴 데이터 로드
//            loadMenusForDate("$year-${month + 1}-$dayOfMonth")
//        }
//    }

    private fun loadMenusForDate(date: String) {
        // 해당 날짜의 메뉴 데이터 로드 로직
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}