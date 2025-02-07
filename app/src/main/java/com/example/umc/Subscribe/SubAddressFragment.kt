package com.example.umc.Subscribe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.umc.R
import com.example.umc.model.Address
import com.example.umc.Main.MainActivity

class SubAddressFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddressAdapter
    private lateinit var addAddressButton: Button
    private lateinit var cvAddAddress: View
    private lateinit var editTextName: EditText
    private lateinit var editTextPostcode: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextMemo: EditText
    private val addressList = mutableListOf(
        Address("김태현", "[00000]", "서울시 송파구 송파동 송파아파트 101동 101호", "010-1234-5678", "문 앞(1234)"),
        Address("양유진", "[00000]", "서울시 강남구 강남동 강남아파트 101동 101호", "010-1234-5678", "문 앞(5678)")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sub_address, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        addAddressButton = view.findViewById(R.id.btn_add_address)
        cvAddAddress = view.findViewById(R.id.cv_add_address)
        editTextName = view.findViewById(R.id.tv_name)
        editTextPostcode = view.findViewById(R.id.tv_postcode)
        editTextAddress = view.findViewById(R.id.tv_address)
        editTextPhone = view.findViewById(R.id.tv_phone)
        editTextMemo = view.findViewById(R.id.tv_memo)

        adapter = AddressAdapter(addressList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        addAddressButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray7))

        addAddressButton.setOnClickListener {
            if (cvAddAddress.visibility == View.GONE) {
                cvAddAddress.visibility = View.VISIBLE
                addAddressButton.text = "추가완료"
                addAddressButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray7))
                (activity as? MainActivity)?.hideBottomBar()
            } else {
                val newAddress = editTextAddress.text.toString()
                if (newAddress.isNotEmpty()) {
                    val address = Address(editTextName.text.toString(), editTextPostcode.text.toString(), newAddress, editTextPhone.text.toString(), editTextMemo.text.toString())
                    addressList.add(address)
                    adapter.notifyItemInserted(addressList.size - 1)
                    editTextName.text.clear()
                    editTextPostcode.text.clear()
                    editTextAddress.text.clear()
                    editTextPhone.text.clear()
                    editTextMemo.text.clear()
                    cvAddAddress.visibility = View.GONE
                    addAddressButton.text = "신규 배송지 추가"
                    addAddressButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Gray7))
                    (activity as? MainActivity)?.showBottomBar()
                }
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonColor()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editTextName.addTextChangedListener(textWatcher)
        editTextPostcode.addTextChangedListener(textWatcher)
        editTextAddress.addTextChangedListener(textWatcher)
        editTextPhone.addTextChangedListener(textWatcher)
        editTextMemo.addTextChangedListener(textWatcher)

        return view
    }

    private fun updateButtonColor() {
        val isFilled = editTextName.text.isNotEmpty() && editTextPostcode.text.isNotEmpty() && editTextAddress.text.isNotEmpty() && editTextPhone.text.isNotEmpty() && editTextMemo.text.isNotEmpty()
        val color = if (isFilled) R.color.Primary_Orange1 else R.color.Gray7
        addAddressButton.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showBottomBar()
    }

}
