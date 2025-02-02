package com.example.umc.Diet

import DietAddManualViewModel
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.example.umc.Diet.manual.DietAddConfirmFragment
import com.example.umc.Main.MainActivity
import com.example.umc.R
import com.example.umc.model.request.PostManualMealsRequest
import java.util.*

class DietAddManualFragment : Fragment(R.layout.fragment_diet_add_manual) {

    private lateinit var dateSpinner: Spinner
    private lateinit var etAddFood: LinearLayout
    private lateinit var etAddCalorie: EditText
    private val foodList = mutableListOf<String>()
    private val viewModel: DietAddManualViewModel by viewModels()
    private var selectedTime: String? = null
    private lateinit var monthButton: Button

    private var selectedDate: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        dateSpinner = view.findViewById(R.id.spinner_date)
        etAddFood = view.findViewById(R.id.et_add_food)
        etAddCalorie = view.findViewById(R.id.et_add_calorie)
        monthButton = view.findViewById(R.id.bt_month)

        // Get the current month and set it to the button text
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        monthButton.text = "${currentMonth}월"

        // Set up the date spinner
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dateList = (1..daysInMonth).map { "${it}일" }
        val dateAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, dateList)
        dateAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        dateSpinner.adapter = dateAdapter

        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDay = dateList[position].replace("일", "").toInt()
                val dateStr = "$currentYear-${"%02d".format(currentMonth)}-${"%02d".format(selectedDay)}T00:00:00.000Z"
                selectedDate = dateStr
                Log.d("MealLogging", "선택한 날짜: $selectedDate")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set up time buttons
        val timeButtons = listOf(
            view.findViewById<Button>(R.id.bt_morning),
            view.findViewById<Button>(R.id.bt_lunch),
            view.findViewById<Button>(R.id.bt_dinner),
            view.findViewById<Button>(R.id.bt_snack)
        )

        timeButtons.forEach { button ->
            button.setOnClickListener {
                selectedTime = button.text.toString()
                Log.d("MealLogging", "선택한 시간대: $selectedTime")

                timeButtons.forEach { btn ->
                    btn.setBackgroundColor(
                        if (btn == button) resources.getColor(R.color.Gray4, null)
                        else resources.getColor(R.color.Gray7, null)
                    )
                }
            }
        }

        // Add new food input fields
        view.findViewById<Button>(R.id.bt_add_food_text).setOnClickListener {
            addNewEditText()
        }

        view.findViewById<Button>(R.id.bt_save).setOnClickListener {
            etAddCalorie.text.clear()
            foodList.clear()

            for (i in 0 until etAddFood.childCount) {
                val editText = etAddFood.getChildAt(i) as EditText
                val food = editText.text.toString()
                if (food.isNotEmpty()) foodList.add(food)
                editText.text.clear()
            }

            val calorie = etAddCalorie.text.toString().toIntOrNull() ?: 0
            if (selectedTime != null && selectedDate != null) {
                Log.d("MealLogging", "Saving meal with data: Date = $selectedDate, Time = $selectedTime, Foods = $foodList, Calories = $calorie")
                sendManualMealsRequest(selectedDate!!, selectedTime!!, foodList, calorie)
                val originalColor = it.background
                it.setBackgroundColor(resources.getColor(R.color.Primary_Orange1, null))

                Handler().postDelayed({
                    it.setBackgroundColor(resources.getColor(R.color.Gray7, null)) // Reset to original color or another color
                }, 100)
            } else {
                Log.e("MealLogging", "날짜와 시간대를 선택해주세요.")
            }

            val confirmFragment = DietAddConfirmFragment()
            val bundle = Bundle()
            bundle.putSerializable("mealList", ArrayList(foodList)) // Pass foodList as Serializable
            confirmFragment.arguments = bundle
        }

        val dietAddConfirmButton: Button = view.findViewById(R.id.bt_add_confirm)
        dietAddConfirmButton.setOnClickListener {
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            val confirmFragment = DietAddConfirmFragment()
            val bundle = Bundle()
            bundle.putSerializable("mealList", ArrayList(foodList)) // Pass foodList as Serializable
            confirmFragment.arguments = bundle

            transaction.replace(R.id.main_container, confirmFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun addNewEditText() {
        val inflater = LayoutInflater.from(requireContext())
        val newEditText = inflater.inflate(R.layout.item_edit_text, etAddFood, false) as EditText
        etAddFood.addView(newEditText)
    }

    private fun sendManualMealsRequest(date: String, time: String, foods: List<String>, calorie: Int) {
        val request = PostManualMealsRequest(
            addedByUser = true,
            calorieTotal = calorie,
            foods = foods,
            mealDate = date,
            time = time,
            userId = 1 // 임의로 설정
        )

        viewModel.addManualMeal(request,
            onSuccess = {
                Log.d("MealLogging", "식단 추가 성공")
            },
            onError = { errorMsg: String ->
                Log.e("MealLogging", "식단 추가 실패: $errorMsg")
            }
        )
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = activity as? MainActivity
        mainActivity?.showTitle("식단 수동 등록", true)
        mainActivity?.hideBottomBar()
    }
}
