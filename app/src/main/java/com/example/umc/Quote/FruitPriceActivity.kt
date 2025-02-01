package com.example.pricefruit

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FruitPriceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit_price)

        // 🔹 ProgressBar (가로 스크롤 Indicator)
        val scrollIndicator: ProgressBar = findViewById(R.id.scrollIndicator)

        // 🔹 "오늘" 텍스트 및 드롭다운 버튼
        val tvToday: TextView = findViewById(R.id.tv_today)
        val dateSelector: View = findViewById(R.id.date_selector) // 클릭 영역

        val dateList = arrayOf("오늘", "내일", "모레")

        // 🔹 날짜 선택 Dialog (Spinner 제거)
        dateSelector.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("날짜 선택")
                .setItems(dateList) { _, which ->
                    tvToday.text = dateList[which] // 선택된 날짜 설정
                }
                .show()
        }

        // 🔹 가로 RecyclerView (가격 정보)
        val horizontalRecyclerView: RecyclerView = findViewById(R.id.horizontal_recycler_view)
        horizontalRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val priceList = listOf("26,828원", "3166원/kg", "12,500원", "10,900원", "9,800원")
        horizontalRecyclerView.adapter = HorizontalRecyclerViewAdapter(priceList)

        // 🔹 RecyclerView 스크롤 시 ProgressBar 업데이트
        horizontalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val offset = recyclerView.computeHorizontalScrollOffset()
                val extent = recyclerView.computeHorizontalScrollExtent()
                val range = recyclerView.computeHorizontalScrollRange()

                val progress = (offset.toFloat() / (range - extent) * 100).toInt()
                scrollIndicator.progress = progress
            }
        })

        // 🔹 세로 RecyclerView (전체 상품 목록)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val itemList = listOf("상품 1", "상품 2", "상품 3", "상품 4", "상품 5", "상품 6")
        recyclerView.adapter = GridRecyclerViewAdapter(itemList)
    }
}
