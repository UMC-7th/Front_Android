package com.example.umc.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.umc.Onboarding.OnboardingMainActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 스플래시 스크린 설치는 반드시 super.onCreate 전에
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // 스플래시 화면 유지를 위한 변수
        var keepSplashOnScreen = true

        // 스플래시 화면 유지 조건 설정
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        // 로딩 작업을 시뮬레이션
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashOnScreen = false
            startActivity(Intent(this, OnboardingMainActivity::class.java))
            finish()
        }, 3000) // 3초 딜레이
    }
}

// 여기 위에 코드 원본 코드라 절대로 건들지 말아주세요 !!


//package com.example.umc.Splash
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.appcompat.app.AppCompatActivity
//import com.example.umc.Main.MainActivity
//import com.example.umc.R
//
//class SplashActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.splash)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            // MainActivity로 이동하면서 시작할 프래그먼트 정보 전달
//            val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
//                putExtra("START_FRAGMENT", "MonthlyHomeFragment")
//            // 여기서 원하는 프래그먼트 지정 value값을 자신이 만든 코틀린 프래그먼트 파일 이름 넣으면 됩니다.
//                // 바로바로 가기 위한 용도의 코드에요
//            }
//            startActivity(intent)
//            finish()
//        }, 3000) // 3초
//    }
//}

// 여기 아래 코드는 바로바로 페이지 넘어갈 수 있게 짜놨습니다.