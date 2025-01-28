package com.example.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.example.umc.databinding.FragmentSubscribeCartBinding

class SubscribeCart : Fragment() {

    private var _binding: FragmentSubscribeCartBinding? = null
    private val binding get() = _binding!!

    private var isAllSelected = false
    private val itemChecked = mutableListOf(false, false, false, false) // 체크 상태
    private val itemCounts = mutableListOf(1, 1, 1, 1) // 기본 1인분

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribeCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전체 선택 버튼 클릭 이벤트
        binding.imageView7.setOnClickListener {
            isAllSelected = !isAllSelected
            val newImageRes = if (isAllSelected) R.drawable.check_on else R.drawable.real_add
            binding.imageView7.setImageResource(newImageRes)

            // 모든 체크박스 상태 변경
            for (i in itemChecked.indices) {
                itemChecked[i] = isAllSelected
            }
            updateItemCheckState()
        }

        // 개별 체크박스 클릭 이벤트
        val checkBoxes = listOf(binding.imgCheck1, binding.imgCheck2, binding.imgCheck3, binding.imgCheck4)
        checkBoxes.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                itemChecked[index] = !itemChecked[index]
                imageView.setImageResource(if (itemChecked[index]) R.drawable.check_on else R.drawable.real_add)

                // 전체 선택 여부 확인
                isAllSelected = itemChecked.all { it }
                binding.imageView7.setImageResource(if (isAllSelected) R.drawable.check_on else R.drawable.real_add)
            }
        }

        // 수량 증가 버튼 클릭 이벤트
        val addButtons = listOf(binding.imgAdd1, binding.imgAdd2, binding.imgAdd3, binding.imgAdd4)
        val minusButtons = listOf(binding.imgMinus1, binding.imgMinus2, binding.imgMinus3, binding.imgMinus4)
        val textCounts = listOf(binding.txtCount1, binding.txtCount2, binding.txtCount3, binding.txtCount4)

        addButtons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (itemCounts[index] < 2) {
                    itemCounts[index] = 2
                    textCounts[index].text = "2인분"
                }
            }
        }

        // 수량 감소 버튼 클릭 이벤트
        minusButtons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (itemCounts[index] > 1) {
                    itemCounts[index] = 1
                    textCounts[index].text = "1인분"
                }
            }
        }
    }

    // 체크 상태 UI 업데이트 함수
    private fun updateItemCheckState() {
        val checkBoxes = listOf(binding.imgCheck1, binding.imgCheck2, binding.imgCheck3, binding.imgCheck4)
        checkBoxes.forEachIndexed { index, imageView ->
            imageView.setImageResource(if (itemChecked[index]) R.drawable.check_on else R.drawable.real_add)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
