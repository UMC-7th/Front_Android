package com.example.umc.Subscribe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.Diet.DietItem
import com.example.umc.R
import com.example.umc.databinding.ItemDietSubBinding

class SubscribeDietAdapter(
    private var items: List<DietItem>,
    private val listener: OnDietCheckedChangeListener
) : RecyclerView.Adapter<SubscribeDietAdapter.DietViewHolder>() {

    fun updateItems(newItems: List<DietItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val binding = ItemDietSubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DietViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val dietItem = items[position] // dietList를 items로 변경
        holder.bind(dietItem)
    }

    override fun getItemCount(): Int = items.size // dietList를 items로 변경

    inner class DietViewHolder(private val binding: ItemDietSubBinding) : RecyclerView.ViewHolder(binding.root) {

        private var isBreakfastChecked = false
        private var isLunchChecked = false
        private var isDinnerChecked = false

        fun bind(dietItem: DietItem) {
            binding.tvDate.text = dietItem.date
            binding.tvWeek.text = dietItem.day // day를 week로 변경
            binding.tvFoodBreakfast.text = dietItem.breakfast
            binding.tvFoodLunch.text = dietItem.lunch
            binding.tvFoodDinner.text = dietItem.dinner

            // 체크박스 초기 상태 리셋
            isBreakfastChecked = false
            isLunchChecked = false
            isDinnerChecked = false

            updateBackground()

            binding.ibtnCheckBreakfast.setOnClickListener {
                isBreakfastChecked = !isBreakfastChecked
                updateButtonState()
            }

            binding.ibtnCheckLunch.setOnClickListener {
                isLunchChecked = !isLunchChecked
                updateButtonState()
            }

            binding.ibtnCheckDinner.setOnClickListener {
                isDinnerChecked = !isDinnerChecked
                updateButtonState()
            }
        }

        private fun updateButtonState() {
            binding.ibtnCheckBreakfast.setImageResource(
                if (isBreakfastChecked) R.drawable.ic_uncheck else R.drawable.ic_check
            )
            binding.ibtnCheckLunch.setImageResource(
                if (isLunchChecked) R.drawable.ic_uncheck else R.drawable.ic_check
            )
            binding.ibtnCheckDinner.setImageResource(
                if (isDinnerChecked) R.drawable.ic_uncheck else R.drawable.ic_check
            )

            updateBackground()
        }

        private fun updateBackground() {
            val isAnyChecked = isBreakfastChecked || isLunchChecked || isDinnerChecked
            binding.root.setBackgroundResource(
                if (isAnyChecked) R.drawable.bg_diet_sub_selected
                else R.drawable.bg_diet_sub_unselected
            )
            listener.onDietCheckedChanged(isAnyChecked)
        }
    }
}

interface OnDietCheckedChangeListener {
    fun onDietCheckedChanged(isAnyChecked: Boolean)
}