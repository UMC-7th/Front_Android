package com.example.umc.Diet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R
import com.example.umc.databinding.ItemMenuBinding

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
                // Glide.with(ivMenuImage)
                //     .load(menuItem.imageUrl)
                //     .into(ivMenuImage)

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