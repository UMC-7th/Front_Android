package com.example.subscribe

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R
import com.example.umc.model.Address

class AddressAdapter(
    private val addressList: List<Address>,
    private val onAddAddressClicked: () -> Unit  // "신규 배송지 추가" 버튼 클릭 이벤트
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0  // 배송지 설명 헤더
        private const val VIEW_TYPE_ITEM = 1    // 주소 아이템
        private const val VIEW_TYPE_BUTTON = 2  // 신규 배송지 추가 버튼
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> VIEW_TYPE_HEADER  // 첫 번째 아이템은 "배송지" 텍스트 헤더
            position < addressList.size + 1 -> VIEW_TYPE_ITEM  // 주소 리스트 아이템
            else -> VIEW_TYPE_BUTTON  // 마지막 아이템은 "신규 배송지 추가" 버튼
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_view_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_address, parent, false)
                AddressViewHolder(view)
            }
            VIEW_TYPE_BUTTON -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_button, parent, false)
                AddButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddressViewHolder -> {
                val addressIndex = position - 1 // 헤더를 제외한 실제 데이터 인덱스
                if (addressIndex in addressList.indices) {
                    val address = addressList[addressIndex]
                    holder.name.text = address.name
                    holder.postcode.text = address.postcode
                    holder.address.text = address.address
                    holder.phone.text = address.phone
                    holder.memo.text = address.memo

                    // iv_check 클릭 시 이미지 & 카드 테두리 변경
                    holder.ivCheck.setOnClickListener {
                        holder.ivCheck.setImageResource(R.drawable.orangecheck)  // 이미지 변경
                        //holder.cardView.setBackgroundResource(R.drawable.cardview_orange_border)  // 테두리 변경
                    }
                }
            }
            is HeaderViewHolder -> {
                // "기본 배송지"와 "신규 배송지" 텍스트 색상 강조
                val spannable = SpannableString("기본 배송지를 설정하고 신규 배송지도 추가할 수 있어요")
                spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF7300")), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // "기본 배송지" 색상 변경
                spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF7300")), 13, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // "신규 배송지" 색상 변경

                holder.deliveryDesc.text = spannable
            }
            is AddButtonViewHolder -> {
                holder.addButton.setOnClickListener {
                    onAddAddressClicked()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return addressList.size + 2 // 헤더(1) + 주소 목록(n) + "신규 배송지 추가" 버튼(1)
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deliveryDesc: TextView = view.findViewById(R.id.tv_delivery_desc)
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tv_name)
        val postcode: TextView = view.findViewById(R.id.tv_postcode)
        val address: TextView = view.findViewById(R.id.tv_address)
        val phone: TextView = view.findViewById(R.id.tv_phone)
        val memo: TextView = view.findViewById(R.id.tv_memo)
        val ivCheck: ImageView = view.findViewById(R.id.iv_check)
        //val cardView: CardView = view.findViewById(R.id.cardView)  // 카드뷰 참조 추가
    }

    class AddButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addButton: Button = view.findViewById(R.id.btn_add_address)
    }
}
