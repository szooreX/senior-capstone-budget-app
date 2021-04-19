package com.example.senior_capstone_budget_app

import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senior_capstone_budget_app.budget.Budget
import com.example.senior_capstone_budget_app.transaction.MonthlyTransactions
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_pie_chart.*
import kotlinx.android.synthetic.main.month_chart_item.view.*
import kotlinx.android.synthetic.main.transaction_item.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.io.IOException
import java.io.InputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

var mT: MonthlyTransactions? = null
var budget: Budget? = null
var input = ""
var bInput = ""
var percentInt = 0
private var spentString = ""
private var percentString = ""

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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (mT == null){
            mT = MonthlyTransactions()
            try {
                val inputStream: InputStream =
                    activity?.applicationContext?.assets!!.open("TransactionSample.txt")
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                input = String(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mT?.loadTransactions2(input)
            mT?.transactionLoop()
        }

        if (budget == null){
            budget = Budget()
            try {
                val inputStream: InputStream = activity?.applicationContext?.assets!!.open("budget.txt")
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                bInput = String(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            budget?.loadBudget(bInput)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //DashboardActivity().mT?.loadTransactions2()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues()
    }

    override fun onResume(){
        super.onResume()
        setValues()
        createPieChart(piechart)
        getMonthChartItems()
    }

    private fun setValues(){
        val stringTotal = String.format("%.2f",mT?.total)
        val percent = ((mT!!.total / budget!!.totalExpenses) * 100).toInt().toString()
        val percent2 = ((mT!!.total / budget!!.expectedIncome) * 100).toInt().toString()
        percentInt = ((mT!!.total / budget!!.totalExpenses) * 100).toInt()
        spentString = "$$stringTotal Spent"
        percentString = "You've spent $percent% of your budget and $percent2% of your income."

        percent_budget.progress = (percentInt).toFloat()
        percent_budget.progressText = ((percentInt).toString() + "%")
        totalSpent.text = (spentString)
        percentBudget.text = (percentString)

        createPieChart(piechart)
        getMonthChartItems()

        month_chart_container.apply {
            month_chart_container.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            month_chart_container.adapter = monthChartAdapter
        }

        //println(mT.toString())

        //put functional code here for function calls, etc.

        budget_panel_button.setOnClickListener { findNavController().navigate(R.id.budgetFragment) }
    }


    private fun makeNewChart(month: Int): BarData {
        var monthTotal = 0.0
        monthTotal = mT!!.totals.get(month)

        /* The goal here is to get data from api data objects to compare total budget to total spent for each month*/

        var values = ArrayList<BarEntry>()
//        for (i in 0 until 2) {
//            values.add(BarEntry((i - 6).toFloat(), mT!!.totals[i].toFloat()))
//
//        }

        values.add(BarEntry(1F, 0F)) //formatting with this for now..


        //only change these ----------------
        values.add(BarEntry(3F, budget!!.totalExpenses.toFloat())) //budget amount
        BarEntry(7F, monthTotal.toFloat())?.let { values.add(it) } //spent of budget

        //----------------------------------

        values.add(BarEntry(9F, 0F)) //formatting with this for now..


        //api data array
        //val dataObject = ArrayList<Double>()

        // getMonthChartData(dataObject) // TODO:  create function to gather the months, starting on current month, and its former months data into an array

        //actual chart entry array
        val chartEntries = ArrayList<BarEntry>()

        for (data in values) {
            chartEntries.add(data)
            var entry = Entry(15.0F, 20.0F)
            //chartEntries.add(20, entry)
        }

        //turn data into entries. ref this URl:  https://weeklycoding.com/mpandroidchart-documentation/getting-started/
//        for (data in dataObject) {
//            //chartEntries.add(dataObject.getValueX, dataObject.getValueY) TODO: gather data like this instead
//            val entry = Entry(15.0F, 20.0F)
//            chartEntries.add(20, entry)
//        }

        //add entries to dataset
        var barDataSet = BarDataSet(chartEntries, "Budget Tracking")

        barDataSet.color = Color.parseColor("#6AAAFA")

        var barData = BarData(barDataSet)
        barData.barWidth = 3F

        return barData
    }

    private fun getMonthChartItems() {
        //___________spending verses budget per month________
        //recycler view item array
        val monthChartItems = ArrayList<MonthChartItem>()

        //form each full chart into an item to pass into each slot in recycler view
        val item1 = MonthChartItem(
            mT!!.getHistory(0),
            0, makeNewChart(0)
        )
        val item2 = MonthChartItem(
            mT!!.getHistory(1),
            1, makeNewChart(1)
        )
        val item3 = MonthChartItem(
            mT!!.getHistory(2),
            2, makeNewChart(2)
        )
        val item4 = MonthChartItem(
            mT!!.getHistory(3),
            3, makeNewChart(3)
        )
        val item5 = MonthChartItem(
            mT!!.getHistory(4),
            4, makeNewChart(4)
        )
        val item6 = MonthChartItem(
            mT!!.getHistory(5),
            5, makeNewChart(5)
        )
//        val item4 = MonthChartItem(
//            "Month Name",
//            3, "yo make this a chart"
//        )

        monthChartItems.add(item1)
        monthChartItems.add(item2)
        monthChartItems.add(item3)
        monthChartItems.add(item4)
        monthChartItems.add(item5)
        monthChartItems.add(item6)

        //TODO: set scroll position to last item in list, so user swipes to the right to see previous month charts
//        monthChartItems.add(item5)


        //pass array list to displayItems to pass through Adapter
        displayItems = monthChartItems
    }

    private fun setSpendingLabels(string: String) {
        totalSpent.text = (string)
    }


    private fun createPieChart(pieChart: PieChart?) {
        // Set the percentage of language used
        pieChart?.clearChart()

        personalPercentage.text = mT?.getCategoryPercents(5).toString();
        financialPercentage.text = mT?.getCategoryPercents(8).toString();
        rentPercentage.text = mT?.getCategoryPercents(1).toString();
        householdPercentage.text = mT?.getCategoryPercents(4).toString();
        utilitiesPercentage.text = mT?.getCategoryPercents(2).toString();
        medicalPercentage.text = mT?.getCategoryPercents(6).toString();
        uncategorizedPercentage.text = mT?.getCategoryPercents(0).toString();

//        personalPercentage.text = mTrans?.getCategoryPercents(5).toString();
//        savingsPercentage.text = mTrans?.getCategoryPercents(8).toString();
//        rentPercentage.text = mTrans?.getCategoryPercents(1).toString();
//        householdPercentage.text = mTrans?.getCategoryPercents(4).toString();
//        utilitiesPercentage.text = mTrans?.getCategoryPercents(2).toString();
//        medicalPercentage.text = mTrans?.getCategoryPercents(6).toString();
//        uncategorizedPercentage.text = mTrans?.getCategoryPercents(0).toString();

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
                "Savings", financialPercentage.text.toString().toInt().toFloat(),
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
        pieChart?.startAnimation();

        // click listener for view transactions
        viewTransactions.setOnClickListener { findNavController().navigate(R.id.transactionsFragment) }

        addTransaction.setOnClickListener {
            val intent = Intent(context, AddTransactionActivity::class.java)
            intent.putExtra("popuptitle", "Add Transaction")
            intent.putExtra("popuptext", "Transaction Amount")
            intent.putExtra("popupbtn", "+ADD")
            // intent.putExtra("darkstatusbar", false)
            startActivity(intent)
        }
        //To use the popup window, just pass the values for the Title, Text, Button text and Status Bar appearance.


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

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view
        var chart = viewHolder.itemView.chart_view

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.data = item.barData // this forms the actual BarChart


        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setPinchZoom(false);
        chart.isClickable = false

        val xAxis = chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7

        val leftAxis = chart.axisLeft

        leftAxis.setLabelCount(8, false)

        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val rightAxis = chart.axisRight
        rightAxis.setDrawGridLines(false)

        rightAxis.setLabelCount(8, false)

        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f


        viewHolder.itemView.month_chart_name.text = item.month_name

    }

    override fun getLayout(): Int {
        return R.layout.month_chart_item
    }

}

data class MonthChartItem(
    var month_name: String,
    var id: Int,
    var barData: BarData
)
