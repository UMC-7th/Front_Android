package com.example.umc.Subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.example.umc.cart.SubscribeCart


// SubFragment.kt


class SubFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscribe_cart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupListeners()

        // 구독관리 페이지에서 바로 장바구니로 이동
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscribeCart())
            .addToBackStack(null)
            .commit()
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initializeViews()
//        setupListeners()
//
//        // 구독관리 프래그먼트로 전환
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.main_container, SubscriptionManageFragment())
//            .addToBackStack(null)
//            .commit()
//    }

    private fun initializeViews() {
        // View 초기화 코드
    }

    private fun setupListeners() {
        // 이벤트 리스너 설정
    }
}