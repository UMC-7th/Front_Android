package com.example.umc.Mypage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.Survey.SurveyGoalFragment
import com.example.umc.databinding.FragmentMyBinding


class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun initializeViews() {
        binding.apply {
            // 프로필 정보 설정
            binding.tvName.text = "토미"
            binding.tvProfileManage.text = "내 정보 관리"
            //ivProfile.setImageResource(R.drawable.default_profile)

            // 건강 점수 설정
            //tvScore.text = "82점"
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
    }
}