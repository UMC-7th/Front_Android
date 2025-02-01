package com.example.umc.Onboarding

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.umc.R


class DietActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)

        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)
        updateDots(dotsLayout, 0) // 첫 번째 점 활성화

        // "이거먹자 식단" 텍스트뷰 색상 변경
        val textView3 = findViewById<TextView>(R.id.textView3)
        setDietTextColor(textView3)

        // 다음 버튼
        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            // 버튼 색상을 진회색(#9A9A9A)으로 변경
            nextButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9A9A9A")))

            val intent = Intent(this, PriceActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, OnboardingMainActivity::class.java)
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
        val text = "이거먹자 식단"
        val spannableString = SpannableString(text)

        // "이거먹자"에 색상 적용 (#5C5C5C)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#5C5C5C")),
            0, 4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // "식단"에 색상 적용 (#FF7300)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF7300")),
            5, 7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }
}
