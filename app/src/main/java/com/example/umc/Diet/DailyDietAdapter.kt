package com.example.umc.Diet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R
import com.example.umc.databinding.ItemDietSubBinding

class DailyDietAdapter(private val dietList: List<DietItem>) :
    RecyclerView.Adapter<DailyDietAdapter.DietViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        val binding = ItemDietSubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DietViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val dietItem = dietList[position]
        holder.bind(dietItem)
    }

    override fun getItemCount(): Int = dietList.size

    inner class DietViewHolder(private val binding: ItemDietSubBinding) : RecyclerView.ViewHolder(binding.root) {

        private var isBreakfastChecked = false
        private var isLunchChecked = false
        private var isDinnerChecked = false

        fun bind(dietItem: DietItem) {
            binding.tvDate.text = dietItem.date
            binding.tvWeek.text = dietItem.day
            binding.tvFoodBreakfast.text = dietItem.breakfast
            binding.tvFoodLunch.text = dietItem.lunch
            binding.tvFoodDinner.text = dietItem.dinner

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
            binding.ibtnCheckBreakfast.setImageResource(if (isBreakfastChecked) R.drawable.ic_uncheck else R.drawable.ic_check)
            binding.ibtnCheckLunch.setImageResource(if (isLunchChecked) R.drawable.ic_uncheck else R.drawable.ic_check)
            binding.ibtnCheckDinner.setImageResource(if (isDinnerChecked) R.drawable.ic_uncheck else R.drawable.ic_check)

            updateBackground()
        }

        private fun updateBackground() {
            val isAnyChecked = isBreakfastChecked || isLunchChecked || isDinnerChecked
            binding.root.setBackgroundResource(
                if (isAnyChecked) R.drawable.bg_diet_sub_selected else R.drawable.bg_diet_sub_unselected
            )
        }
    }
}
