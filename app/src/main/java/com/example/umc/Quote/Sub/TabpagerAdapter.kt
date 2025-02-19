package com.example.umc.Quote.Sub

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4  // 탭 개수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VegetableFragment()  // 채소 탭
            1 -> MeatFragment()       // 육류 탭
            2 -> FruitFragment()      // 과일 탭
            3 -> SeafoodFragment()    // 어류/수산물 탭
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}

