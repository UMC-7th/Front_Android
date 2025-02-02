package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
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
        breakfastAdapter = MenuItemAdapter { item: MenuItem, position: Int ->
            selectedBreakfastPosition = position
            breakfastAdapter.notifyDataSetChanged()
            onMenuItemClicked(item, "아침")
        }

        lunchAdapter = MenuItemAdapter { item: MenuItem, position: Int ->
            selectedLunchPosition = position
            lunchAdapter.notifyDataSetChanged()
            onMenuItemClicked(item, "점심")
        }

        dinnerAdapter = MenuItemAdapter { item: MenuItem, position: Int ->
            selectedDinnerPosition = position
            dinnerAdapter.notifyDataSetChanged()
            onMenuItemClicked(item, "저녁")
        }
    }

    private fun setupRecyclerViews() {
        binding.apply {
            rvBreakfast.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = breakfastAdapter
            }
            breakfastAdapter.submitList(getDummyMenuItems())

            rvLunch.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = lunchAdapter
            }
            lunchAdapter.submitList(getDummyMenuItems())

            rvDinner.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = dinnerAdapter
            }
            dinnerAdapter.submitList(getDummyMenuItems())
        }
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