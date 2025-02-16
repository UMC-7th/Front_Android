package com.example.umc.Diet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.databinding.FragmentFavoriteBinding

class DietFavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteItems = listOf(
        FavoriteItem("https://example.com/imageUrl1.png", "빵, 바나나, 계란후라이", "560 kcal"),
        FavoriteItem("https://example.com/imageUrl2.png", "사과, 땅콩잼", "116 kcal"),
        FavoriteItem("https://example.com/imageUrl3.png", "계란후라이, 토스트, 바나나", "135 kcal"),
        FavoriteItem("https://example.com/imageUrl4.png", "두부 볶음, 파, 양파, 밥", "583 kcal")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDietFavorite.layoutManager = GridLayoutManager(context, 2)

        // Adapter 설정
        val adapter = DietFavoriteAdapter { item, position ->
            onFavoriteItemClicked(item) // 수정: item을 전달
        }
        binding.rvDietFavorite.adapter = adapter

        // 어댑터에 아이템 설정
        adapter.updateItems(favoriteItems)

        // 텍스트뷰 클릭 리스너 설정
        binding.tvNew.setOnClickListener {
            binding.tvNew.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary_Orange1))
            binding.tvCalorie.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray7))

            adapter.updateItems(favoriteItems)
        }

        binding.tvCalorie.setOnClickListener {
            binding.tvNew.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray7))
            binding.tvCalorie.setTextColor(ContextCompat.getColor(requireContext(), R.color.Primary_Orange1))

            // 칼로리 순으로 정렬
            val sortedItems = favoriteItems.sortedBy { it.calories.replace(" kcal", "").toInt() }
            adapter.updateItems(sortedItems)
        }
    }

    private fun onFavoriteItemClicked(item: FavoriteItem) {
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
        mainActivity?.showTitle("즐겨찾기", true)
        mainActivity?.hideBottomBar()
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle("즐겨찾기", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
