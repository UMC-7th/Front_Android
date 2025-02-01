package com.example.umc.Quote


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pricefruit.FruitPriceActivity
import com.example.umc.databinding.FragmentQuoteSubBinding

class QuoteFragmentSub : Fragment() {

    private var _binding: FragmentQuoteSubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuoteSubBinding.inflate(inflater, container, false)

        // 🍌 banana ID를 가진 ImageView 클릭 시 FruitPriceActivity로 이동
        binding.banana.setOnClickListener {
            val intent = Intent(requireContext(), FruitPriceActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
