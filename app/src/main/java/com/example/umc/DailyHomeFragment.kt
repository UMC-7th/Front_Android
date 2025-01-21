package com.example.umc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.databinding.FragmentDailyHomeBinding
import com.example.umc.databinding.FragmentHomeBinding

class DailyHomeFragment : Fragment() {
    private var _binding: FragmentDailyHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // RecyclerView 설정
        val adapter = MenuItemAdapter()
        binding.rvMenuItems.adapter = adapter
        // 더미 데이터 설정
        adapter.submitList(getDummyMenuItems())
    }

    private fun getDummyMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem("image_url1", "메뉴1", "560Kcal"),
            MenuItem("image_url2", "메뉴2", "450Kcal")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

