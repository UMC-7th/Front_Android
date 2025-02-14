package com.example.umc.Subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Main.MainActivity
import com.example.umc.databinding.FragmentSubscriptionHistoryBinding

class SubscriptionHistoryFragment : Fragment() {
    private var _binding: FragmentSubscriptionHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        loadOrderHistory()
    }

    private fun setupRecyclerView() {
        adapter = OrderHistoryAdapter()
        binding.subscriptionHistoryRecyclerView.apply {
            adapter = this@SubscriptionHistoryFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchBar() {
        binding.searchBar.setOnSearchClickListener { searchText ->
            filterOrders(searchText)
        }
    }

    private fun filterOrders(searchText: String) {
        if (searchText.isEmpty()) {
            loadOrderHistory()
            return
        }

        // 날짜로 필터링된 주문 목록 표시
        val filteredList = orderGroups.filter { group ->
            group.orderItems.any { item ->
                item.deliveryDate.contains(searchText, ignoreCase = true)
            }
        }
        adapter.submitList(filteredList)
    }

    private fun loadOrderHistory() {
        val sampleData = listOf(
            OrderGroup(
                orderDate = "2025.01.01 주문내역",
                orderItems = listOf(
                    OrderItem(
                        deliveryDate = "1/22 (수)",
                        deliveryStatus = "배송 완료",
                        deliveryLocation = "배송장소 (아침, 점심)",
                        menuName = "다이어트 구독 식단",
                        breakfastMenu = "아침 - 하루 시작 포케 (420kcal)",
                        lunchMenu = "점심 - 고등어 조림 한상/비조림 (830kcal)",
                        imageUrl = "sample_image_url",
                        isReviewEnabled = true
                    ),
                    OrderItem(
                        deliveryDate = "1/24 (금)",
                        deliveryStatus = "도착 예정",
                        deliveryLocation = "배송장소 (아침, 점심)",
                        menuName = "다이어트 구독 식단",
                        breakfastMenu = "아침 - 하루 시작 포케 (420kcal)",
                        lunchMenu = "점심 - 고등어 조림 한상/비조림 (830kcal)",
                        imageUrl = "sample_image_url",
                        isReviewEnabled = false
                    )
                )
            )
        )

        adapter.submitList(sampleData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle("구독 내역", true)
    }

    companion object {
        private val orderGroups = mutableListOf<OrderGroup>()
    }
}