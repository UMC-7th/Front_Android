package com.example.umc.Subscribe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.databinding.ItemOrderBinding

class OrderMenuAdapter : RecyclerView.Adapter<OrderMenuViewHolder>() {
    private var items = listOf<OrderMenuItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderMenuViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderMenuViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<OrderMenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}