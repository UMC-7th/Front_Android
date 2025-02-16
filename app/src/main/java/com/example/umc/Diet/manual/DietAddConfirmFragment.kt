package com.example.umc.Diet.manual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.R
import com.example.umc.databinding.FragmentDietAddConfirmBinding
import com.example.umc.model.ManualMeals
import android.util.Log

class DietAddConfirmFragment : Fragment(R.layout.fragment_diet_add_confirm) {

    private var _binding: FragmentDietAddConfirmBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DietAddConfirmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietAddConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 어댑터 설정
        adapter = DietAddConfirmAdapter(mutableListOf())
        binding.rvAddConfirm.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAddConfirm.adapter = adapter

        val foods = arguments?.getString("foods")?.split(", ") ?: emptyList()
        val date = arguments?.getString("date")
        val time = arguments?.getString("time")
        val calories = arguments?.getInt("calories")

        Log.d("MealLogging", "전달된 foods: $foods")
        Log.d("MealLogging", "전달된 date: $date")
        Log.d("MealLogging", "전달된 time: $time")
        Log.d("MealLogging", "전달된 calories: $calories")

        val mealList = listOf(
            ManualMeals(
                calorieTotal = calories ?: 0,
                foods = foods,
                time = time ?: "Unknown",  // 기본값 설정
                mealDate = date ?: "Unknown"
            )
        )
        adapter.updateMeals(mealList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
