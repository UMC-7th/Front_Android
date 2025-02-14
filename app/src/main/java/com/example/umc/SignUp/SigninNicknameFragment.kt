package com.example.umc.SignUp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.umc.R
import com.example.umc.Signin.LoginActivity
import com.example.umc.UserApi.Viewmodel.SignUpViewModel
import com.example.umc.databinding.FragmentSigninNicknameBinding
// nickname 처리
class SigninNicknameFragment : Fragment() {

    private var _binding: FragmentSigninNicknameBinding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by activityViewModels() // ViewModel 초기화

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigninNicknameBinding.inflate(inflater, container, false)
        val view = binding.root

        // EditText 선택 시 밑줄 색상 변경
        setupEditTextFocus()

        // 닉네임 입력값에 따라 버튼 활성화 및 색상 변경
        setupNicknameValidation()

        // NextButton 클릭 시 Fragment 전환  -> 일단 다시 login쪽으로 넘어가게했습니다.
        binding.NextButton.setOnClickListener {
            signUpViewModel.name = binding.editText.text.toString()
            Toast.makeText(requireContext(), "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // 현재 Fragment가 포함된 Activity 종료 (필요에 따라 유지 가능)
        }


        return view
    }

    // EditText 포커스 시 밑줄 색상 변경
    private fun setupEditTextFocus() {
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editText.backgroundTintList = resources.getColorStateList(R.color.Primary_Orange1, null) // 포커스 시 색상 변경
            } else {
                // 포커스 해제 시, 유효성 검사 후 색상 변경
                val isValid = binding.editText.text.length >= 2 // 닉네임 길이가 2 이상이면 유효
                binding.editText.backgroundTintList = if (isValid) {
                    resources.getColorStateList(R.color.Gray3, null) // 유효한 경우 기본 색상
                } else {
                    resources.getColorStateList(android.R.color.holo_red_light, null) // 유효하지 않은 경우 빨간색
                }

                // canUse의 visibility 변경
                binding.canUse.visibility = if (isValid) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }
        }
    }


    // 닉네임 유효성 검사 및 버튼 활성화
    private fun setupNicknameValidation() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 닉네임이 2글자 이상일 때만 버튼을 활성화하고 색상 변경
                val isValid = s.toString().length >= 2
                binding.NextButton.isEnabled = isValid
                binding.NextButton.setBackgroundColor(
                    if (isValid) resources.getColor(R.color.Primary_Orange1) // 유효하면 버튼 색상 변경
                    else resources.getColor(R.color.Gray3) // 유효하지 않으면 비활성화된 색상
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ViewBinding 객체를 해제하여 메모리 누수를 방지
    }
}
