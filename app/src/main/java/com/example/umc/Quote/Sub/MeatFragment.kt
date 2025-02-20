package com.example.umc.Quote.Sub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.R

class MeatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃 파일을 inflate하고 반환합니다.
        return inflater.inflate(R.layout.fragment_ranking_meat, container, false)
    }
}
