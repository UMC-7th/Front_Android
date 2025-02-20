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
import java.time.format.DateTimeParseException

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

        // 데이터 설정
        binding.tvFoodName.text = foodName
        binding.tvFoodPrice.text = foodPrice
        binding.tvFoodUnit.text = foodUnit
        binding.tvPriceRate.text = priceRate
        binding.tvPricePercent.text = pricePercent

        // 구매 링크 버튼 클릭
        binding.ibtBuy.setOnClickListener {
            foodName?.let { name ->
                val encodedFoodName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                val url =
                    "https://www.coupang.com/np/search?component=&q=$encodedFoodName&channel=user"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } ?: Toast.makeText(requireContext(), "상품명이 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 이미지 로드 호출
        if (!foodName.isNullOrEmpty()) {
            loadMaterialImage(foodName!!)
        }

        viewModel.fetchRecentlyPriceTrendList()

        viewModel.priceData.observe(viewLifecycleOwner, Observer { response ->
            response?.let { kamisResponse ->
                val prices = kamisResponse.price.mapNotNull { priceItem ->
                    if (priceItem.yyyy == "평년") {
                        Log.e("DateParsing", "Skipping invalid date: ${priceItem.yyyy}")
                        return@mapNotNull null // "평년" 데이터 제외
                    }

                    val formattedDate = if (priceItem.yyyy.length == 4) "${priceItem.yyyy}-01-01" else priceItem.yyyy

                    val localDate = try {
                        LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    } catch (e: DateTimeParseException) {
                        Log.e("DateParsing", "Failed to parse date: $formattedDate", e)
                        return@mapNotNull null // 날짜 파싱 실패 시 제외
                    }

                    val d40Value = priceItem.d40.toString().toFloatOrNull() ?: 0f

                    Log.d("ParsedPriceData", "Date: $formattedDate, Price: $d40Value")

                    // ✅ 기존 Price 객체 대신 Pair<LocalDate, Float> 사용
                    Pair(localDate, d40Value)
                }

                setChart(prices) // ✅ 이제 setChart()에 맞는 타입으로 전달됨
            }
        })
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
                    Toast.makeText(context, "API 호출 실패: ${response.message()}", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Log.e("FoodImage", "네트워크 오류: ${e.message}")
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setChart(prices: List<Pair<LocalDate, Float>>) {
        val lineChart: LineChart = binding.lineChart
        lineChart.invalidate()
        lineChart.clear()

        val entries = prices.mapIndexed { index, (date, price) ->
            Entry(index.toFloat(), price)
        }

        val lineDataSet = LineDataSet(entries, "가격 변동").apply {
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
                    return prices.getOrNull(value.toInt())?.first?.format(
                        DateTimeFormatter.ofPattern(
                            "MM-dd"
                        )
                    ) ?: ""
                }
            }
            setLabelCount(5, true)
            textColor = ContextCompat.getColor(requireContext(), R.color.black)
            gridColor = ContextCompat.getColor(requireContext(), R.color.black)
            labelRotationAngle = -45f
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
}

    data class Price(
    val dateTime: Long,
    val price: Float
)