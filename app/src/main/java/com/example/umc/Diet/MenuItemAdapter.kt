package com.example.umc.Diet

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.umc.R
import com.example.umc.UserApi.RetrofitClient
import com.example.umc.databinding.ItemMenuBinding
import kotlinx.coroutines.launch

class MenuItemAdapter(private val onClick: (MenuItem, Int) -> Unit) :
    ListAdapter<MenuItem, MenuItemAdapter.ViewHolder>(DiffCallback()) {

    private var selectedPosition = -1

    inner class ViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: MenuItem) {
            binding.apply {
                tvMenuName.text = menuItem.name
                tvMenuCalories.text = menuItem.calories

                // 이미지 로딩 (Glide 사용 시)
                loadMealImage(menuItem.name, ivMenuImage)

                // 선택 상태에 따른 테두리 설정
                root.background = if (adapterPosition == selectedPosition) {
                    ContextCompat.getDrawable(root.context, R.color.selector_home_menu_item)
                } else {
                    ContextCompat.getDrawable(root.context, R.color.selector_home_menu_item)
                }

                root.setOnClickListener {
                    selectedPosition = adapterPosition
                    notifyDataSetChanged()
                    onClick(menuItem, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private fun loadMealImage(foodName: String, imageView: ImageView) {
        val context = imageView.context
        (context as? LifecycleOwner)?.lifecycleScope?.launch {
            try {
                val response = RetrofitClient.imageApiService.getMealImage(foodName)

                if (response.isSuccessful) {
                    val imageUrl = response.body()?.success?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(context)
                            .load(imageUrl)
                            .into(imageView) // 이미지 뷰에 적용
                    } else {
                        Log.e("MenuItemAdapter", "이미지 URL이 비어 있음")
                    }
                } else {
                    Log.e("MenuItemAdapter", "API 호출 실패: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MenuItemAdapter", "네트워크 오류: ${e.message}")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<MenuItem>() {
        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem) =
            oldItem == newItem
    }
}
