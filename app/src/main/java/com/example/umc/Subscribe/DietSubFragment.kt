package com.example.umc.Subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Diet.DietItem
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.cart.SubscribeCart
import com.example.umc.databinding.FragmentDietSubBinding

class DietSubFragment : Fragment(), OnDietCheckedChangeListener {

    private var _binding: FragmentDietSubBinding? = null
    private val binding get() = _binding!!

    private lateinit var dailyDietAdapter: SubscribeDietAdapter
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

        dailyDietAdapter = SubscribeDietAdapter(dietList, this)
        binding.recyclerDailyDiet.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDailyDiet.adapter = dailyDietAdapter

        binding.btCart.setOnClickListener {
            Toast.makeText(requireContext(), "장바구니 담기 완료", Toast.LENGTH_SHORT).show()
            navigateToSubscribeCart()
        }
    }

    private fun navigateToSubscribeCart() {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, SubscribeCart())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = activity as? MainActivity
        mainActivity?.showTitle("맛있는 일상 구독", true)
        (activity as? MainActivity)?.showBottomBar()
    }

    override fun onDietCheckedChanged(isAnyChecked: Boolean) {
        val color = if (isAnyChecked) R.color.Primary_Orange1 else R.color.Gray7
        binding.btCart.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }
}
