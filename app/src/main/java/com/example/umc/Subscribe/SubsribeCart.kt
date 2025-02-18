package com.example.umc.cart

import Subscribecredit
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.model.CartRequest

import com.example.umc.UserApi.RetrofitClient


import com.example.umc.Subscribe.Subscribecredit

import com.example.umc.databinding.FragmentSubscribeCartBinding
import com.example.umc.model.KartSubRequest
import kotlinx.coroutines.launch

class SubscribeCart : Fragment() {
    private var _binding: FragmentSubscribeCartBinding? = null
    private val binding get() = _binding!!
    private val apiService = RetrofitClient.mealApiService()

    private var isAllSelected = false
    private val itemChecked = mutableListOf(false, false, false, false)
    private val itemCounts = mutableListOf(1, 1, 1, 1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribeCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.creditbutton.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, Subscribecredit())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.imageView7.setOnClickListener {
            isAllSelected = !isAllSelected
            val newImageRes = if (isAllSelected) R.drawable.check_on else R.drawable.real_add
            binding.imageView7.setImageResource(newImageRes)

            for (i in itemChecked.indices) {
                itemChecked[i] = isAllSelected
            }
            updateItemCheckState()
        }

        val checkBoxes = listOf(binding.imgCheck1, binding.imgCheck2, binding.imgCheck3, binding.imgCheck4)
        checkBoxes.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                itemChecked[index] = !itemChecked[index]
                imageView.setImageResource(if (itemChecked[index]) R.drawable.check_on else R.drawable.real_add)

                isAllSelected = itemChecked.all { it }
                binding.imageView7.setImageResource(if (isAllSelected) R.drawable.check_on else R.drawable.real_add)
            }
        }

        val addButtons = listOf(binding.imgAdd1, binding.imgAdd2, binding.imgAdd3, binding.imgAdd4)
        val minusButtons = listOf(binding.imgMinus1, binding.imgMinus2, binding.imgMinus3, binding.imgMinus4)
        val textCounts = listOf(binding.txtCount1, binding.txtCount2, binding.txtCount3, binding.txtCount4)

        addButtons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (itemCounts[index] < 2) {
                    lifecycleScope.launch {
                        try {
                            Log.d("API_TEST", "API 호출 시작 - index: $index")
                            val response = apiService.addToCart(
                                CartRequest(
                                    KartSubRequest(
                                        mealSubId = index + 1,
                                        cnt = 2
                                    )
                                )
                            )
                            Log.d("API_TEST", "API 응답: ${response.body()}")
                            Log.d("API_TEST", "에러 응답: ${response.errorBody()?.string()}")
                            Log.d("API_TEST", "성공 여부: ${response.isSuccessful}")

                            if (response.isSuccessful) {
                                itemCounts[index] = 2
                                textCounts[index].text = "2인분"
                            }
                        } catch (e: Exception) {
                            Log.e("API_TEST", "API 오류 발생: ${e.message}")
                        }
                    }
                }
            }
        }

        minusButtons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (itemCounts[index] > 1) {
                    lifecycleScope.launch {
                        try {
                            val response = apiService.addToCart(
                                CartRequest(
                                    KartSubRequest(
                                        mealSubId = index + 1,
                                        cnt = 1
                                    )
                                )
                            )
                            Log.d("API", "Success: ${response.body()}")
                            if (response.isSuccessful) {
                                itemCounts[index] = 1
                                textCounts[index].text = "1인분"
                                Toast.makeText(context, "API 성공: 수량 감소", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("API", "Error: ${response.errorBody()?.string()}")
                                Toast.makeText(context, "API 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Log.e("API", "Exception: ${e.message}")
                            Toast.makeText(context, "API 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

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

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle("장바구니", true)
        (activity as? MainActivity)?.showBottomBar()
    }
}