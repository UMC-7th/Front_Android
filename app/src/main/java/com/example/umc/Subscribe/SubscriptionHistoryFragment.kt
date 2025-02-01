

// SubscriptionHistoryFragment.kt
package com.example.umc.Subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.R
import com.example.umc.Main.MainActivity

class SubscriptionHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscription_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.showTitle("구독 내역", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // MainActivity에서 hideTitle 메서드가 있는지 확인하세요
        (activity as? MainActivity)?.hideTitle()
    }
}