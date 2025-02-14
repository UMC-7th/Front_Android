package com.example.umc.Subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import android.view.MotionEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.umc.subscribe.SubscriptionHistoryFragment

class SubscriptionManageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscription_manage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.showTitle("구독 관리", true)
        (activity as? MainActivity)?.hideBottomBar()

        setupDetailButton(view)
        setupCalendar(view)
        setupManageAddressButton(view)  // 주소 관리 버튼 설정
    }

    private fun setupDetailButton(view: View) {
        // 상세내역 버튼 클릭 리스너
        view.findViewById<TextView>(R.id.detailButton)?.setOnClickListener {
            navigateToHistory()
        }
    }

    private fun setupManageAddressButton(view: View) {
        // 주소 관리 레이아웃 클릭 리스너
        view.findViewById<View>(R.id.ll_manage_address)?.setOnClickListener {
            (activity as? MainActivity)?.showTitle("주소록 관리", true)
            navigateToAddress()
        }
    }

    private lateinit var calendarView: MaterialCalendarView

    private fun setupCalendar(view: View) {
        calendarView = view.findViewById(R.id.calendar_view)

        // ID를 문자열로 찾기
        val leftArrowId = resources.getIdentifier("mcv_arrow_previous", "id", requireContext().packageName)
        val rightArrowId = resources.getIdentifier("mcv_arrow_next", "id", requireContext().packageName)

        val leftArrow = view.findViewById<ImageView>(leftArrowId)
        val rightArrow = view.findViewById<ImageView>(rightArrowId)

        leftArrow?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    leftArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Primary_Orange1))
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    leftArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Gray5))
                    true
                }
                else -> false
            }
        }

        rightArrow?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    rightArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Primary_Orange1))
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    rightArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Gray5))
                    true
                }
                else -> false
            }
        }

        // Null 체크 후 기본 색상 설정
        leftArrow?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Gray5))
        rightArrow?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.Gray5))
    }
    private fun navigateToHistory() {
        // 구독 내역 프래그먼트로 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubscriptionHistoryFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToAddress() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, SubAddressFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showBottomBar()
    }
}
