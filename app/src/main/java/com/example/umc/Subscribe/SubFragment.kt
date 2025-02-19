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
    // View Binding을 안전하게 관리합니다
    private var _binding: FragmentSubBinding? = null
    private val binding get() = _binding!!

    // RecyclerView의 어댑터입니다
    private lateinit var subAdapter: SubAdapter

    // ViewModel을 초기화합니다. 의존성 주입을 통해 Repository를 전달합니다
    private val viewModel: SubViewModel by viewModels {
        val api = RetrofitClient.deliveryAddressApi // API 인스턴스를 가져옵니다
        SubViewModelFactory(SubRepository(api, requireContext()))
    }

    // Fragment의 레이아웃을 생성하고 초기화합니다
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    // View가 생성된 후 필요한 설정들을 수행합니다
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SubFragment", "Fragment 생성됨")

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        // API를 통해 카테고리 데이터를 로드합니다
        viewModel.loadMealCategories()
    }

    // RecyclerView를 설정하는 메서드입니다
    private fun setupRecyclerView() {
        subAdapter = SubAdapter(emptyList()) { subItem ->
            // 클릭 이벤트 발생 시 상세 화면으로 이동합니다
            if (subItem.isClickable) {
                Log.d("SubFragment", "카테고리 클릭: ${subItem.item1}")
                navigateToDietSub(subItem)
            }
        }

        binding.rvSubItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subAdapter
        }
    }

    // 클릭 이벤트 리스너들을 설정합니다
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

    // ViewModel의 상태 변화를 관찰하는 메서드입니다
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mealCategories.collect { categories ->
                Log.d("SubFragment", "카테고리 데이터 수신: ${categories.size}개")
                subAdapter.updateItems(categories)
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.isLoading.collect { isLoading ->
//                Log.d("SubFragment", "로딩 상태: $isLoading")
//                binding.progressBar?.isVisible = isLoading
//            }
//        }

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

    // 식단 상세 화면으로 이동하는 메서드입니다
    private fun navigateToDietSub(subItem: SubItem) {
        val fragmentDietSubFragment = DietSubFragment().apply {
            arguments = Bundle().apply {
                putString("item1", subItem.item1)
                putString("item2", subItem.item2)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragmentDietSubFragment)
            .addToBackStack(null)
            .commit()
    }

    // 구독 관리 화면으로 이동하는 메서드입니다
    private fun navigateToSubscriptionManage() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscriptionManageFragment())
            .addToBackStack(null)
            .commit()
    }

    // 장바구니 화면으로 이동하는 메서드입니다
    private fun navigateToCart() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscribeCart())
            .addToBackStack(null)
            .commit()
    }

    // 로그인 화면으로 이동하는 메서드입니다
    private fun navigateToLogin() {
        // TODO: 로그인 화면으로 이동하는 로직을 구현해야 합니다
        Toast.makeText(context, "로그인이 필요합니다", Toast.LENGTH_LONG).show()
    }

    // 에러 메시지를 표시하는 메서드입니다
    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    // Fragment가 파괴될 때 호출되는 메서드입니다
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수를 방지합니다
    }

    // Fragment가 재개될 때 호출되는 메서드입니다
    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideTitle()
    }
}