package com.example.umc.Subscribe

import SubRepository
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.Subscribe.SubcribeApi.DeliveryAddressApi
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.cart.SubscribeCart
import com.example.umc.databinding.FragmentSubBinding
import com.example.umc.model.SubItem
import kotlinx.coroutines.launch

class SubFragment : Fragment() {
    private var _binding: FragmentSubBinding? = null
    private val binding get() = _binding!!
    private lateinit var subAdapter: SubAdapter

    private val viewModel: SubViewModel by viewModels {
        val api = RetrofitClient.deliveryAddressApi
        SubViewModelFactory(SubRepository(api, requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SubFragment", "Fragment 생성됨")

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        subAdapter = SubAdapter(emptyList()) { subItem ->
            onCategoryClick(subItem)
        }

        binding.rvSubItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subAdapter
        }
    }

    private fun setupClickListeners() {
        binding.llManageSubscription.setOnClickListener {
            Log.d("SubFragment", "구독 관리 클릭")
            navigateToSubscriptionManage()
        }

        binding.llCart.setOnClickListener {
            Log.d("SubFragment", "장바구니 클릭")
            navigateToCart()
        }
    }

    private fun setupObservers() {
        // 카테고리 목록 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mealCategories.collect { categories ->
                Log.d("SubFragment", "카테고리 데이터 수신: ${categories.size}개")
                subAdapter.updateItems(categories)
            }
        }

        // 맛있는 일상 음식 데이터 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyMeals.collect { meals ->
                Log.d("SubFragment", "일상 음식 데이터 업데이트: ${meals.size}개")
                // 필요한 UI 업데이트 처리
            }
        }

        // 다이어트 식단 데이터 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dietMeals.collect { meals ->
                Log.d("SubFragment", "다이어트 식단 데이터 업데이트: ${meals.size}개")
                // 필요한 UI 업데이트 처리
            }
        }

        // 건강 음식 데이터 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.healthMeals.collect { meals ->
                Log.d("SubFragment", "건강 음식 데이터 업데이트: ${meals.size}개")
                // 필요한 UI 업데이트 처리
            }
        }

//        // 로딩 상태 관찰
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.isLoading.collect { isLoading ->
//                Log.d("SubFragment", "로딩 상태: $isLoading")
//                binding.progressBar?.isVisible = isLoading
//            }
//        }

        // 에러 상태 관찰
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    Log.e("SubFragment", "에러 발생: $it")
                    if (it.contains("인증이 만료되었습니다")) {
                        navigateToLogin()
                    } else {
                        showError(it)
                    }
                }
            }
        }
    }

    private fun onCategoryClick(subItem: SubItem) {
        Log.d("SubFragment", "카테고리 클릭: ${subItem.item1}")
        if (subItem.isClickable) {
            // 먼저 카테고리의 데이터를 로드
            viewModel.loadMealsForCategory(subItem.item1)

            // DietSubFragment로 이동
            val dietSubFragment = DietSubFragment().apply {
                arguments = Bundle().apply {
                    putString("item1", subItem.item1)
                    putString("item2", subItem.item2)
                }
            }

            // 명시적으로 트랜잭션 수행
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_container, dietSubFragment)
                .addToBackStack(null)  // 백 스택에 추가
                .commit()

            Log.d("SubFragment", "DietSubFragment로 전환 시도")
        } else {
            Log.d("SubFragment", "클릭 불가능한 카테고리입니다.")
        }
    }

    private fun navigateToSubscriptionManage() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscriptionManageFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToCart() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscribeCart())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToLogin() {
        Toast.makeText(context, "로그인이 필요합니다", Toast.LENGTH_LONG).show()
        // 로그인 화면으로 이동하는 로직 구현
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideTitle()
    }
}