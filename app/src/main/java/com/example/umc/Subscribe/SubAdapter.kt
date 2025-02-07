package com.example.umc.Subscribe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.databinding.ItemSubMainBinding
import com.example.umc.model.SubItem

class SubAdapter(
    private val subList: List<SubItem>,
    private val itemClickListener: (SubItem) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<SubAdapter.SubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val binding = ItemSubMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        val subItem = subList[position]
        holder.bind(subItem)
    }

    override fun getItemCount(): Int = subList.size

    inner class SubViewHolder(private val binding: ItemSubMainBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subItem: SubItem) {
            binding.tvSubItem1.text = subItem.item1
            binding.tvSubItem2.text = subItem.item2

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                if (subItem.item1 == "맛있는 일상 음식 구독") {
                    itemClickListener(subItem)
                }
            }
        }
    }
}
