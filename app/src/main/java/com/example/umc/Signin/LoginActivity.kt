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
import com.example.umc.Survey.SurveyGoalFragment
import com.example.umc.UserApi.Response.LoginResponse
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.UserApi.SharedPreferencesManager
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
        if (savedInstanceState == null) {
            clearBackStack()
        }

        setupUI()
    }
    private fun clearBackStack() {
        // 백스택에 있는 모든 프래그먼트 제거
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
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
//        // 카카오 로그인 버튼 클릭 리스너
//        binding.kakaologin.setOnClickListener {
//            // 카카오 로그인 호출
//            loginWithKakao()
//        }
    }

    //    private fun loginWithKakao() {
//        // 카카오 로그인 후 받은 액세스 토큰
//        val accessToken = "여기_카카오_액세스_토큰_입력"
//
//        RetrofitClient.kakaoLoginApi.loginWithKakao(accessToken).enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful) {
//                    val loginResponse = response.body()
//                    if (loginResponse?.success == true) {
//                        val token = loginResponse.accessToken
//                        // 로그인 성공 후 처리
//                        UserRepository.saveAuthToken(this@LoginActivity, token)
//                        navigateToMain(token)
//                    } else {
//                        Toast.makeText(this@LoginActivity, "로그인 실패: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "서버 오류", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//
//    private fun performKakaoLogin(kakaoToken: String) {
//        userRepository.kakaoLogin(kakaoToken).enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                if (response.isSuccessful) {
//                    val accessToken = response.body()?.success?.accessToken
//                    if (accessToken != null) {
//                        UserRepository.saveAuthToken(this@LoginActivity, accessToken)
//                        checkSurveyStatus(accessToken)
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
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
            binding.signinvisible.setImageResource(R.drawable.ic_eye_invisible) // 보이기 아이콘 설정
        }
        isPasswordVisible = !isPasswordVisible
        binding.passwordLoginEditText.text?.let { binding.passwordLoginEditText.setSelection(it.length) } // 커서를 끝으로 이동
    }

    // 설문조사 임시코드
    private fun performLogin(email: String, password: String) {
        userRepository.login(email, password).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("Login", "Response Code: ${response.code()}")

                if (response.isSuccessful) {
                    val accessToken = response.body()?.success?.accessToken
                    val userId = response.body()?.success?.user?.userId  // userId 받아오기

                    if (accessToken != null) {
                        UserRepository.saveAuthToken(this@LoginActivity, accessToken)

                        // 사용자가 설문조사를 완료했는지 확인하는 로직
                        checkSurveyStatus(accessToken)
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


    //원래 코드
//    private fun performLogin(email: String, password: String) {
//        // 로그인 요청 보내기
//        userRepository.login(email, password).enqueue(object : retrofit2.Callback<LoginResponse> {
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//
//                Log.d("Login", "Response Code: ${response.code()}")
//                Log.d("Login", "Response Body: ${response.body()}")
//                Log.d("Login", "Error Body: ${response.errorBody()?.string()}")
//
//                if (response.isSuccessful) {
//                    Toast.makeText(this@LoginActivity, "로그인 성공!", Toast.LENGTH_SHORT).show()
//
//                    // 로그인 성공 시, accessToken을 SharedPreferences에 저장
//                    val accessToken = response.body()?.success?.accessToken
//
//                    if (accessToken != null) {
//                        // SharedPreferences에 토큰 저장
//                        UserRepository.saveAuthToken(this@LoginActivity, accessToken)
//
//                        // 로그인 후 MainActivity로 이동
//                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                        intent.putExtra("ACCESS_TOKEN", accessToken) // accessToken을 Intent에 담아서 전달
//                        startActivity(intent)
//                        finish() // LoginActivity 종료 (뒤로 가기 방지)
//                    } else {
//                        // accessToken이 없으면 적절한 처리를 추가
//                        Toast.makeText(this@LoginActivity, "토큰이 없습니다.", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@LoginActivity, "로그인 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Log.e("Login", "Network Error", t)
//                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


    private fun checkSurveyStatus(accessToken: String) {
        // TODO: API를 통해 사용자의 설문조사 완료 여부를 확인
        // 임시로 설문조사를 하지 않았다고 가정
        val hasSurveyCompleted = false

        if (!hasSurveyCompleted) {
            navigateToSurvey()
        } else {
            navigateToMain(accessToken)
        }
    }

    private fun navigateToSurvey() {
        // 기존 프래그먼트들을 모두 제거
        clearBackStack()

        // 새로운 컨테이너 레이아웃으로 전환
        setContentView(R.layout.activity_survey_container)

        // SurveyGoalFragment 추가
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.survey_container, SurveyGoalFragment())
            // 설문 진행 중에는 백스택에 추가하지 않음
        }
    }

    private fun navigateToMain(accessToken: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("ACCESS_TOKEN", accessToken)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }



}