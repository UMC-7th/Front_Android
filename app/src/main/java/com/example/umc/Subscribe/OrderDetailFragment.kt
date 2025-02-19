package com.example.umc.Subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Main.MainActivity
import com.example.umc.databinding.FragmentOrderDetailBinding

class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val orderMenuAdapter = OrderMenuAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        loadOrderData()
    }

    private fun setupToolbar() {
        // 필요한 경우 툴바 설정
    }

    private fun setupRecyclerView() {
        binding.orderItemsRv.apply {
            adapter = orderMenuAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadOrderData() {
        // 기존 데이터 로딩 로직 그대로 유지
        val orderItems = listOf(
            OrderMenuItem(
                menuDate = "1/22 (수) 식단 - 배송 완료",
                breakfastName = "하루 시작 포케 - 2인분",
                breakfastKcal = 420,
                breakfastPrice = 20000,
                lunchName = "고등어 조림 한상 - 1인분",
                lunchKcal = 840,
                lunchPrice = 12000
            )
        )
        orderMenuAdapter.submitList(orderItems)

        binding.apply {
            orderDateTv.text = "2025. 1. 1 결제건"
            recipientNameTv.text = "김태현"
            addressDetailTv.text = "서울시 송파구 송파동 송파아파트 101동 101호"
            phoneTv.text = "010-1234-5678"
            deliveryMemoTv.text = "문 앞 (1234)"

            productPriceTv.text = "32,000원"
            deliveryFeeTv.text = "0원"
            kakaoPayTv.text = "32,000원"
            totalPriceTv.text = "32,000원"

            editDeliveryBtn.setOnClickListener {
                // TODO: 배송지 변경 기능 구현
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle("주문 상세보기", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}