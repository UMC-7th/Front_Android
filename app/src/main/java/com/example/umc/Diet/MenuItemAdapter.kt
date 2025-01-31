package com.example.umc.Diet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.databinding.ItemMenuBinding

class MenuItemAdapter(private val itemClickListener: ((MenuItem) -> Unit)? = null) : ListAdapter<MenuItem, MenuItemAdapter.MenuViewHolder>(
    MenuDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }

    class MenuViewHolder(
        private val binding: ItemMenuBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem, clickListener: ((MenuItem) -> Unit)?) {
            binding.apply {
                tvMenuName.text = item.name
                tvMenuCalories.text = item.calories
                // Glide나 Coil을 사용하여 이미지 로드
                // Glide.with(ivMenuImage).load(item.imageUrl).into(ivMenuImage)
                btnOrder.isSelected = item.isSelected
                root.setOnClickListener { clickListener?.invoke(item) }
            }
        }
    }
}

class MenuDiffCallback : DiffUtil.ItemCallback<MenuItem>() {
    override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
        return oldItem == newItem
    }
}
