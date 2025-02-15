import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.Subscribe.SubAddressFragment
import com.example.umc.databinding.FragmentSubscribePaymentBinding

class Subscribecredit : Fragment() {
    private var _binding: FragmentSubscribePaymentBinding? = null
    private val binding get() = _binding!!
    private val maxTextLength = 50
    private var isDetailVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupClickListeners()
        setupTextWatcher()
        setupPriceDetailToggle()
    }

    private fun setupUI() {
        binding.apply {
            // 주문자 정보 설정
            textView16.text = "김태현"
            textView17.text = "[00000]"
            textView18.text = "서울시 송파구 송파동 송마아파트 101동 101호"
            textView19.text = "010-1234-5678"
            textView21.text = "문앞 (1234)"

            // 가격 상세 정보 초기 설정
            priceDetailContainer.visibility = View.GONE
        }
        setupPaymentInfo()
    }

    private fun setupPriceDetailToggle() {
        binding.priceToggleButton.setOnClickListener {
            isDetailVisible = !isDetailVisible
            binding.apply {
                priceDetailContainer.visibility = if (isDetailVisible) View.VISIBLE else View.GONE
                priceToggleButton.isSelected = isDetailVisible
            }
        }
    }

    private fun setupPaymentInfo() {
        binding.apply {
            val totalAmount = "56,000원"
            creditbutton.text = "${totalAmount}결제하기"

            // 가격 상세 정보 설정
            priceDetail1.text = "1인분 (12,000원) × 3"
            priceDetail2.text = "2인분 (20,000원) × 1"
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            imgChangeAdd.setOnClickListener {
                val mainActivity = activity as? MainActivity
                mainActivity?.showTitle("배송지 변경", true)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_container, SubAddressFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }

            NaverPay.setOnClickListener {
                it.isSelected = !it.isSelected
                KakaoPay.isSelected = false
            }

            KakaoPay.setOnClickListener {
                it.isSelected = !it.isSelected
                NaverPay.isSelected = false
            }

            creditbutton.setOnClickListener {
                // 최종 결제 처리
            }
        }
    }

    private fun setupTextWatcher() {
        binding.editText4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0
                binding.textView22.text = "$textLength/$maxTextLength"
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showTitle("결제", true)
        (activity as? MainActivity)?.hideBottomBar()
    }
}