package com.example.umc.Signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.SignUp.SignUpFragment
import com.example.umc.UserApi.LoginResponse
import com.example.umc.UserApi.UserRepository
import com.example.umc.databinding.FragmentSigninBinding
import retrofit2.Call
import retrofit2.Response
//login 로직 처리

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentSigninBinding
    private var isPasswordVisible = false // 비밀번호 표시 상태
    private val userRepository = UserRepository() // UserRepository 인스턴스 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = FragmentSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        // 이메일과 비밀번호 입력 감지
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateLoginButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.emailLoginEditText.addTextChangedListener(textWatcher)
        binding.passwordLoginEditText.addTextChangedListener(textWatcher)

        // 비밀번호 표시/숨기기 기능
        binding.signinvisible.setOnClickListener {
            togglePasswordVisibility()
        }

        // 로그인 버튼 클릭 리스너
        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginEditText.text.toString().trim()
            val password = binding.passwordLoginEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLogin(email, password)
            } else {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 리스너
        binding.loginButton2.setOnClickListener {
            navigateToSignUpFragment()
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

    private fun navigateToSignUpFragment() {
        val fragment = SignUpFragment()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null) // 뒤로가기 시 이전 화면으로 돌아가게 설정
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // 비밀번호 숨기기
            binding.passwordLoginEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.signinvisible.setImageResource(R.drawable.ic_eye_visible) // 숨기기 아이콘 설정
        } else {
            // 비밀번호 표시
            binding.passwordLoginEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            //binding.signinvisible.setImageResource(R.drawable.ic_eye_invisible) // 보이기 아이콘 설정
        }
        isPasswordVisible = !isPasswordVisible
        binding.passwordLoginEditText.text?.let { binding.passwordLoginEditText.setSelection(it.length) } // 커서를 끝으로 이동
    }
    private fun performLogin(email: String, password: String) {
        // 로그인 요청 보내기
        userRepository.login(email, password).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                Log.d("Login", "Response Code: ${response.code()}")
                Log.d("Login", "Response Body: ${response.body()}")
                Log.d("Login", "Error Body: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // LoginActivity 종료 (뒤로 가기 방지)
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


}
