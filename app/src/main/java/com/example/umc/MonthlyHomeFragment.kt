package com.example.umc

import android.content.Context
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.umc.databinding.FragmentMonthlyHomeBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MonthlyHomeFragment : Fragment() {
    private var _binding: FragmentMonthlyHomeBinding? = null
    private val binding get() = _binding!!

    // 서버에서 받은 이벤트 날짜
    private var eventDates: List<CalendarDay> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadEventDatesFromServer()

        setupCalendar()
    }

    private fun loadEventDatesFromServer() {
        // 식단 만든 날짜 더미데이터
        val serverResponseDates = listOf("2025-01-20", "2025-01-30", "2025-02-10")

        // date가 어떤 형식으로 오는지 아직 몰라서 일단 이렇게 설정
        eventDates = serverResponseDates.map {
            val parts = it.split("-")
            CalendarDay.from(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        }
    }

    private fun setupCalendar() {
        // eventDates를 EventDecorator 및 SelectedMonthDecorator에 전달
        val selectedMonth = CalendarDay.today().month // 현재 보고 있는 달
        val eventDecorator = EventDecorator(requireContext(), eventDates, selectedMonth)
        val selectedMonthDecorator = SelectedMonthDecorator(requireContext(), selectedMonth, eventDates)

        binding.calendarView.addDecorators(eventDecorator, selectedMonthDecorator)

        // 상단 날짜 커스텀
        binding.calendarView.setTitleFormatter { day ->
            "${day.month}월"
        }

        binding.calendarView.setOnMonthChangedListener { _, date ->
            // 월이 바뀌면 날짜들을 새롭게 추가하되, 기존 decorator는 지워진 상태
            binding.calendarView.removeDecorators()

            // 새로운 달에 대한 데코레이터 적용
            val eventDecorator = EventDecorator(requireContext(), eventDates, date.month)
            val selectedMonthDecorator = SelectedMonthDecorator(requireContext(), date.month, eventDates)

            binding.calendarView.addDecorators(eventDecorator, selectedMonthDecorator)
        }
    }

    // 이벤트 데코레이터
    private class EventDecorator(
        private val context: Context,
        private val dates: List<CalendarDay>,
        private val selectedMonth: Int
    ) : DayViewDecorator {

        private val drawable = ContextCompat.getDrawable(context, R.drawable.ic_background)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            // 날짜가 이벤트 날짜이고, 선택된 달에 해당할 때만 데코레이터 적용
            return dates.contains(day) && day.month == selectedMonth
        }

        override fun decorate(view: DayViewFacade) {
            drawable?.let { view.setBackgroundDrawable(it) }
            view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.white))) // 글씨 색 흰색
            view.addSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD)) // 볼드
        }
    }

    // 선택된 월 데코레이터
    private class SelectedMonthDecorator(
        private val context: Context,
        private val selectedMonth: Int,
        private val eventDates: List<CalendarDay>
    ) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            // 선택된 달의 날짜이거나 이벤트 날짜가 아니면 데코레이터 적용
            return day.month != selectedMonth
        }

        override fun decorate(view: DayViewFacade) {
            // 선택되지 않은 달의 날짜에 대해 색깔 적용
            val gray7Color = ContextCompat.getColor(context, R.color.Gray7)
            view.addSpan(ForegroundColorSpan(gray7Color))
        }
    }

    // 양 옆 화살표를 누를 때 색깔이 변하는 기능은 1월 30일 이후 처리할 예정

    private fun loadMenusForDate(date: String) {
        // 해당 날짜의 메뉴 데이터를 로드하는 로직
        // 메인 홈화면이 완성되면 그때 바꾸겠습니다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}