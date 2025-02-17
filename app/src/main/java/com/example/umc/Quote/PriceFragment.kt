package com.example.umc

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.Diet.DietDetailFragment
import com.example.umc.Main.MainActivity
import com.example.umc.Quote.FoodPriceFragment
import com.example.umc.Quote.PriceAdapter
import com.example.umc.Quote.Sub.QuoteFragmentSub
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

//        // 텍스트 스타일 적용 예제
//        val textView = binding.textcolor1 // Fragment의 레이아웃에 텍스트뷰가 있어야 합니다.
//        val fullText = "아이콘을 눌러 다양한 식재료의 시세를 볼 수 있어요"
//
//        // SpannableString을 사용하여 텍스트의 일부에 스타일 적용
//        val spannable = SpannableString(fullText)
//
//        // "식재료의 시세" 부분만 빨간색으로 변경
//        val start = fullText.indexOf("식재료의 시세")
//        val end = start + "식재료의 시세".length
//        spannable.setSpan(ForegroundColorSpan(drawab;), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        // 변경된 SpannableString을 TextView에 적용
//        textView.text = spannable
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
            Category(8, "즐겨찾기", R.drawable.ic_star_filled)
        )

        val categoryAdapter = CategoryAdapter(categories) { category ->
            if (category.id == 1) {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.main_container, QuoteFragmentSub())
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
            Product(1, "쌀", 31658, "kg", ""),
            Product(2, "배추", 32658, "kg", ""),
            Product(3, "콩", 32658, "kg", ""),
            Product(4, "김치", 32658, "kg", "")

        )

        val bestAdapter = PriceAdapter(bestProducts) { product ->
            val mainActivity = activity as? MainActivity
            mainActivity?.showTitle(product.name, true)
            mainActivity?.hideBottomBar()
//            기존 코드
//            val foodPriceFragment = FoodPriceFragment()
//            val bundle = Bundle().apply {
//                putString("food_name", product.name)
//                putString("food_price", product.price.toString())
//                putString("price_unit", product.unit)
//                putString("price_percent", "")
//            }
//            foodPriceFragment.arguments = bundle
//
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.main_container, foodPriceFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
            val dietDetailFragment = DietDetailFragment()
            val bundle = Bundle().apply {
                putString("name", product.name)
                putString("calories", product.price.toString())  // 가격을 칼로리 값으로 전달
            }
            dietDetailFragment.arguments = bundle

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, dietDetailFragment)
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
            Product(2, "급식재료 부세피", 0, "kg", ""),
            Product(3, "우리아이 영양간식", 0, "kg", ""),
            Product(4, "혼밥 레시피", 0, "kg", "")
        )

        val hotAdapter = PriceAdapter(hotProducts) { product ->
            val mainActivity = activity as? MainActivity
            mainActivity?.showTitle(product.name, true)
            mainActivity?.hideBottomBar()
              // 기존 코드
//            val foodPriceFragment = FoodPriceFragment()
//            val bundle = Bundle().apply {
//                putString("food_name", product.name)
//                putString("food_price", product.price.toString())
//                putString("price_unit", product.unit)
//                putString("price_percent", "")
//            }
//            foodPriceFragment.arguments = bundle
//
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.replace(R.id.main_container, foodPriceFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
            val dietDetailFragment = DietDetailFragment()
            val bundle = Bundle().apply {
                putString("name", product.name)
                putString("calories", product.price.toString())  // 가격을 칼로리 값으로 전달
            }
            dietDetailFragment.arguments = bundle

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, dietDetailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.hotRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hotAdapter
        }
    }

    private fun setupListeners() {

        // 클릭 리스너 설정 등
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideTitle()
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
