package com.example.umc.Mypage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.Survey.SurveyGoalFragment
import com.example.umc.UserApi.Response.HealthScoreData
import com.example.umc.UserApi.Response.SuccessData
import com.example.umc.UserApi.UserRepository
import com.example.umc.databinding.FragmentMyBinding
import kotlinx.coroutines.launch


class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupListeners()
        fetchHealthScore()
        fetchAiDiagnosis() // AI 진단 데이터 조회

    }
    private fun fetchAiDiagnosis() {
        lifecycleScope.launch {
            try {
                val response = userRepository.getDiagnosisResult(requireContext())
                response?.let { diagnosisResponse ->
                    updateAiDiagnosisInfo(diagnosisResponse.success)
                }
            } catch (e: Exception) {
                Log.e("MyFragment", "AI 진단 조회 실패: ${e.message}")
                // 에러 처리 필요시 여기에 추가
            }
        }
    }
    private fun fetchHealthScore() {
        lifecycleScope.launch {
            try {
                val response = userRepository.getHealthScore(requireContext())
                response?.let { healthScoreResponse ->
                    updateHealthInfo(healthScoreResponse.success)
                }
            } catch (e: Exception) {
                Log.e("MyFragment", "건강 점수 조회 실패: ${e.message}")
                // 에러 처리 필요시 여기에 추가
            }
        }
    }
    private fun updateAiDiagnosisInfo(data: SuccessData?) {
        data?.let {
            binding.apply {
                // 진단 리스트를 보여주기 위해 각 항목을 TextView에 설정
                it.diagnosis?.let { diagnosis ->
                    // 진단 내용 출력 (여러 항목을 출력할 수 있게 Join 처리)
                    tvAiDiagnosisDiet.text = diagnosis.joinToString("\n")
                }

                it.advice?.let { advice ->
                    // 조언 내용 출력 (여러 항목을 출력할 수 있게 Join 처리)
                    tvAiDiagnosisHealth.text = advice.joinToString("\n")
                }
            }
        }
    }
    private fun updateHealthInfo(data: HealthScoreData) {
        binding.apply {
            // 건강 점수 업데이트
            healthscore.text = "${data.healthScore}점"

            // 비교값 업데이트 (comparison이 String으로 받아지므로 그대로 표시)
            comparsion.text = data.comparison

            // 업데이트 날짜 표시
            textView52.text = "${data.updateAt} 기준"
        }
    }


    private fun initializeViews() {
        binding.apply {
            // 기존 코드
            tvName.text = "토미"
            tvProfileManage.text = "내 정보 관리"

            // AI 텍스트 색상 변경을 위한 SpannableString 설정
            val texts = listOf(
                binding.tvAiDiagnosisDiet,
                binding.tvAiDiagnosisHealth,
                binding.tvAiDiagnosisSuggestion
            )

            texts.forEach { textView ->
                val fullText = textView.text.toString()
                val spannableString = SpannableString(fullText)

                // "AI" 텍스트의 위치 찾기
                val startIndex = fullText.indexOf("AI")
                if (startIndex != -1) {
                    spannableString.setSpan(
                        ForegroundColorSpan(resources.getColor(R.color.Primary_Orange1, null)),
                        startIndex,
                        startIndex + 5,  // "AI"는 2글자
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                textView.text = spannableString
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            // 프로필 관리 클릭 리스너
            tvProfileManage.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_my_container, MyFragmentInfo()) // fragment_container는 MainActivity의 Fragment 배치 영역 ID
                    .addToBackStack(null) // 뒤로 가기 가능하도록 추가
                    .commit()
            }

            // 식단 카드 클릭 리스너
//            cvDiet.setOnClickListener {
//                // TODO: 식단 상세 화면으로 이동
//            }

            // 건강 점수 카드 클릭 리스너
//            cvHealthScore.setOnClickListener {
//                // TODO: 건강 점수 상세 화면으로 이동
//            }
            // 변경 버튼 클릭 리스너 추가
            btnChange.setOnClickListener {
                showChangeDialog()
            }
        }
    }
    private fun showChangeDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_my_dialog)

        // 다이얼로그 배경을 투명하게 설정
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 외부 터치시 종료되지 않도록 설정
        dialog.setCanceledOnTouchOutside(false)

        // 취소 버튼 클릭 리스너
        dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        // 변경 버튼 클릭 리스너
        dialog.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            // 여기에 변경 버튼 클릭시 수행할 로직 추가
            dialog.dismiss()
            // SurveyGoalFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SurveyGoalFragment())  // fragment_container는 메인 액티비티의 프래그먼트 컨테이너 ID입니다
                .addToBackStack(null)  // 뒤로 가기 동작을 위해 백스택에 추가
                .commit()
        }

        // 다이얼로그 크기 설정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideTitle()

        // 여기나 oncreateView에 추가
    }
}