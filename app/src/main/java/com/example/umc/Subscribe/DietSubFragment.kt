

package com.example.umc.Subscribe

import SubRepository
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Diet.DietItem
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.cart.SubscribeCart
import com.example.umc.databinding.FragmentDietSubBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class DietSubFragment : Fragment(), OnDietCheckedChangeListener {
    private var _binding: FragmentDietSubBinding? = null
    private val binding get() = _binding!!

    private lateinit var dailyDietAdapter: SubscribeDietAdapter
    private var category: String? = null

    private val viewModel: SubViewModel by viewModels {
        val api = RetrofitClient.deliveryAddressApi
        SubViewModelFactory(SubRepository(api, requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDietSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = arguments?.getString("item1")
        Log.d("DietSubFragment", "받은 카테고리: $category")

        setupRecyclerView()
        observeData()
        setupClickListeners()

        (activity as? MainActivity)?.let { mainActivity ->
            category?.let {
                mainActivity.showTitle(it, true)
            }
        }
    }

    private fun setupRecyclerView() {
        dailyDietAdapter = SubscribeDietAdapter(emptyList(), this)
        binding.recyclerDailyDiet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dailyDietAdapter
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            category?.let { cat ->
                viewModel.getMealsForCategory(cat).collect { meals ->
                    val dietItems = meals.map { meal ->
                        val mealDate = meal.mealSubs?.firstOrNull()?.mealDate
                        val formattedDate = formatDate(mealDate)
                        DietItem(
                            date = formattedDate,
                            day = getDayOfWeek(mealDate),
                            breakfast = meal.food ?: "메뉴 없음",
                            lunch = meal.lunch ?: "메뉴 없음",
                            dinner = meal.dinner ?: "메뉴 없음"
                        )
                    }
                    dailyDietAdapter.updateItems(dietItems)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun formatDate(dateStr: String?): String {
        if (dateStr.isNullOrEmpty()) return "날짜 없음"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            val date = inputFormat.parse(dateStr)
            outputFormat.format(date ?: return "날짜 없음")
        } catch (e: Exception) {
            Log.e("DietSubFragment", "날짜 변환 실패: $dateStr", e)
            "날짜 없음"
        }
    }

    private fun getDayOfWeek(dateStr: String?): String {
        if (dateStr.isNullOrEmpty()) return "요일 없음"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val calendar = Calendar.getInstance()
            calendar.time = inputFormat.parse(dateStr) ?: return "요일 없음"

            when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> "일"
                Calendar.MONDAY -> "월"
                Calendar.TUESDAY -> "화"
                Calendar.WEDNESDAY -> "수"
                Calendar.THURSDAY -> "목"
                Calendar.FRIDAY -> "금"
                Calendar.SATURDAY -> "토"
                else -> "요일 없음"
            }
        } catch (e: Exception) {
            Log.e("DietSubFragment", "요일 변환 실패: $dateStr", e)
            "요일 없음"
        }
    }

    private fun setupClickListeners() {
        binding.btCart.setOnClickListener {
            Toast.makeText(requireContext(), "장바구니 담기 완료", Toast.LENGTH_SHORT).show()
            navigateToSubscribeCart()
        }
    }

    private fun navigateToSubscribeCart() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscribeCart())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = activity as? MainActivity
        category?.let {
            mainActivity?.showTitle(it, true)
        }
        mainActivity?.showBottomBar()
    }

    override fun onDietCheckedChanged(isAnyChecked: Boolean) {
        val color = if (isAnyChecked) R.color.Primary_Orange1 else R.color.Gray7
        binding.btCart.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }
}