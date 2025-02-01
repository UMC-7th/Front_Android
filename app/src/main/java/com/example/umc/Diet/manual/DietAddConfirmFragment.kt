package com.example.umc.Diet.manual

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.R
import com.example.umc.databinding.FragmentDietAddConfirmBinding
import com.example.umc.model.ManualMeals

class DietAddConfirmFragment : Fragment(R.layout.fragment_diet_add_confirm) {

    private lateinit var binding: FragmentDietAddConfirmBinding
    private lateinit var adapter: DietAddConfirmAdapter
    private val viewModel: DietAddConfirmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDietAddConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DietAddConfirmAdapter(mutableListOf())
        binding.rvAddConfirm.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAddConfirm.adapter = adapter

        val mealList = arguments?.getSerializable("mealList") as? List<String> ?: emptyList()
        Log.d("MealLogging", "Received meal list: $mealList")

        adapter.updateMeals(mealList.map { food ->
            ManualMeals(
                calorieTotal = 0,
                foods = listOf(food),
                time = "Unknown",
                mealDate = "Unknown"
            )
        })

        viewModel.mealList.observe(viewLifecycleOwner) { meals ->
            adapter.updateMeals(meals)
        }

        viewModel.fetchManualMeals(userId = 1)
    }
}
