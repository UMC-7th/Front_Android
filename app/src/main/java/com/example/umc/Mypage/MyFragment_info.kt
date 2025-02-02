package com.example.umc.Mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc.R
import com.example.umc.UserApi.UserRepository
import com.example.umc.UserApi.UserProfileData
import com.example.umc.databinding.FragmentMyInfoBinding
import kotlinx.coroutines.launch

class MyFragmentInfo : Fragment() {

    private lateinit var binding: FragmentMyInfoBinding
    private var isEditMode = false
    private lateinit var editTextList: List<EditText>
    private val userRepository = UserRepository()  // UserRepository 사용

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOf(
            binding.nickNameEdit,
            binding.nameEdit,
            binding.birthEdit,
            binding.emailEdit,
            binding.phoneEdit
        ).also { editTextList = it }

        setEditableMode(false)

        // 로그인 후 받은 토큰을 SharedPreferences에 저장
        val token = UserRepository.getAuthToken(requireContext())  // 수정된 부분

        if (token != null) {
            loadUserProfile(token)
        } else {
            Log.e("MyFragmentInfo", "토큰이 존재하지 않습니다.")
        }

        binding.change.setOnClickListener {
            isEditMode = !isEditMode
            setEditableMode(isEditMode)
            //updateChangeButton()

            if (!isEditMode) {
                // 수정이 완료된 후 변경된 정보를 서버로 전송
                val updatedProfileData = UserProfileData(
                    binding.nickNameEdit.text.toString(),
                    binding.nameEdit.text.toString(),
                    binding.birthEdit.text.toString(),
                    binding.emailEdit.text.toString(),
                    binding.phoneEdit.text.toString()
                )
                updateUserProfile(updatedProfileData)
            }
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setEditableMode(editable: Boolean) {
        editTextList.forEach { editText ->
            editText.isEnabled = editable
            editText.isFocusableInTouchMode = editable
            editText.isFocusable = editable
            if (!editable) {
                editText.clearFocus()
            }
        }
    }

//    private fun updateChangeButton() {
//        val imageResource = if (isEditMode) {
//            R.drawable.my_info_done
//        } else {
//            R.drawable.my_info_change
//        }
//        binding.change.setImageResource(imageResource)
//    }

    private fun loadUserProfile(token: String) {
        lifecycleScope.launch {
            try {
                Log.d("MyFragmentInfo", "프로필 로드 시작")
                Log.d("MyFragmentInfo", "사용중인 토큰: $token")  // 토큰 값 확인

                val profileResponse = try {
                    userRepository.getUserProfile(requireContext()).also {
                        Log.d("MyFragmentInfo", "서버 응답: $it")  // 전체 응답 로깅
                    }
                } catch (e: Exception) {
                    Log.e("MyFragmentInfo", "API 호출 실패", e)
                    Toast.makeText(context, "API 호출 중 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // null 체크 및 데이터 구조 검증
                when {
                    profileResponse == null -> {
                        Log.e("MyFragmentInfo", "응답이 null입니다")
                        Toast.makeText(context, "서버 응답이 없습니다", Toast.LENGTH_SHORT).show()
                    }
                    profileResponse.user == null -> {
                        Log.e("MyFragmentInfo", "사용자 데이터가 null입니다: $profileResponse")
                        Toast.makeText(context, "사용자 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        try {
                            updateUIWithProfile(profileResponse.user)
                            Log.d("MyFragmentInfo", "UI 업데이트 성공: ${profileResponse.user}")
                        } catch (e: Exception) {
                            Log.e("MyFragmentInfo", "UI 업데이트 실패", e)
                            Toast.makeText(context, "화면 업데이트 중 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MyFragmentInfo", "예상치 못한 오류", e)
                Toast.makeText(context, "예상치 못한 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun updateUIWithProfile(profileData: UserProfileData) {
        with(binding) {
            nickNameEdit.setText(profileData.nickname)
            nameEdit.setText(profileData.name)
            birthEdit.setText(profileData.birth)
            emailEdit.setText(profileData.email)
            phoneEdit.setText(profileData.phoneNum)  // phone -> phoneNum으로 변경

            // 데이터가 잘 들어갔는지 로그로 확인
            Log.d("MyFragmentInfo", """
            프로필 UI 업데이트:
            닉네임: ${profileData.nickname}
            이름: ${profileData.name}
            생일: ${profileData.birth}
            이메일: ${profileData.email}
            전화번호: ${profileData.phoneNum}
        """.trimIndent())
        }
    }

    // 프로필 정보 업데이트
    private fun updateUserProfile(updatedProfileData: UserProfileData) {
        val token = UserRepository.getAuthToken(requireContext())  // 수정된 부분
        if (token != null) {
            // 현재는 업데이트 기능을 구현하지 않음
        } else {
            Log.e("MyFragmentInfo", "토큰이 존재하지 않습니다.")
        }
    }

}
