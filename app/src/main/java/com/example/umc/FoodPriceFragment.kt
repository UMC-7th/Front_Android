package com.example.umc

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.databinding.FragmentFoodPriceBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FoodPriceFragment : Fragment(R.layout.fragment_food_price) {

    private var _binding: FragmentFoodPriceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodPriceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = FoodPriceAdapter(viewModel.midQuality)
        }

        binding.ibtBuy.setOnClickListener {
            binding.ibtBuy.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.Primary_Orange1),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        binding.ibtBuy.postDelayed({
            binding.ibtBuy.clearColorFilter()
        }, 500)

        // 차트 설정
        setChart(viewModel.prices)
    }

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
        _binding = null // Safely nullify binding to avoid memory leaks
    }
}
