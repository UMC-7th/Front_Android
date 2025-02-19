package com.example.umc.Subscribe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.databinding.ItemSubMainBinding
import com.example.umc.databinding.ItemSubscriptionFooterBinding
import com.example.umc.model.SubItem

class SubAdapter(
    // items를 private var로 선언하여 updateItems()에서 수정할 수 있게 합니다
    private var items: List<SubItem>,
    // 아이템 클릭 콜백을 생성자에서 받습니다
    private val onItemClick: (SubItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() { // ViewHolder 타입을 RecyclerView.ViewHolder로 변경

    // 뷰 타입을 구분하는 상수를 정의합니다
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_FOOTER = 1
    }

    // 아이템 목록을 업데이트하는 메서드입니다
    fun updateItems(newItems: List<SubItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    // ViewHolder를 생성하는 메서드입니다
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FOOTER -> {
                val binding = ItemSubscriptionFooterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                FooterViewHolder(binding)
            }
            else -> {
                val binding = ItemSubMainBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SubViewHolder(binding)
            }
        }
    }

    // ViewHolder에 데이터를 바인딩하는 메서드입니다
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SubViewHolder -> {
                // items 리스트의 크기를 체크하여 안전하게 접근합니다
                if (position < items.size) {
                    val subItem = items[position]
                    holder.bind(subItem)
                }
            }
            is FooterViewHolder -> {
                // Footer는 별도의 바인딩이 필요 없습니다
            }
        }
    }

    // 전체 아이템 개수를 반환하는 메서드입니다 (아이템 + 푸터)
    override fun getItemCount(): Int = items.size + 1

    // 각 포지션의 뷰 타입을 반환하는 메서드입니다
    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }

    // 일반 아이템을 위한 ViewHolder 클래스입니다
    inner class SubViewHolder(private val binding: ItemSubMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subItem: SubItem) {
            binding.tvSubItem1.text = subItem.item1
            binding.tvSubItem2.text = subItem.item2

            // 클릭 리스너를 설정합니다
            binding.root.setOnClickListener {
                if (subItem.item1 == "맛있는 일상 음식 구독") {
                    onItemClick(subItem) // 생성자에서 받은 콜백을 사용합니다
                }
            }
        }
    }

    // 푸터를 위한 ViewHolder 클래스입니다
    class FooterViewHolder(binding: ItemSubscriptionFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
}