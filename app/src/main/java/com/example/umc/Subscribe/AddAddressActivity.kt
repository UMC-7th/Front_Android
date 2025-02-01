package com.example.subscribe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R

class AddAddressActivity : AppCompatActivity() {
    private lateinit var adapter: AddressAdapter
    private lateinit var addressList: MutableList<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnBack = findViewById<ImageButton>(R.id.btn_back) // 뒤로 가기 버튼

        //  뒤로 가기 버튼 클릭 시 AddressActivity로 이동
        btnBack.setOnClickListener {
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }

        // 기존 배송지 리스트 (김태현 + 양유진)
        addressList = mutableListOf(
            Address("김태현", "[00000]", "서울시 송파구 송파동 송파아파트 101동 101호", "010-1234-5678", "문 앞(1234)"),
            Address("양유진", "[00000]", "서울시 강남구 강남동 강남아파트 101동 101호", "010-1234-5678", "문 앞(5678)")
        )


        adapter = AddressAdapter(addressList) { }
        recyclerView.adapter = adapter

        val btnComplete = findViewById<Button>(R.id.btn_complete)
        val etPostcode = findViewById<EditText>(R.id.et_postcode)
        val etAddress = findViewById<EditText>(R.id.et_address)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val etMemo = findViewById<EditText>(R.id.et_memo)

        // "추가 완료" 버튼 클릭 시 입력된 정보를 리스트에 추가
        btnComplete.setOnClickListener {
            val newAddress = Address(
                "이름",
                etPostcode.text.toString(),
                etAddress.text.toString(),
                etPhone.text.toString(),
                etMemo.text.toString()
            )

            addressList.add(newAddress)  // 리스트에 추가
            adapter.notifyItemInserted(addressList.size)  // RecyclerView 갱신
            finish()  // 현재 화면 종료 후 이전 화면으로 이동
        }
    }
}
