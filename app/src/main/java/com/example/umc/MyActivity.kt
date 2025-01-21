package com.example.umcproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)
        updateDots(dotsLayout, 3) // 네 번째 점 활성화

        // "이거먹자 마이" 텍스트뷰 색상 변경
        val textView3 = findViewById<TextView>(R.id.textView3)
        setDietTextColor(textView3)

        //뒤로가기 버튼
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, SubscribeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateDots(dotsLayout: LinearLayout, activeIndex: Int) {
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i)
            if (i == activeIndex) {
                dot.setBackgroundResource(R.drawable.dot_active) // 활성화 점
            } else {
                dot.setBackgroundResource(R.drawable.dot_inactive) // 비활성화 점
            }
        }
    }

    private fun setDietTextColor(textView: TextView) {
        val text = "이거먹자 마이"
        val spannableString = SpannableString(text)

        // "이거먹자"에 색상 적용 (#5C5C5C)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#5C5C5C")),
            0, 4, // "이거먹자"의 인덱스 범위
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // "마이"에 색상 적용 (#FF7300)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF7300")),
            5, 7, // "식단"의 인덱스 범위
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }
}