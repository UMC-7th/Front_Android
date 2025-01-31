package com.example.umc.Mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.R


class MyFragment : Fragment() {

    // Fragment 생성 시 초기화가 필요한 변수들
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // Fragment가 생성될 때 호출
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Fragment의 UI를 그릴 때 호출
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃을 인플레이트
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    // View가 생성된 후 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // View 초기화 및 이벤트 설정
        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        // View 초기화 코드
    }

    private fun setupListeners() {
        // 이벤트 리스너 설정
    }

    // Fragment가 화면에 표시될 때 호출
    override fun onResume() {
        super.onResume()
    }

    // Fragment가 화면에서 사라질 때 호출
    override fun onPause() {
        super.onPause()
    }

    // Fragment가 제거될 때 호출
    override fun onDestroy() {
        super.onDestroy()
    }
}