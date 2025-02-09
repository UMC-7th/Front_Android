package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.databinding.FragmentDietSubBinding

class DietSubFragment : Fragment() {

    private var _binding: FragmentDietSubBinding? = null
    private val binding get() = _binding!!

    private lateinit var dailyDietAdapter: DailyDietAdapter
    private lateinit var dietList: List<DietItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 예제 데이터
        dietList = listOf(
            DietItem("01.01", "수", "콩나물 김치국, 제육볶음, 콩자반", "돈까스 냉모밀 세트, 새우튀김, 식혜", "제육볶음, 파래무침, 멸치볶음, 미역국"),
            DietItem("01.02", "목", "콩나물 김치국, 제육볶음, 콩자반", "돈까스 냉모밀 세트, 새우튀김, 식혜", "제육볶음, 파래무침, 멸치볶음, 미역국"),
            DietItem("01.03", "금", "콩나물 김치국, 제육볶음, 콩자반", "돈까스 냉모밀 세트, 새우튀김, 식혜", "제육볶음, 파래무침, 멸치볶음, 미역국"),
            DietItem("01.04", "토", "콩나물 김치국, 제육볶음, 콩자반", "돈까스 냉모밀 세트, 새우튀김, 식혜", "제육볶음, 파래무침, 멸치볶음, 미역국"),
            DietItem("01.06", "일", "콩나물 김치국, 제육볶음, 콩자반", "돈까스 냉모밀 세트, 새우튀김, 식혜", "제육볶음, 파래무침, 멸치볶음, 미역국")
        )

        dailyDietAdapter = DailyDietAdapter(dietList)
        binding.recyclerDailyDiet.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDailyDiet.adapter = dailyDietAdapter

        binding.btCart.setOnClickListener {
            Toast.makeText(requireContext(), "장바구니 담기", Toast.LENGTH_SHORT).show()
            // 장바구니 확인 활성, 비활성화 기능은 나중에 추가할 예정
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
