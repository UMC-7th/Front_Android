package com.example.umc.Subscribe

import androidx.recyclerview.widget.RecyclerView
import com.example.umc.databinding.ItemOrderBinding

class OrderMenuViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: OrderMenuItem) {
        with(binding) {
            deliveryDateTv.text = item.menuDate
            breakfastTitleTv.text = "아침"
            breakfastMenuTv.text = item.breakfastName
            breakfastKcalTv.text = "${item.breakfastKcal}Kcal"
            breakfastPriceTv.text = "${item.breakfastPrice}원"
            lunchTitleTv.text = "점심"
            lunchMenuTv.text = item.lunchName
            lunchKcalTv.text = "${item.lunchKcal}Kcal"
            lunchPriceTv.text = "${item.lunchPrice}원"
        }
    }
}