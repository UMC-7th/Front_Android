package com.example.umc.Quote.Sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.databinding.FragmentQuoteSubBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class QuoteFragmentSub : Fragment() {
    private var _binding: FragmentQuoteSubBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰바인딩 사용하여 레이아웃을 반환
        _binding = FragmentQuoteSubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)



        // Adapter 설정
        val pagerAdapter = TabPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // TabLayout과 ViewPager2 연동
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "채소"
                1 -> tab.text = "육류"
                2 -> tab.text = "과일"
                3 -> tab.text = "어류/수산물"
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle("제철", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 뷰바인딩 참조 해제
    }
}
