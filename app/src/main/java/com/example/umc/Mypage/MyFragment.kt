package com.example.umc.Mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.R
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
            tvName.text = "토미"
            tvProfileManage.text = "내 정보 관리"
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
                    .replace(R.id.fragment_container, MyFragmentInfo()) // fragment_container는 MainActivity의 Fragment 배치 영역 ID
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}