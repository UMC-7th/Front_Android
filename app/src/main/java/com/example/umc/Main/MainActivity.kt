package com.example.umc.Main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.umc.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // 먼저 레이아웃을 설정

        bottomNavigationView = findViewById(R.id.bottom_navigation_view) // 이제 뷰를 찾을 수 있습니다
        enableEdgeToEdge()


    }
}