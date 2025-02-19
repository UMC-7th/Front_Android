package com.example.umc.Mypage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc.R
import com.example.umc.UserApi.Request.UpdateUserRequest
import com.example.umc.UserApi.UserRepository
import com.example.umc.UserApi.Response.UserProfileData  // 프로필 조회용
import com.example.umc.UserApi.SharedPreferencesManager
import com.example.umc.databinding.FragmentMyInfoBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MyFragmentInfo : Fragment() {

    private lateinit var binding: FragmentMyInfoBinding
    private var isEditMode = false
    private lateinit var editTextList: List<EditText>
    private val userRepository = UserRepository()
    private var selectedImagePath: String? = null // 선택한 이미지 경로 저장


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

        //이거 원래코드
//        binding.change.setOnClickListener {
//            isEditMode = !isEditMode
//            setEditableMode(isEditMode)
//            updateChangeButton()
//
//            if (!isEditMode) {
//                val updatedProfileData = UpdateUserRequest(  // ✅ UpdateUserRequest로 변경
//                    nickname = binding.nickNameEdit.text.toString(),
//                    email = binding.emailEdit.text.toString(),
//                    birth = binding.birthEdit.text.toString(),
//                    name = binding.nameEdit.text.toString(),
//                    phoneNum = binding.phoneEdit.text.toString()
//                )
//                updateUserProfile(updatedProfileData)  // ✅ 올바른 타입으로 전달
//            }
//        }
        binding.change.setOnClickListener {
            isEditMode = !isEditMode
            setEditableMode(isEditMode)
            updateChangeButton()

            val userId = SharedPreferencesManager.getUserId(requireContext())

            if (!isEditMode) {
                // 이름만 수정하는 경우
                val updatedName = binding.nameEdit.text.toString()

                if (updatedName != "") {
                    // name 수정 (POST 요청)
                    updateUserName(userId, updatedName)
                } else {
                    // name을 제외한 모든 정보 수정 (PUT 요청)
                    val updatedProfileData = UpdateUserRequest(
                        nickname = binding.nickNameEdit.text.toString(),
                        email = binding.emailEdit.text.toString(),
                        birth = binding.birthEdit.text.toString(),
                        name = binding.nameEdit.text.toString(),
                        phoneNum = binding.phoneEdit.text.toString()
                    )
                    updateUserProfile(updatedProfileData)
                }
            }
        }


        // 이미지 클릭 리스너 추가
        binding.imageView8.setOnClickListener {
            showImageDialog()
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
    // 이름 수정만 POST 요청
    private fun updateUserName(userId: Int, newName: String) {
        lifecycleScope.launch {
            try {
                userRepository.updateUserName(userId, newName) { success, message ->
                    if (success) {
                        Toast.makeText(context, "이름이 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "이름 수정 실패: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "이름 수정 중 오류 발생", Toast.LENGTH_SHORT).show()
                Log.e("MyFragmentInfo", "이름 수정 실패: ${e.message}")
            }
        }
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { uploadImage(it) }
    }

    private fun selectImageFromGallery() {
        imagePickerLauncher.launch("image/*") // 🔥 갤러리에서 이미지 선택
    }



    private fun uploadImage(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                val file = File(requireContext().cacheDir, "profile_image.jpg").apply {
                    requireContext().contentResolver.openInputStream(imageUri)?.use { input ->
                        FileOutputStream(this).use { output -> input.copyTo(output) }
                    }
                }

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

                userRepository.updateProfileImage(requireContext(), imagePart) { success, message ->
                    Toast.makeText(
                        context,
                        if (success) "프로필 이미지가 성공적으로 업데이트되었습니다."
                        else "프로필 이미지 업데이트 실패: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("MyFragmentInfo", "이미지 업로드 실패: ${e.message}")
            }
        }
    }
    private fun showImageDialog() {
        val dialog = Dialog(requireContext())

        dialog.setContentView(R.layout.fragment_my_image_dialog)

        // 다이얼로그 배경을 어두운 색으로 설정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 외부 터치 시 종료되지 않도록 설정
        dialog.setCanceledOnTouchOutside(true)

        // 다이얼로그 크기 설정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val urlEditText = dialog.findViewById<EditText>(R.id.profilechange)  // URL 입력 EditText
        val confirmButton = dialog.findViewById<View>(R.id.btnConfirm)  // 확인 버튼
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancel) // 취소 버튼


//        confirmButton.setOnClickListener {
//            val imageUrl = urlEditText.text.toString()
//
//            if (imageUrl.isNotEmpty()) {
//                updateProfileImage(imageUrl)  // PUT 요청을 보내는 메서드
//                dialog.dismiss()  // 다이얼로그 닫기
//            } else {
//                Toast.makeText(requireContext(), "URL을 입력해 주세요.", Toast.LENGTH_SHORT).show()
//            }
//        } 원래 코드

        confirmButton.setOnClickListener {
            val imagePath = urlEditText.text.toString().trim()  // 사용자가 입력한 파일 경로 가져오기

            if (imagePath.isNotEmpty()) {
                updateProfileImage(imagePath)  // 🚀 입력한 경로로 이미지 업데이트 실행
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "이미지 경로를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun updateProfileImage(imagePathOrUrl: String) {
        val token = UserRepository.getAuthToken(requireContext())
        if (token != null) {
            lifecycleScope.launch {
                try {
                    val imageFile: File? = if (imagePathOrUrl.startsWith("http")) {
                        // 🔹 웹 URL이면 다운로드해서 파일로 변환
                        downloadImageFromUrl(imagePathOrUrl)
                    } else {
                        // 🔹 로컬 파일이면 그대로 사용
                        File(imagePathOrUrl)
                    }

                    if (imageFile == null || !imageFile.exists()) {
                        Toast.makeText(context, "이미지 파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                        Log.e("MyFragmentInfo", "파일이 존재하지 않음: $imagePathOrUrl")
                        return@launch
                    }

                    Log.d("MyFragmentInfo", "업로드할 파일 경로: ${imageFile.absolutePath}")

                    // ✅ 파일을 `RequestBody`로 변환 (수정된 부분)
                    val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                    // ✅ Retrofit API 호출
                    userRepository.updateProfileImage(requireContext(), imagePart) { success, message ->
                        if (success) {
                            Toast.makeText(context, "프로필 이미지가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "프로필 이미지 업데이트 실패: $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "이미지 업데이트 실패", Toast.LENGTH_SHORT).show()
                    Log.e("MyFragmentInfo", "Error updating profile image", e)
                }
            }
        } else {
            Log.e("MyFragmentInfo", "토큰이 존재하지 않습니다.")
        }
    }

    private fun downloadImageFromUrl(imageUrl: String): File? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.doInput = true
            connection.connect()

            val inputStream: InputStream = connection.inputStream
            val file = File(requireContext().cacheDir, "profile_image.jpg") // 🔹 임시 파일 저장

            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()

            file
        } catch (e: Exception) {
            Log.e("MyFragmentInfo", "이미지 다운로드 실패: ${e.message}")
            null
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
