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
import com.example.umc.UserApi.APi.KakaoAuthService
import com.example.umc.UserApi.KakaoLoginManager
import com.example.umc.UserApi.Response.AuthResponse
import com.example.umc.UserApi.Response.LoginResponse
import com.example.umc.UserApi.SharedPreferencesManager
import com.example.umc.UserApi.UserRepository
import com.example.umc.databinding.FragmentSigninBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 로그인 결과를 처리하는 봉인된 클래스
sealed class LoginResult<out T> {
    data class Success<T>(val data: T) : LoginResult<T>()
    data class Error(val exception: Exception, val code: Int? = null) : LoginResult<Nothing>()
}

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var kakaoLoginManager: KakaoLoginManager
    private val userRepository = UserRepository()
    private var isPasswordVisible = false

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // KakaoLoginManager 초기화
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
        supportFragmentManager.popBackStack()
    }

    private fun setupUI() {
        // 이메일과 비밀번호 입력 감지를 위한 텍스트 와쳐
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateLoginButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        binding.emailLoginEditText.addTextChangedListener(textWatcher)
        binding.passwordLoginEditText.addTextChangedListener(textWatcher)

        // 비밀번호 가시성 토글
        binding.signinvisible.setOnClickListener {
            togglePasswordVisibility()
        }

        // 로컬 로그인 버튼
        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginEditText.text.toString().trim()
            val password = binding.passwordLoginEditText.text.toString().trim()

            when {
                email.isEmpty() -> showError("이메일을 입력하세요.")
                password.isEmpty() -> showError("비밀번호를 입력하세요.")
                else -> performLocalLogin(email, password)
            }
        }

        // 회원가입 버튼
        binding.loginButton2.setOnClickListener {
            navigateToSignUpFragment()
        }

        // 카카오 로그인 버튼
        binding.kakaologin.setOnClickListener {
            startKakaoLogin()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateLoginButtonState() {
        val email = binding.emailLoginEditText.text.toString().trim()
        val password = binding.passwordLoginEditText.text.toString().trim()

        val isInputValid = email.isNotEmpty() && password.isNotEmpty()
        binding.loginButton.apply {
            isEnabled = isInputValid
            setBackgroundColor(
                getColor(if (isInputValid) R.color.Primary_Orange1 else R.color.Gray8)
            )
        }
    }

    private fun togglePasswordVisibility() {
        binding.passwordLoginEditText.apply {
            transformationMethod = if (isPasswordVisible) {
                PasswordTransformationMethod.getInstance()
            } else {
                HideReturnsTransformationMethod.getInstance()
            }
            text?.let { setSelection(it.length) }
        }

        binding.signinvisible.setImageResource(
            if (isPasswordVisible) R.drawable.ic_eye_visible
            else R.drawable.ic_eye_invisible
        )
        isPasswordVisible = !isPasswordVisible
    }

    // 로컬 로그인 메서드
    private fun performLocalLogin(email: String, password: String) {
        userRepository.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(TAG, "Local Login Response Code: ${response.code()}")

                when {
                    response.isSuccessful -> handleSuccessfulLocalLogin(response, email)
                    else -> handleFailedLocalLogin(response)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Local Login Network Error", t)
                showError("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun handleSuccessfulLocalLogin(response: Response<LoginResponse>, email: String) {
        val loginSuccess = response.body()?.success
        val accessToken = loginSuccess?.accessToken
        val userId = loginSuccess?.user?.userId

        when {
            accessToken != null -> {
                userId?.let {
                    SharedPreferencesManager.saveUserData(
                        context = this,
                        userId = it,
                        accessToken = accessToken,
                        email = email
                    )
                    Log.d(TAG, "토큰과 userId가 저장되었습니다: $accessToken, $userId")
                    navigateToMain(accessToken)
                } ?: showError("사용자 ID를 찾을 수 없습니다.")
            }
            else -> showError("토큰이 없습니다.")
        }
    }

    private fun handleFailedLocalLogin(response: Response<LoginResponse>) {
        val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류"
        showError("로그인 실패: $errorMessage")
    }

    // 카카오 로그인 메서드
    private fun startKakaoLogin() {
        binding.apply {
            loginConstraintLayout.visibility = View.GONE
            kakaoWebView.apply {
                visibility = View.VISIBLE
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }
                webViewClient = createKakaoWebViewClient()
                loadUrl(kakaoLoginManager.getKakaoAuthUrl())
            }
        }
    }

    private fun createKakaoWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Log.e(TAG, "WebView Error: $errorCode - $description")
                view?.let { webView ->
                    loadErrorPage(
                        webView,
                        errorCode,
                        description ?: "알 수 없는 오류",
                        failingUrl ?: "알 수 없는 URL"
                    )
                }
                restoreLoginLayout()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    // 리다이렉트 URI를 정확히 일치시킴
                    val redirectUri = "http://3.38.39.238:3000/auth/kakao/callback"

                    if (url.startsWith(redirectUri)) {
                        val uri = Uri.parse(url)
                        val authCode = uri.getQueryParameter("code")

                        authCode?.let { code ->
                            lifecycleScope.launch {
                                handleKakaoAuthCode(code)
                            }
                        }
                        hideWebView()
                        return true
                    }
                }
                return false
            }

            private fun loadErrorPage(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                val htmlData = """
                    <html>
                    <body style="text-align: center; font-family: Arial, sans-serif; padding-top: 50px;">
                        <h1>네트워크 오류</h1>
                        <p>페이지를 로드할 수 없습니다.</p>
                        <p>오류 코드: $errorCode</p>
                        <p>설명: $description</p>
                        <p>URL: $failingUrl</p>
                        <button onclick="window.location.reload()">다시 시도</button>
                    </body>
                    </html>
                """.trimIndent()

                view.loadDataWithBaseURL(
                    null,
                    htmlData,
                    "text/html",
                    "UTF-8",
                    null
                )
            }

            private fun restoreLoginLayout() {
                binding.loginConstraintLayout.visibility = View.VISIBLE
            }

            private fun hideWebView() {
                binding.apply {
                    kakaoWebView.visibility = View.GONE
                    loginConstraintLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private suspend fun handleKakaoAuthCode(authCode: String) {
        try {
            val result = withContext(Dispatchers.IO) {
                kakaoLoginManager.handleKakaoLogin(authCode)
            }

            when (result) {
                is LoginResult.Success -> processSuccessfulKakaoLogin(result.data)
                is LoginResult.Error -> handleKakaoLoginError(result)
            }
        } catch (e: Exception) {
            Log.e(TAG, "예상치 못한 카카오 로그인 오류", e)
            showError("로그인 중 오류 발생")
        }
    }

    private fun processSuccessfulKakaoLogin(authResponse: AuthResponse) {
        // Log the full response for comprehensive debugging
        Log.d(TAG, "Full Auth Response: $authResponse")

        // Extract tokens with null checks
        val accessToken = authResponse.accessToken.orEmpty()
        val refreshToken = authResponse.refreshToken.orEmpty()

        // Extract user ID with robust null handling
        val userId = authResponse.user?.let { user ->
            user.userId ?: user.id
        }

        // Validate required data
        when {
            accessToken.isBlank() -> {
                Log.e(TAG, "Access token is missing")
                showError("로그인 토큰을 받지 못했습니다.")
                return
            }
            userId == null -> {
                Log.e(TAG, "No user ID found in auth response")
                showError("사용자 정보를 찾을 수 없습니다.")
                return
            }
            else -> {
                // Save user data with non-null values
                SharedPreferencesManager.saveUserData(
                    context = this,
                    userId = userId.toInt(),
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

                // Log successful login details
                Log.d(TAG, "Login Success - User ID: $userId")

                // Navigate to main screen
                navigateToMain(accessToken)
            }
        }
    }
    private fun handleKakaoLoginError(result: LoginResult.Error) {
        Log.e(TAG, "Login Error: ${result.exception.message}", result.exception)
        showError("로그인 실패: ${result.exception.message}")
        result.code?.let {
            Log.e(TAG, "Error Code: $it")
        }
    }

    private fun navigateToSignUpFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainer, SignUpFragment())
            addToBackStack(null)
        }
    }

    private fun navigateToMain(accessToken: String) {
        Intent(this, MainActivity::class.java).apply {
            putExtra("ACCESS_TOKEN", accessToken)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
        finish()
    }
}