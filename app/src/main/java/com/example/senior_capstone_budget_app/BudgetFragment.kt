package com.example.senior_capstone_budget_app

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_budget.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BudgetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BudgetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        additional_income_edit_text.visibility = View.GONE
        additional_income_amount_prompt.visibility = View.GONE


        expected_work_income_edit_text.setText(budget?.expectedIncome.toString())
        additional_income_edit_text.setText(budget?.additionalIncome.toString())
        if (budget!!.additionalIncome > 0.0){
            additional_income_switch.isChecked = true
            additional_income_edit_text.visibility = View.VISIBLE
            additional_income_amount_prompt.visibility = View.VISIBLE
        }
        var totalIncome = 0.0
        totalIncome = budget!!.expectedIncome + budget!!.additionalIncome
        total_income.setText(totalIncome.toString())

        savings_budget_edit_text.setText(budget!!.limits[8].toString())
        personal_bugdet_edit_text.setText(budget!!.limits[5].toString())
        utilities_budget_edit_text.setText(budget!!.limits[2].toString())
        household_budget_edit_text.setText(budget!!.limits[4].toString())
        rent_budget_edit_text.setText(budget!!.limits[1].toString())
        medical_budget_edit_text.setText(budget!!.limits[6].toString())
        uncategorized_budget_edit_text.setText(budget!!.limits[0].toString())
        total_expenses.setText(budget?.totalExpenses.toString())

        expected_work_income_edit_text.addTextChangedListener(textWatcher2)
        additional_income_edit_text.addTextChangedListener(textWatcher2)

        savings_budget_edit_text.addTextChangedListener(textWatcher)
        personal_bugdet_edit_text.addTextChangedListener(textWatcher)
        utilities_budget_edit_text.addTextChangedListener(textWatcher)
        household_budget_edit_text.addTextChangedListener(textWatcher)
        rent_budget_edit_text.addTextChangedListener(textWatcher)
        medical_budget_edit_text.addTextChangedListener(textWatcher)
        uncategorized_budget_edit_text.addTextChangedListener(textWatcher)

        set_budget_button.setOnClickListener {
            //on click actions here
            editBudget()
            budget?.saveBudget(DashboardActivity().user)
            Toast.makeText(context, "Budget Successfully Saved", Toast.LENGTH_SHORT).show()
            
            // navigate back to dashboard
            findNavController().navigate(R.id.pieChartFragment)
        }

        additional_income_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                additional_income_edit_text.visibility = View.VISIBLE
                additional_income_amount_prompt.visibility = View.VISIBLE

            } else {
                additional_income_edit_text.visibility = View.GONE
                additional_income_amount_prompt.visibility = View.GONE
                budget?.additionalIncome = 0.0
                additional_income_edit_text.setText(budget?.additionalIncome.toString())
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(savings_budget_edit_text.text.toString())
                || !TextUtils.isEmpty(personal_bugdet_edit_text.text.toString())
                || !TextUtils.isEmpty(utilities_budget_edit_text.text.toString())
                || !TextUtils.isEmpty(household_budget_edit_text.text.toString())
                || !TextUtils.isEmpty(rent_budget_edit_text.text.toString())
                || !TextUtils.isEmpty(medical_budget_edit_text.text.toString())
                || !TextUtils.isEmpty(uncategorized_budget_edit_text.text.toString())){

                editBudget()
                total_expenses.setText(budget?.totalExpenses.toString())
            } else {
                total_expenses.text = ""
            }
        }
    }

    private val textWatcher2 = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(expected_work_income_edit_text.text.toString())
                || !TextUtils.isEmpty(personal_bugdet_edit_text.text.toString())
                || !TextUtils.isEmpty(additional_income_edit_text.text.toString())){

                editBudget()
                var totalIncome = 0.0
                totalIncome = budget!!.expectedIncome + budget!!.additionalIncome
                total_income.text = totalIncome.toString()
            } else {
                total_income.text = ""
            }
        }
    }

    private fun editBudget(){
        var b: DoubleArray = doubleArrayOf(uncategorized_budget_edit_text.text.toString().toDouble(), rent_budget_edit_text.text.toString().toDouble(), utilities_budget_edit_text.text.toString().toDouble(), 0.0,
            household_budget_edit_text.text.toString().toDouble(), personal_bugdet_edit_text.text.toString().toDouble(), medical_budget_edit_text.text.toString().toDouble(), 0.0, savings_budget_edit_text.text.toString().toDouble())

        budget?.limits = b
        budget?.calculateTotal()

        budget?.expectedIncome = expected_work_income_edit_text.text.toString().toDouble()
        budget?.additionalIncome = additional_income_edit_text.text.toString().toDouble()

        println(budget?.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BudgetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BudgetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}