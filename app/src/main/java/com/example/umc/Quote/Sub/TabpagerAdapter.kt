package com.example.umc.Quote.Sub

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(fragment: QuoteFragmentSub) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        QuoteFragmentSub(),
        MeatFragment(),
        SeafoodFragment(),
        VegetableFragment()
    )

    private val titles = listOf(
        "Quote", "Meat", "Seafood", "Vegetable"
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position] as Fragment
    }

    fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}
