package com.example.umc.Quote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.databinding.FragmentFoodPriceBinding

class FoodPriceFragment : Fragment() {

    private var _binding: FragmentFoodPriceBinding? = null
    private val binding get() = _binding!!

    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodUnit: String? = null
    private var priceRate: String? = null
    private var pricePercent: String? = null

    companion object {
        private const val ARG_FOOD_NAME = "food_name"
        private const val ARG_FOOD_PRICE = "food_price"
        private const val ARG_FOOD_UNIT = "food_unit"
        private const val ARG_PRICE_RATE = "price_rate"
        private const val ARG_PRICE_PERCENT = "price_percent"

        fun newInstance(
            foodName: String,
            foodPrice: String,
            foodWeight: String,
            priceRate: String,
            pricePercent: String
        ) = FoodPriceFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FOOD_NAME, foodName)
                putString(ARG_FOOD_PRICE, foodPrice)
                putString(ARG_FOOD_UNIT, foodUnit)
                putString(ARG_PRICE_RATE, priceRate)
                putString(ARG_PRICE_PERCENT, pricePercent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            foodName = it.getString(ARG_FOOD_NAME)
            foodPrice = it.getString(ARG_FOOD_PRICE)
            foodUnit = it.getString(ARG_FOOD_UNIT)
            priceRate = it.getString(ARG_PRICE_RATE)
            pricePercent = it.getString(ARG_PRICE_PERCENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터를 UI에 설정
        binding.tvFoodName.text = foodName
        binding.tvFoodPrice.text = foodPrice
        binding.tvFoodUnit.text = foodUnit
        binding.tvPriceRate.text = priceRate
        binding.tvPricePercent.text = pricePercent
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
