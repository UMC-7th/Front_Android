package com.example.umc

import android.os.Bundle
import android.view.View
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

    private fun handleBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        if (currentFragment is DietDetailFragment) {
            hideBottomBar()
            supportFragmentManager.popBackStack()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
        hideTitle()
        showBottomBar()
    }

    // Title을 설정
    fun showTitle(title: String, isBackBtn: Boolean) {
        binding.flTitle.visibility = View.VISIBLE
        binding.tvTitle.text = title
        if (isBackBtn) {
            binding.ibtnBack.visibility = View.VISIBLE
            binding.ibtnBack.setOnClickListener {
                handleBackPressed()
            }
        } else {
            binding.ibtnBack.visibility = View.GONE
        }
    }

    private fun hideTitle() {
        binding.flTitle.visibility = View.GONE
    }

    fun hideBottomBar() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomBar() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}