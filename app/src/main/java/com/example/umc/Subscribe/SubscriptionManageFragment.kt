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
            (activity as? MainActivity)?.showTitle("배송지 관라", true)
            navigateToAddress()
        }
    }

    private fun setupCalendar(view: View) {
        // 캘린더 설정
        view.findViewById<MaterialCalendarView>(R.id.calendar_view)?.apply {
            // 기본 설정
            selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE

            // 데코레이터 설정 (배송일 표시)
            val deliveryDates = listOf(3, 6, 8, 10, 15, 17, 22, 24, 31)
            val decorator = DeliveryDateDecorator(requireContext())
            deliveryDates.forEach { day ->
                decorator.addDate(CalendarDay.from(2025, 1, day))
            }
            addDecorator(decorator)

            // 날짜 선택 리스너
            setOnDateChangedListener { _, _, selected ->
                if (selected) {
                    navigateToHistory()
                }
            }
        }
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
