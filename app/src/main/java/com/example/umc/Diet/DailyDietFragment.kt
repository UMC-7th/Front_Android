package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R
import com.example.umc.databinding.FragmentDailyDietBinding

class DailyDietFragment : Fragment() {
    private var _binding: FragmentDailyDietBinding? = null
    private val binding get() = _binding!!

    private var selectedBreakfastPosition = -1
    private var selectedLunchPosition = -1
    private var selectedDinnerPosition = -1

    private lateinit var breakfastAdapter: MenuItemAdapter
    private lateinit var lunchAdapter: MenuItemAdapter
    private lateinit var dinnerAdapter: MenuItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyDietBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdapters()
        setupRecyclerViews()

        val month = arguments?.getInt("month") ?: 1
        val day = arguments?.getInt("day") ?: 1
        binding.tvDailyDietToday.text = getString(R.string.daily_diet_day, month, day)
    }

    private fun initializeAdapters() {
        breakfastAdapter = MenuItemAdapter(
            onClick = { item, position ->
                selectedBreakfastPosition = position
            },
            onFavoriteChanged = { item, isFavorite ->
                // 즐겨찾기 상태 변경 처리
            },
            onDietCompleteChanged = { item, isCompleted ->
                // 식단 완료 상태 변경 처리
            }
        )

        lunchAdapter = MenuItemAdapter(
            onClick = { item, position ->
                selectedLunchPosition = position
            },
            onFavoriteChanged = { item, isFavorite ->
                // 즐겨찾기 상태 변경 처리
            },
            onDietCompleteChanged = { item, isCompleted ->
                // 식단 완료 상태 변경 처리
            }
        )

        dinnerAdapter = MenuItemAdapter(
            onClick = { item, position ->
                selectedDinnerPosition = position
            },
            onFavoriteChanged = { item, isFavorite ->
                // 즐겨찾기 상태 변경 처리
            },
            onDietCompleteChanged = { item, isCompleted ->
                // 식단 완료 상태 변경 처리
            }
        )
    }

    private fun setupRecyclerViews() {
        binding.apply {
            rvBreakfast.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = breakfastAdapter
                setHasFixedSize(true)
            }
            breakfastAdapter.submitList(getDummyMenuItems())

            rvLunch.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = lunchAdapter
                setHasFixedSize(true)
            }
            lunchAdapter.submitList(getDummyMenuItems())

            rvDinner.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = dinnerAdapter
                setHasFixedSize(true)
            }
            dinnerAdapter.submitList(getDummyMenuItems())
        }
    }

    private fun getDummyMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem("image_url1", "제육볶음 도시락", "560Kcal"),
            MenuItem("image_url2", "샐러드 도시락", "450Kcal"),
            MenuItem("image_url3", "볶음밥 도시락", "520Kcal"),
            MenuItem("image_url4", "연어 도시락", "480Kcal")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}