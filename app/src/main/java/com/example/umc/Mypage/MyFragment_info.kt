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
            updateChangeButton()

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

    private fun updateChangeButton() {
        val imageResource = if (isEditMode) {
            R.drawable.my_info_done
        } else {
            R.drawable.my_info_change
        }
        binding.change.setImageResource(imageResource)
    }

    // 사용자 프로필을 로드하는 함수
    private fun loadUserProfile(token: String) {
        lifecycleScope.launch {
            try {
                // suspend 함수 호출
                val profileResponse = userRepository.getUserProfile(requireContext())  // context로 토큰 전달

                if (profileResponse != null && profileResponse.data != null) {
                    updateUIWithProfile(profileResponse.data)  // 데이터가 null이 아닌 경우에만 UI 업데이트
                } else {
                    Toast.makeText(context, "프로필 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("MyFragmentInfo", "프로필 데이터가 null입니다.")
                }
            } catch (e: Exception) {
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.e("MyFragmentInfo", "Error loading profile", e)
            }
        }
    }


    private fun updateUIWithProfile(profileData: UserProfileData) {
        with(binding) {
            nickNameEdit.setText(profileData.nickname)
            nameEdit.setText(profileData.name)
            birthEdit.setText(profileData.birth)
            emailEdit.setText(profileData.email)
            phoneEdit.setText(profileData.phone)
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
