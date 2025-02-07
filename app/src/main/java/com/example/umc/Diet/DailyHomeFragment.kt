package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.databinding.FragmentDailyHomeBinding

class DailyHomeFragment : Fragment() {
    private var _binding: FragmentDailyHomeBinding? = null
    private val binding get() = _binding!!

    private var selectedBreakfastPosition = -1
    private var selectedLunchPosition = -1
    private var selectedDinnerPosition = -1

    private lateinit var breakfastAdapter: MenuItemAdapter
    private lateinit var lunchAdapter: MenuItemAdapter
    private lateinit var dinnerAdapter: MenuItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAdapters()
        setupRecyclerViews()
    }

    private fun initializeAdapters() {
        breakfastAdapter = MenuItemAdapter(
            onClick = { item: MenuItem, position: Int ->
                selectedBreakfastPosition = position
                breakfastAdapter.notifyDataSetChanged()
                onMenuItemClicked(item, "아침")
            },
            onFavoriteChanged = { item, isFavorite ->
                // 즐겨찾기 상태 변경 처리
            },
            onDietCompleteChanged = { item, isCompleted ->
                // 식단 완료 상태 변경 처리
            }
        )

        lunchAdapter = MenuItemAdapter(
            onClick = { item: MenuItem, position: Int ->
                selectedLunchPosition = position
                lunchAdapter.notifyDataSetChanged()
                onMenuItemClicked(item, "점심")
            },
            onFavoriteChanged = { item, isFavorite ->
                // 즐겨찾기 상태 변경 처리
            },
            onDietCompleteChanged = { item, isCompleted ->
                // 식단 완료 상태 변경 처리
            }
        )

        dinnerAdapter = MenuItemAdapter(
            onClick = { item: MenuItem, position: Int ->
                selectedDinnerPosition = position
                dinnerAdapter.notifyDataSetChanged()
                onMenuItemClicked(item, "저녁")
            },
            onFavoriteChanged = { item, isFavorite ->
                // 즐겨찾기 상태 변경 처리
            },
            onDietCompleteChanged = { item, isCompleted ->
                // 식단 완료 상태 변경 처리
            }
        )
    }

    private fun setupRecyclerViews() {
        binding.apply {
            // 아침 메뉴
            rvBreakfast.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = breakfastAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        updateIndicator(
                            recyclerView,
                            binding.breakfastIndicatorBar
                        )
                    }
                })
            }
            breakfastAdapter.submitList(getDummyMenuItems())

            // 점심 메뉴
            rvLunch.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = lunchAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        updateIndicator(
                            recyclerView,
                            binding.lunchIndicatorBar
                        )
                    }
                })
            }
            lunchAdapter.submitList(getDummyMenuItems())

            // 저녁 메뉴
            rvDinner.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = dinnerAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        updateIndicator(
                            recyclerView,
                            binding.dinnerIndicatorBar
                        )
                    }
                })
            }
            dinnerAdapter.submitList(getDummyMenuItems())
        }
    }
    // 인디케이터 업데이트 함수
    private fun updateIndicator(recyclerView: RecyclerView, indicator: View) {
        val totalWidth = recyclerView.computeHorizontalScrollRange()
        val visibleWidth = recyclerView.computeHorizontalScrollExtent()
        val scrollOffset = recyclerView.computeHorizontalScrollOffset()

        // 스크롤 진행률 계산
        val scrollProgress = if (totalWidth - visibleWidth > 0) {
            scrollOffset.toFloat() / (totalWidth - visibleWidth)
        } else {
            0f
        }

        // 인디케이터 이동
        val maxScroll = (indicator.parent as View).width - indicator.width
        indicator.translationX = maxScroll * scrollProgress
    }

    private fun onMenuItemClicked(item: MenuItem, mealTime: String) {
        val dietDetailFragment = DietDetailFragment()

        val bundle = Bundle()
        bundle.putString("name", item.name)
        bundle.putString("calories", item.calories)
        dietDetailFragment.arguments = bundle

        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, dietDetailFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        val mainActivity = activity as? MainActivity
        mainActivity?.showTitle(mealTime, true)
        mainActivity?.hideBottomBar()
    }

    private fun getDummyMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem("image_url1", "제육볶음 도시락", "560Kcal"),
            MenuItem("image_url2", "샐러드 도시락", "450Kcal"),
            MenuItem("image_url3", "볶음밥 도시락", "520Kcal"),
            MenuItem("image_url4", "연어 도시락", "480Kcal")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}