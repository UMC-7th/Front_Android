package com.example.umc.Quote

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.umc.R
import com.example.umc.databinding.FragmentFoodPriceBinding
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.model.response.KamisPriceResponse
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FoodPriceFragment : Fragment() {

    private var _binding: FragmentFoodPriceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodPriceViewModel by viewModels()

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
            foodUnit: String,
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터를 UI에 설정
        binding.tvFoodName.text = foodName
        binding.tvFoodPrice.text = foodPrice
        binding.tvFoodUnit.text = foodUnit
        binding.tvPriceRate.text = priceRate
        binding.tvPricePercent.text = pricePercent
        binding.ibtBuy.setOnClickListener {
            foodName?.let { name ->
                val encodedFoodName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                val url = "https://www.coupang.com/np/search?component=&q=$encodedFoodName&channel=user"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } ?: Toast.makeText(requireContext(), "상품명이 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 이미지 로드 호출
        if (!foodName.isNullOrEmpty()) {
            loadMaterialImage(foodName!!)
        }

        /*
                viewModel.priceData.observe(viewLifecycleOwner, Observer { response ->
                    response?.let {
                        val prices = listOf(
                            FoodPriceViewModel.Price(LocalDate.parse(it.yyyy).toEpochDay(), it.d0.toFloat()),
                            FoodPriceViewModel.Price(LocalDate.parse(it.yyyy).minusDays(10).toEpochDay(), it.d10.toFloat()),
                            FoodPriceViewModel.Price(LocalDate.parse(it.yyyy).minusDays(20).toEpochDay(), it.d20.toFloat()),
                            FoodPriceViewModel.Price(LocalDate.parse(it.yyyy).minusDays(30).toEpochDay(), it.d30.toFloat()),
                            FoodPriceViewModel.Price(LocalDate.parse(it.yyyy).minusDays(40).toEpochDay(), it.d40.toFloat())
                        )
                        setChart(prices)
                    }
                })
        // API 호출 변경 (올바른 메서드 사용)
                viewModel.fetchRecentlyPriceTrendList(
                    "20250201", // 검색일자
                    "1177e9f8-8f03-45ec-9cef-318101246a8d", // 인증 키
                    "rmarkdalswn@naver.com", // 요청자 id
                    "XML", // return type
                    "212" // 품목 코드
                )
        */

    }

    private fun loadMaterialImage(foodName: String) {
        binding.imgPriceFood.setImageDrawable(null)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.imageApiService.getMaterialImage(foodName)

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.success?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.imgPriceFood) // 이미지 뷰에 적용
                        Log.d("FoodImage", "이미지 로드 성공: $imageUrl")
                    } else {
                        Log.e("FoodImage", "이미지 URL이 비어 있음")
                        Toast.makeText(context, "이미지 URL이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FoodImage", "API 호출 실패: ${response.message()}")
                    Toast.makeText(context, "API 호출 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("FoodImage", "네트워크 오류: ${e.message}")
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
        @RequiresApi(Build.VERSION_CODES.O)
        private fun setChart(prices: List<FoodPriceViewModel.Price>) {
            val lineChart: LineChart = binding.lineChart
            lineChart.invalidate()
            lineChart.clear()

            val values = prices.map { Entry(it.dateTime.toFloat(), it.price) }
            val lineDataSet = LineDataSet(values, "가격 변동").apply {
                color = ContextCompat.getColor(requireContext(), R.color.Blue)
                setCircleColor(ContextCompat.getColor(requireContext(), R.color.Blue))
                circleHoleColor = ContextCompat.getColor(requireContext(), R.color.white)
                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                lineWidth = 3f
                circleRadius = 6f
                circleHoleRadius = 3f
            }

            val lineData = LineData(lineDataSet).apply {
                setValueTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                setValueTextSize(9f)
            }

            val xAxis = lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val date = LocalDate.ofEpochDay(value.toLong())
                        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    }
                }
                setLabelCount(7, true)
                textColor = ContextCompat.getColor(requireContext(), R.color.black)
                gridColor = ContextCompat.getColor(requireContext(), R.color.black)
                labelRotationAngle = -30f
                setDrawGridLines(false)
            }

            lineChart.axisLeft.setLabelCount(4, true)
            lineChart.axisRight.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            lineChart.description = null
            lineChart.legend.isEnabled = false
            lineChart.data = lineData
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }*/
}