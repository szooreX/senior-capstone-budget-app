package com.example.senior_capstone_budget_app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_pie_chart.*
import kotlinx.android.synthetic.main.month_chart_item.view.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.time.Month
import kotlin.math.log


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


    //______________Variables For Recycler View____________________
    private val monthChartAdapter = GroupAdapter<GroupieViewHolder>()
    private var mTrans: MonthlyTransactions? = null

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<MonthChartItem> = ArrayList()
        set(value) {
            monthChartAdapter.clear()

            for (sectionItem: MonthChartItem in value) {
                val monthChart = MonthChartAdapter(sectionItem)
                monthChartAdapter.add(monthChart)
            }
            field = value
        }
    //_____________________________________________________________

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
        mTrans = MonthlyTransactions()
        mTrans?.loadTransactions()
        mTrans?.transactionLoop()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPieChart(piechart)
        getMonthChartItems()

        month_chart_container.apply {
            month_chart_container.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            month_chart_container.adapter = monthChartAdapter
        }

        //put functional code here for function calls, etc.
    }


    private fun makeNewChart() : LineData {

        //programmatically create a view to add to month_chart_item.xml layout file
        var chart: LineChart = LineChart(context)

        /* The goal here is to get data from api data objects that would ideally
         contain the data for current and the previous months (just for the current year)
          total spending to compare */

        //api data array
        val dataObject = ArrayList<String>()

        // getMonthChartData(dataObject) // TODO:  create function to gather the months, starting on current month, and its former months data into an array


        //actual chart entry array
        val chartEntries = ArrayList<Entry>()

        //turn data into entries. ref this URl:  https://weeklycoding.com/mpandroidchart-documentation/getting-started/
        for (data in dataObject) {
            //chartEntries.add(dataObject.getValueX, dataObject.getValueY) TODO: gather data like this instead
            val entry = Entry(15.0F, 20.0F)
            chartEntries.add(20, entry)
        }

        //add entries to dataset
        var lineDataSet = LineDataSet(chartEntries, "My Label")
        lineDataSet.color = Color.CYAN

        val lineData = LineData(lineDataSet)


        return lineData
    }
    private fun getMonthChartItems() {
        //recycler view item array
        val monthChartItems = ArrayList<MonthChartItem>()

        //form each full chart into an item to pass into each slot in recycler view
        val item1 = MonthChartItem(
            "Month Name",
            0, makeNewChart()
        )
        val item2 = MonthChartItem(
            "Month Name",
            1, makeNewChart()
        )
//        val item3 = MonthChartItem(
//            "Month Name",
//            2, "yo make this a chart"
//        )
//        val item4 = MonthChartItem(
//            "Month Name",
//            3, "yo make this a chart"
//        )

        monthChartItems.add(item1)
        monthChartItems.add(item2)
//        monthChartItems.add(item3)
//        monthChartItems.add(item4)


        //pass array list to displayItems to pass through Adapter
        displayItems = monthChartItems
    }

    private fun createPieChart(pieChart: PieChart?) {
        // Set the percentage of language used

        Log.e("testing", mTrans?.getCategoryPercents(0).toString())
        personalPercentage.text = mTrans?.getCategoryPercents(5).toString();
        savingsPercentage.text = mTrans?.getCategoryPercents(8).toString();
        rentPercentage.text = mTrans?.getCategoryPercents(1).toString();
        householdPercentage.text = mTrans?.getCategoryPercents(4).toString();
        utilitiesPercentage.text = mTrans?.getCategoryPercents(2).toString();
        medicalPercentage.text = mTrans?.getCategoryPercents(6).toString();
        uncategorizedPercentage.text = mTrans?.getCategoryPercents(0).toString();

        pieChart?.addPieSlice(
            PieModel(
                "Medical", medicalPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#18AF2C")
            )
        )
        Log.e("medical percentage", medicalPercentage.text.toString())
        pieChart?.addPieSlice(
            PieModel(
                "Personal", personalPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#4D8558")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Savings", savingsPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#063E3B")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Rent", rentPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#122B54")
            )
        )
        pieChart?.addPieSlice(
            PieModel(
                "Household", householdPercentage.text.toString().toInt().toFloat(),
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
                "Uncategorized", uncategorizedPercentage.text.toString().toInt().toFloat(),
                Color.parseColor("#ACC4E2")
            )
        )


        // To animate the pie chart
        Log.e("Pie Chart", "Created")
        pieChart?.startAnimation();

        viewTransactions.setOnClickListener { findNavController().navigate(R.id.transactionsFragment) }
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


class MonthChartAdapter(private val item: MonthChartItem) :
    com.xwray.groupie.kotlinandroidextensions.Item() {
    private var itemID = item.id

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view
       viewHolder.itemView.chart_view.data = item.lineData



    }

    override fun getLayout(): Int {
        return R.layout.month_chart_item
    }

}


data class MonthChartItem(
    var month_name: String,
    var id: Int,
    var lineData: LineData
)