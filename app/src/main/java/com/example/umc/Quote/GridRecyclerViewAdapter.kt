package com.example.pricefruit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.Quote.FoodItem
import com.example.umc.R

class GridRecyclerViewAdapter(
    private val itemList: List<FoodItem>,
    private val fragment: FoodPriceReFragment // 현재 Fragment를 전달
) : RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: TextView = itemView.findViewById(R.id.item_image)
        val itemDescription: TextView = itemView.findViewById(R.id.item_description)

        init {
            // 아이템 클릭 시 FoodPriceReFragment로 전환
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val foodItem = itemList[position]

                    Log.d("GridRecyclerViewAdapter", "클릭됨: ${foodItem.name}") // 클릭된 아이템 로그 출력

                    // 새로운 Fragment 인스턴스 생성
                    val foodPriceFragment = FoodPriceReFragment()
                    val bundle = Bundle().apply {
                        putString("food_name", foodItem.name)
                        putString("food_price", foodItem.price)
                        putString("price_unit", foodItem.unit)
                        putString("price_percent", "+3.8%") // 예시로 % 추가
                    }
                    foodPriceFragment.arguments = bundle

                    // 프래그먼트 교체
                    fragment.requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_container, foodPriceFragment)  // 해당 컨테이너에 교체
                        addToBackStack(null) // 백스택에 추가하여 뒤로가기 가능
                        commit() // 트랜잭션 실행
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = itemList[position]
        holder.itemDescription.text = "세부 설명 ${position + 1}"  // 설명 텍스트 설정
        holder.itemImage.text = foodItem.imageUrl  // 이미지 URL을 텍스트로 표시 (이미지 로딩 필요)
    }

    override fun getItemCount(): Int = itemList.size
}
