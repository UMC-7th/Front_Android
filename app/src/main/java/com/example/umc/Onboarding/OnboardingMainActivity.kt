package com.example.umc.Onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc.Onboarding.DietActivity // DietActivity 패키지 확인
import com.example.umc.Signin.LoginActivity
import com.example.umc.databinding.ActivityOnboardingmainBinding

class OnboardingMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingmainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingmainBinding.inflate(layoutInflater) // View Binding 설정 확인
        setContentView(binding.root)

        // 시작 버튼 클릭 시 DietActivity로 이동
        binding.startButton.setOnClickListener {
            val intent = Intent(this, DietActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener({
            val intent =Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}
