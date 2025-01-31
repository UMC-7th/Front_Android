package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc.MainActivity
import com.example.umc.MenuItem
import com.example.umc.MenuItemAdapter
import com.example.umc.R
import com.example.umc.databinding.FragmentDailyHomeBinding

class DailyHomeFragment : Fragment() {
    private var _binding: FragmentDailyHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        val adapter = MenuItemAdapter { item -> onMenuItemClicked(item) }
        binding.rvMenuItems.layoutManager = LinearLayoutManager(context)
        binding.rvMenuItems.adapter = adapter
        adapter.submitList(getDummyMenuItems())
    }

    private fun onMenuItemClicked(item: MenuItem) {
        // DietDetailFragment로 전환
        val dietDetailFragment = DietDetailFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, dietDetailFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        // 상단바 보이게 + 바텀네비게이션 숨기기 (나중에 MainActivity로 다시 작업할 예정)
        val mainActivity = activity as? MainActivity
        val title = getString(R.string.breakfast) // 아침, 점심, 저녁인지 확인하는 코드는 나중에
        mainActivity?.showTitle(title, true)
        mainActivity?.hideBottomBar()
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
