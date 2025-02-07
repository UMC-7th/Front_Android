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
    private val maxTextLength = 50  // 최대 글자 수

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

        binding.imgChangeAdd.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.showTitle("배송지 변경", true)

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, SubAddressFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        setupUI()
        setupClickListeners()
        setupTextWatcher() // 글자 수 세는 기능 추가
    }

    private fun setupUI() {
        // 주문자 정보 설정
        binding.apply {
            textView16.text = "김태현" // 주문자 이름
            textView17.text = "[00000]" // 우편번호
            textView18.text = "서울시 송파구 송파동 송마아파트 101동 101호" // 주소
            textView19.text = "010-1234-5678" // 전화번호
            textView21.text = "문앞 (1234)" // 배송 요청사항
        }

        // 결제 금액 정보 설정
        setupPaymentInfo()
    }

    private fun setupPaymentInfo() {
        binding.apply {
            // 결제 상세 정보 설정
            val totalAmount = "56,000원"

            // 결제하기 버튼 텍스트 설정
            creditbutton.text = "${totalAmount}결제하기"
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            // 결제 수단 버튼들
            NaverPay.setOnClickListener {
                // 네이버페이 결제 처리
            }

            KakaoPay.setOnClickListener {
                // 카카오페이 결제 처리
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
                binding.textView22.text = "$textLength/$maxTextLength"  // 글자 수 업데이트
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