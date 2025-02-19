package com.example.umc.Quote

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.Main.MainActivity
import com.example.umc.databinding.FragmentPriceDetailBinding

class PriceDetailFragment : Fragment() {

    private var _binding: FragmentPriceDetailBinding? = null
    private val binding get() = _binding!!
    private var categoryName: String? = null
    private val dateList = arrayOf("오늘", "내일", "모레")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString("category_name")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPriceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scrollIndicator: ProgressBar = binding.scrollIndicator

        // 가로 RecyclerView 설정
        val horizontalRecyclerView: RecyclerView = binding.horizontalRecyclerView
        horizontalRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val priceList = listOf(
            FoodItem("바나나", "26,828원", "1kg", ""),
            FoodItem("딸기", "12,500원", "500g", ""),
            FoodItem("토마토", "9,800원", "1kg", ""),
            FoodItem("샤인머스켓", "38,000원", "500g", ""),
            FoodItem("귤", "16,000원", "1kg", ""),
            FoodItem("수박", "22,000원", "1개", "")
        )

        horizontalRecyclerView.adapter = HorizontalPriceDetailAdapter(priceList, this@PriceDetailFragment)

        // 스크롤 리스너 설정
        horizontalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateIndicator(recyclerView, scrollIndicator)
            }
        })

        // 초기 진행 상태 업데이트
        updateIndicator(horizontalRecyclerView, scrollIndicator)

        // 세로 RecyclerView 설정
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = PriceDetailAdapter(priceList, this@PriceDetailFragment)

        // 날짜 선택 버튼 설정 (예: TextView로 날짜 선택)
        binding.dateSelector.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("날짜 선택")
                .setItems(dateList) { _, which ->
                    binding.tvToday.text = dateList[which] // 선택된 날짜 표시
                }
                .show()
        }
    }

    private fun updateIndicator(recyclerView: RecyclerView, indicator: ProgressBar) {
        val totalWidth = recyclerView.computeHorizontalScrollRange()
        val visibleWidth = recyclerView.computeHorizontalScrollExtent()
        val scrollOffset = recyclerView.computeHorizontalScrollOffset()

        val scrollProgress = if (totalWidth - visibleWidth > 0) {
            scrollOffset.toFloat() / (totalWidth - visibleWidth)
        } else {
            0f
        }

        // ProgressBar의 위치를 고정하고, 진행 상태를 업데이트
        indicator.progress = (scrollProgress * 100).toInt()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle(categoryName ?: "과일류", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
