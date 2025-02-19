package com.example.umc.Signin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.umc.Main.MainActivity
import com.example.umc.UserApi.APi.KakaoAuthService
import com.example.umc.UserApi.KakaoLoginManager
import com.example.umc.UserApi.Response.AuthResponse
import com.example.umc.UserApi.SharedPreferencesManager
import com.example.umc.UserApi.UserRepository
import com.example.umc.databinding.FragmentSigninBinding
import kotlinx.coroutines.launch

// 기존 Result 클래스를 제거하고 직접 만든 sealed 클래스로 대체
sealed class LoginResult<out T> {
    data class Success<T>(val data: T) : LoginResult<T>()
    data class Error(val exception: Exception) : LoginResult<Nothing>()
}

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var kakaoLoginManager: KakaoLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // KakaoLoginManager 초기화
        kakaoLoginManager = KakaoLoginManager(
            authService = KakaoAuthService.create(),
            context = applicationContext
        )

        setupKakaoLoginButton()
    }

    private fun setupKakaoLoginButton() {
        binding.kakaologin.setOnClickListener {
            startKakaoLogin()
        }
    }

    private fun startKakaoLogin() {
        // 로그인 화면 숨기기
        binding.loginConstraintLayout.visibility = View.GONE

        // WebView 설정
        val webView = binding.kakaoWebView
        webView.apply {
            visibility = View.VISIBLE
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        // 카카오 로그인 콜백 URL 확인
                        if (url.startsWith("your-custom-scheme://kakao-login")) {
                            val uri = Uri.parse(url)
                            val authCode = uri.getQueryParameter("code")

                            authCode?.let { code ->
                                lifecycleScope.launch {
                                    handleKakaoAuthCode(code)
                                }
                            }

                            // WebView와 로그인 화면 상태 복원
                            webView.visibility = View.GONE
                            binding.loginConstraintLayout.visibility = View.VISIBLE
                            return true
                        }
                    }
                    return false
                }
            }

            // 카카오 로그인 URL 로드
            loadUrl(kakaoLoginManager.getKakaoAuthUrl())
        }
    }

    private suspend fun handleKakaoAuthCode(authCode: String) {
        try {
            val result: LoginResult<AuthResponse> = kakaoLoginManager.handleKakaoLogin(authCode)
            when (result) {
                is LoginResult.Success -> {
                    val authResponse: AuthResponse = result.data
                    val token = authResponse.accessToken
                    val userId = authResponse.userId

                    token?.let { accessToken ->
                        UserRepository.saveAuthToken(this, accessToken)

                        // 문자열 userId를 정수로 변환
                        userId?.let { userIdString ->
                            try {
                                val userIdInt = userIdString.toInt()
                                SharedPreferencesManager.saveUserId(this, userIdInt)
                            } catch (e: NumberFormatException) {
                                Log.e("LoginActivity", "Invalid userId format", e)
                            }
                        }

                        navigateToMain(accessToken)
                    } ?: run {
                        Toast.makeText(this, "토큰이 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
                is LoginResult.Error -> {
                    // 로그인 실패 처리
                    Toast.makeText(
                        this,
                        "로그인 실패: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginActivity", "Login Error", result.exception)
                }
                // 모든 가능한 분기를 처리하기 위한 else 브랜치 추가
                else -> {
                    Toast.makeText(
                        this,
                        "알 수 없는 로그인 결과",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginActivity", "Unexpected login result: $result")
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "로그인 중 오류 발생", Toast.LENGTH_SHORT).show()
            Log.e("LoginActivity", "Unexpected error", e)
        }
    }
    private fun navigateToMain(accessToken: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            // 명시적으로 문자열로 전달
            putExtra("ACCESS_TOKEN", accessToken)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}