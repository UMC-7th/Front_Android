package com.example.umc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Main.MainActivity
import com.example.umc.Quote.FoodPriceFragment
import com.example.umc.Quote.QuoteFragmentSub
import com.example.umc.databinding.FragmentPriceBinding
import com.example.umc.model.Category
import com.example.umc.model.Product

class PriceFragment : Fragment() {

    private var _binding: FragmentPriceBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PriceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        setupCategoryRecyclerView()
        setupBestRecyclerView()
        setupHotRecyclerView()
    }

    private fun setupCategoryRecyclerView() {
        val categories = listOf(
            Category(1, "제철", R.drawable.ic_meta),
            Category(2, "식량작물", R.drawable.ic_gluten),
            Category(3, "특용작물", R.drawable.ic_mushroom),
            Category(4, "과일류", R.drawable.ic_banana),
            Category(5, "수산물", R.drawable.ic_crab),
            Category(6, "축산물", R.drawable.ic_beef),
            Category(7, "식품", R.drawable.ic_dobu),
            Category(8, "즐겨찾기", R.drawable.ic_star)
        )

        val categoryAdapter = CategoryAdapter(categories) { category ->
            if (category.id == 1) {  // "제철" 카테고리 클릭 시 Fragment 전환
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.main_container, QuoteFragmentSub()) // 새로운 Fragment로 이동
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        binding.categoryRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = categoryAdapter
        }
    }


    private fun setupBestRecyclerView() {
        val bestProducts = listOf(
            Product(1, "공식 시세", 31658, "kg", ""),
            Product(2, "공식 시세", 32658, "kg", "")
        )
        val bestAdapter = ProductAdapter(bestProducts) { product ->
            val mainActivity = activity as? MainActivity
            mainActivity?.showTitle(product.name, true) // 제목 보이게
            mainActivity?.hideBottomBar() // 하단 네비게이션 바 숨기기

            // Create the FoodPriceFragment and pass the selected product data as arguments
            val foodPriceFragment = FoodPriceFragment()
            val bundle = Bundle().apply {
                putString("food_name", product.name)
                putString("food_price", product.price.toString())
                putString("price_unit", product.unit)  // or any other additional data
                putString("price_percent", "") // Example, change as needed
            }
            foodPriceFragment.arguments = bundle

            // Replace the current fragment with FoodPriceFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, foodPriceFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.bestRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bestAdapter
        }
    }

    private fun setupHotRecyclerView() {
        val hotProducts = listOf(
            Product(1, "어묵류 김말이피", 0, "kg", ""),
            Product(2, "급식재료 부세피", 0, "kg", "")
        )

        val hotAdapter = ProductAdapter(hotProducts) { product ->
            val mainActivity = activity as? MainActivity
            mainActivity?.showTitle(product.name, true) // 제목 보이게
            mainActivity?.hideBottomBar() // 하단 네비게이션 바 숨기기

            // Create the FoodPriceFragment and pass the selected product data as arguments
            val foodPriceFragment = FoodPriceFragment()
            val bundle = Bundle().apply {
                putString("food_name", product.name)
                putString("food_price", product.price.toString())
                putString("price_unit", product.unit)  // or any other additional data
                putString("price_percent", "") // Example, change as needed
            }
            foodPriceFragment.arguments = bundle

            // Replace the current fragment with FoodPriceFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, foodPriceFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.hotRecyclerView.apply {
            layoutManager = LinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
            adapter = hotAdapter
        }
    }

    private fun setupListeners() {

        // 클릭 리스너 설정 등
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}