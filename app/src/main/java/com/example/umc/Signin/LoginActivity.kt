package com.example.umc.Signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.umc.R
import com.example.umc.SignUp.SignUpFragment
import com.example.umc.databinding.FragmentSigninBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentSigninBinding
    private var isPasswordVisible = false // 비밀번호 표시 상태

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
            val email = binding.emailLoginEditText.text.toString()
            val password = binding.passwordLoginEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "로그인 성공: $email", Toast.LENGTH_SHORT).show()
                // 로그인 성공 후 동작 추가
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
        val email = binding.emailLoginEditText.text.toString()
        val password = binding.passwordLoginEditText.text.toString()

        // 이메일과 비밀번호가 입력되었는지 확인
        val isInputValid = email.isNotEmpty() && password.isNotEmpty()

        // 로그인 버튼 색상 변경
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
            binding.signinvisible.setImageResource(R.drawable.ic_eye_visible) // 보이기 아이콘 설정
        }
        isPasswordVisible = !isPasswordVisible
        binding.passwordLoginEditText.text?.let { binding.passwordLoginEditText.setSelection(it.length) } // 커서를 끝으로 이동
    }
}
