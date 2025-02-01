package com.example.subscribe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R

class AddressActivity : AppCompatActivity() {
    private lateinit var adapter: AddressAdapter
    private lateinit var addressList: MutableList<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 기존 배송지 리스트 (김태현 + 양유진)
        addressList = mutableListOf(
            Address("김태현", "[00000]", "서울시 송파구 송파동 송파아파트 101동 101호", "010-1234-5678", "문 앞(1234)"),
            Address("양유진", "[00000]", "서울시 강남구 강남동 강남아파트 101동 101호", "010-1234-5678", "문 앞(5678)")
        )

        adapter = AddressAdapter(addressList) {
            val intent = Intent(this, AddAddressActivity::class.java)
            startActivity(intent)
        }

        recyclerView.adapter = adapter  //  반드시 어댑터 설정 필요
    }
}
