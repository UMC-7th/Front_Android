package com.example.umc.SignUp

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.example.umc.databinding.SigninPhoneBinding

class SigninPhoneFragment : Fragment() {

    private lateinit var binding: SigninPhoneBinding  // ViewBinding 객체
    private var isPhoneNumberValid = false
    private var isCodeValid = false
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SigninPhoneBinding.inflate(inflater, container, false)

        // 전화번호 입력 형식 검사
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val phoneNumber = editable.toString()
                isPhoneNumberValid = isPhoneNumberValid(phoneNumber)
                updateNextButtonState()
            }
        })

        // editText 포커스 상태 변경에 따른 색상 업데이트
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editText.backgroundTintList =
                    resources.getColorStateList(R.color.Primary_Orange1, null)
            } else if (!isPhoneNumberValid) {
                binding.editText.backgroundTintList =
                    resources.getColorStateList(R.color.Gray3, null)
            }
        }

        // NextButton 클릭 이벤트 처리
        binding.NextButton.setOnClickListener {
            if (!binding.editText2.isVisible) {
                // 1단계: 인증코드 입력란 활성화
                showCodeInputFields()
                startTimer()
            } else if (isCodeValid) {
                // 2단계: 인증확인 후 다음 Fragment로 이동
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainer, SigninInfoFragment())
                transaction.addToBackStack(null) // 뒤로 가기 지원
                transaction.commit()
            }
        }

        // 인증코드 입력에 따른 버튼 상태 업데이트
        binding.editText2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val code = editable.toString()
                isCodeValid = code.length == 4
                updateNextButtonState()
            }
        })

        // 인증코드 입력란 포커스 상태 변경에 따른 색상 업데이트
        binding.editText2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editText2.backgroundTintList =
                    resources.getColorStateList(R.color.Primary_Orange1, null)
            } else if (!isCodeValid) {
                binding.editText2.backgroundTintList =
                    resources.getColorStateList(R.color.Gray3, null)
            }
        }

        return binding.root
    }

    private fun showCodeInputFields() {
        // 인증문자 받기 후 나타날 EditText 및 버튼 상태 업데이트
        binding.editText2.visibility = View.VISIBLE
        binding.timerText.visibility = View.VISIBLE
        binding.NextButton.text = "인증확인"
        binding.NextButton.isEnabled = false
        binding.NextButton.setBackgroundColor(resources.getColor(R.color.Gray2))
    }

    private fun updateNextButtonState() {
        // 버튼 상태를 전화번호 또는 인증코드 유효성에 따라 업데이트
        binding.NextButton.isEnabled = if (!binding.editText2.isVisible) {
            isPhoneNumberValid
        } else {
            isCodeValid
        }

        binding.NextButton.setBackgroundColor(
            if (binding.NextButton.isEnabled) resources.getColor(R.color.Primary_Orange1)
            else resources.getColor(R.color.Gray2)
        )
    }

    private fun startTimer() {
        // 5분 타이머
        timer?.cancel() // 기존 타이머 초기화
        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.timerText.text = "시간 초과"
                binding.NextButton.isEnabled = false
                binding.NextButton.setBackgroundColor(resources.getColor(R.color.Gray2))
            }
        }.start()
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        // 전화번호 형식 확인 (10~11자리 숫자)
        return phone.matches(Regex("^\\d{11}$"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // 타이머 정리
    }
}
