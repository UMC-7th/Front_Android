package com.example.umc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.umc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge()
        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_home
        }
    }

//    // 시작 버튼 클릭 이벤트 설정
//    val startButton = findViewById<Button>(R.id.startButton)
//    startButton.setOnClickListener {
//        val intent = Intent(this, DietActivity::class.java)
//        startActivity(intent) //DietActivity로 이동
//    }
//}


    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, HomeContainerFragment())
                        .commit()
                    true
                }
                R.id.fragment_price -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, PriceFragment())
                        .commit()
                    true
                }
                R.id.fragment_sub -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, SubFragment())
                        .commit()
                    true
                }
                R.id.fragment_my -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, MyFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}