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
        setupRecyclerViews()// 전달된 데이터 수신

        val month = arguments?.getInt("month") ?: 1
        val day = arguments?.getInt("day") ?: 1

        // TextView의 텍스트 설정
        binding.tvDailyDietToday.text = getString(R.string.daily_diet_day, month, day) // 여기서 데이터 사용
    }

    private fun initializeAdapters() {
        breakfastAdapter = MenuItemAdapter { item: MenuItem, position: Int ->
            selectedBreakfastPosition = position
        }

        lunchAdapter = MenuItemAdapter { item: MenuItem, position: Int ->
            selectedLunchPosition = position
        }

        dinnerAdapter = MenuItemAdapter { item: MenuItem, position: Int ->
            selectedDinnerPosition = position
        }
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
