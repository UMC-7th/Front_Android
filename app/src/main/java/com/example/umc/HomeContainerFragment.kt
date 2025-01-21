package com.example.umc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.databinding.FragmentHomeContainerBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeContainerFragment : Fragment() {
    private var _binding: FragmentHomeContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupTabLayout()

        // 사용자 이름 설정 (추후 데이터 연동)
        binding.tvServe.text = getString(R.string.serve).format("토미")
    }

    private fun setupViewPager() {
        // ViewPager2 어댑터 설정
        val pagerAdapter = HomePagerAdapter(requireActivity())
        binding.viewPager.adapter = pagerAdapter
    }

    private fun setupTabLayout() {
        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "오늘"
                1 -> tab.text = "월간"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}