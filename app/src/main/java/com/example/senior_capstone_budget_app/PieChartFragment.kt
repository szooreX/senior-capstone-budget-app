package com.example.senior_capstone_budget_app

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.senior_capstone_budget_app.R
import kotlinx.android.synthetic.main.fragment_pie_chart.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PieChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PieChartFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_pie_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPieChart(piechart)
    }

    private fun createPieChart(pieChart: PieChart?) {
        // Set the percentage of language used
        personalPercentage.text = Integer.toString(23)
        miscellaneousPercentage.text = Integer.toString(15)
        housingPercentage.text = Integer.toString(40)
        groceriesPercentage.text = Integer.toString(12)
        utilitiesPercentage.text = Integer.toString(60)
        medicalPercentage.text = Integer.toString(3)
        savingsPercentage.text = Integer.toString(7)


        pieChart?.addPieSlice(
            PieModel(
                "Medical", medicalPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#18AF2C")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Personal", personalPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#4D8558")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Miscellaneous", miscellaneousPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#063E3B")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Housing", housingPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#122B54")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Groceries", groceriesPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#446192")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Utilities", utilitiesPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#6AAAFA")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Savings", savingsPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#ACC4E2")
            )
        )

        // To animate the pie chart
        pieChart?.startAnimation();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PieChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PieChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}