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
import com.example.umc.Survey.SurveyGoalFragment
import com.example.umc.UserApi.UpdateUserRequest
import com.example.umc.UserApi.UserRepository
import com.example.umc.UserApi.UserProfileData  // 프로필 조회용
import com.example.umc.UserApi.UserUpdateData  // 프로필 업데이트 요청용
import com.example.umc.databinding.FragmentMyInfoBinding
import kotlinx.coroutines.launch

class MyFragmentInfo : Fragment() {

    private lateinit var binding: FragmentMyInfoBinding
    private var isEditMode = false
    private lateinit var editTextList: List<EditText>
    private val userRepository = UserRepository()

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

        val token = UserRepository.getAuthToken(requireContext())

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
                val updatedProfileData = UpdateUserRequest(  // ✅ UpdateUserRequest로 변경
                    nickname = binding.nickNameEdit.text.toString(),
                    email = binding.emailEdit.text.toString(),
                    birth = binding.birthEdit.text.toString(),
                    name = binding.nameEdit.text.toString(),
                    phoneNum = binding.phoneEdit.text.toString()
                )
                updateUserProfile(updatedProfileData)  // ✅ 올바른 타입으로 전달
            }
        }

        binding.btnalarm.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_my_container, MyFragmentalarm())  // fragment_container는 메인 액티비티의 프래그먼트 컨테이너 ID입니다
                .addToBackStack(null)  // 뒤로 가기 동작을 위해 백스택에 추가
                .commit()
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

    private fun loadUserProfile(token: String) {
        lifecycleScope.launch {
            try {
                val profileResponse = userRepository.getUserProfile(requireContext())

                if (profileResponse != null && profileResponse.data != null) {
                    updateUIWithProfile(profileResponse.data)
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

    private fun updateUIWithProfile(profileData: UserProfileData) {  // ✅ 조회용 데이터 클래스 사용
        with(binding) {
            nickNameEdit.setText(profileData.nickname)
            nameEdit.setText(profileData.name)
            birthEdit.setText(profileData.birth)
            emailEdit.setText(profileData.email)
            phoneEdit.setText(profileData.phone)  // ✅ UserProfileData는 phone을 사용
        }
    }

    private fun updateUserProfile(updatedProfileData: UpdateUserRequest) {  // ✅ 요청 객체 변경
        val token = UserRepository.getAuthToken(requireContext())
        if (token != null) {
            lifecycleScope.launch {
                Log.d("MyFragmentInfo", "업데이트 요청 데이터: $updatedProfileData")  // 추가
                val success = userRepository.updateUserProfile(requireContext(), updatedProfileData)
                if (success) {
                    Toast.makeText(context, "프로필이 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "프로필 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e("MyFragmentInfo", "토큰이 존재하지 않습니다.")
        }
    }

}
