package com.example.umc.Signin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.SignUp.SignUpFragment
import com.example.umc.Survey.SurveyGoalFragment
import com.example.umc.UserApi.APi.KakaoAuthService
import com.example.umc.UserApi.KakaoLoginManager
import com.example.umc.UserApi.Response.AuthResponse
import com.example.umc.UserApi.Response.LoginResponse
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.UserApi.SharedPreferencesManager
import com.example.umc.UserApi.UserRepository
import com.example.umc.databinding.FragmentSigninBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Sealed class for handling different login results
sealed class LoginResult<out T> {
    data class Success<T>(val data: T) : LoginResult<T>()
    data class Error(val exception: Exception) : LoginResult<Nothing>()
}

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var kakaoLoginManager: KakaoLoginManager
    private val userRepository = UserRepository()
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize KakaoLoginManager
        kakaoLoginManager = KakaoLoginManager(
            authService = KakaoAuthService.create(),
            context = applicationContext
        )

        if (savedInstanceState == null) {
            clearBackStack()
        }

        setupUI()
    }

    private fun clearBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    private fun setupUI() {
        // Text watcher for email and password fields
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateLoginButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.emailLoginEditText.addTextChangedListener(textWatcher)
        binding.passwordLoginEditText.addTextChangedListener(textWatcher)

        // Password visibility toggle
        binding.signinvisible.setOnClickListener {
            togglePasswordVisibility()
        }

        // Local login button
        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginEditText.text.toString().trim()
            val password = binding.passwordLoginEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLocalLogin(email, password)
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // Sign up button
        binding.loginButton2.setOnClickListener {
            navigateToSignUpFragment()
        }

        // Kakao login button
        binding.kakaologin.setOnClickListener {
            startKakaoLogin()
        }
    }

    private fun updateLoginButtonState() {
        val email = binding.emailLoginEditText.text.toString().trim()
        val password = binding.passwordLoginEditText.text.toString().trim()

        val isInputValid = email.isNotEmpty() && password.isNotEmpty()
        binding.loginButton.isEnabled = isInputValid
        binding.loginButton.setBackgroundColor(
            if (isInputValid) {
                getColor(R.color.Primary_Orange1)
            } else {
                getColor(R.color.Gray8)
            }
        )
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            binding.passwordLoginEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.signinvisible.setImageResource(R.drawable.ic_eye_visible)
        } else {
            // Show password
            binding.passwordLoginEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.signinvisible.setImageResource(R.drawable.ic_eye_invisible)
        }
        isPasswordVisible = !isPasswordVisible
        binding.passwordLoginEditText.text?.let { binding.passwordLoginEditText.setSelection(it.length) }
    }

    // Local Login Method
    private fun performLocalLogin(email: String, password: String) {
        userRepository.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("Login", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val accessToken = response.body()?.success?.accessToken
                    val userId = response.body()?.success?.user?.userId

                    if (accessToken != null) {
                        UserRepository.saveAuthToken(this@LoginActivity, accessToken)
                        if (userId != null) {
                            SharedPreferencesManager.saveUserData(
                                context = this@LoginActivity,
                                userId = userId,
                                accessToken = accessToken,
                                email = email
                            )
                        }
                        Log.d("LoginAuthToken", "토큰과 userId가 저장되었습니다: $accessToken, $userId")

                        navigateToMain(accessToken)
                    } else {
                        Toast.makeText(this@LoginActivity, "토큰이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "로그인 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login", "Network Error", t)
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Kakao Login Methods
    private fun startKakaoLogin() {
        // Hide login layout
        binding.loginConstraintLayout.visibility = View.GONE

        // Setup WebView for Kakao login
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
                        // Check for Kakao login callback URL
                        if (url.startsWith("your-custom-scheme://kakao-login")) {
                            val uri = Uri.parse(url)
                            val authCode = uri.getQueryParameter("code")

                            authCode?.let { code ->
                                lifecycleScope.launch {
                                    handleKakaoAuthCode(code)
                                }
                            }

                            // Restore WebView and login layout
                            webView.visibility = View.GONE
                            binding.loginConstraintLayout.visibility = View.VISIBLE
                            return true
                        }
                    }
                    return false
                }
            }

            // Load Kakao login URL
            loadUrl(kakaoLoginManager.getKakaoAuthUrl())
        }
    }

    private suspend fun handleKakaoAuthCode(authCode: String) {
        try {
            val result: LoginResult<AuthResponse> = kakaoLoginManager.handleKakaoLogin(authCode)
            when (result) {
                is LoginResult.Success -> {
                    val authResponse: AuthResponse = result.data

                    // Extract data
                    val accessToken = authResponse.accessToken
                    val refreshToken = authResponse.refreshToken
                    val userId = authResponse.userId

                    // Validate all required data
                    if (accessToken != null && userId != null) {
                        val userIdInt = userId.toIntOrNull() ?: run {
                            Log.e("LoginActivity", "Invalid userId format")
                            Toast.makeText(this, "사용자 ID 오류", Toast.LENGTH_SHORT).show()
                            return
                        }

                        // Save comprehensive user data
                        SharedPreferencesManager.saveUserData(
                            context = this,
                            userId = userIdInt,
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )

                        // Navigate to main screen
                        navigateToMain(accessToken)
                    } else {
                        Toast.makeText(this, "로그인 정보가 불완전합니다", Toast.LENGTH_SHORT).show()
                    }
                }
                is LoginResult.Error -> {
                    Toast.makeText(
                        this,
                        "로그인 실패: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginActivity", "Login Error", result.exception)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "로그인 중 오류 발생", Toast.LENGTH_SHORT).show()
            Log.e("LoginActivity", "Unexpected error", e)
        }
    }

    private fun navigateToSignUpFragment() {
        val fragment = SignUpFragment()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    private fun navigateToMain(accessToken: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("ACCESS_TOKEN", accessToken)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}