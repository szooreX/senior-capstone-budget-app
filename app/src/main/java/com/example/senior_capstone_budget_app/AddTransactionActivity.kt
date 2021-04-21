package com.example.senior_capstone_budget_app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import com.example.senior_capstone_budget_app.transaction.Transaction
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.transaction_item.*
import java.sql.Timestamp
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private var popupTitle = ""
    private var transactionAmount = ""
    private var transactionPayee = ""
    private var transactionDate = ""
    private var transactionCategory = ""
    private var popupButton = ""
    private var darkStatusBar = false
    private var cal: Calendar = Calendar.getInstance()
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_add_transaction)
        spinner = findViewById(R.id.category_spinner)

        // Fade animation for the background of Popup Window
        val alpha = 50 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()


        popup_window_title.text = "Add Transaction"
        transaction_amount.text = "Transaction Amount:"
        payment_to.text = "Payment To:"
        payment_date.text = "Date of Transaction:"
        payment_category.text = "Category:"
        add_button.text = "Add Transaction"
        transaction_description.text = "Transaction Details"

        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }

        cal = Calendar.getInstance()

        add_transaction_calendar.setOnDateChangeListener{view, year, month, dayOfMonth ->
            cal.set(year,month,dayOfMonth)
        }

        // Close the Popup Window when you press the button
        add_button.setOnClickListener {
            addTransaction()
            Toast.makeText(this, "Added Transaction $${transaction_amount_edit_text.text} To ${transaction_to_edit_text.text}", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
        close_popup_button.setOnClickListener {
            onBackPressed()
        }

        add_transaction_calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            hideKeyboard()

        }
    }

    private fun hideKeyboard() {
        //hide keyboard on tab selected
        try {
            val inputMethodManager: InputMethodManager = this.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken, 0
            )

        } catch (error: Exception) {
            //keyboard was not open
            error.printStackTrace()
            return
        }
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }

    private fun addTransaction(){
        transactionAmount = transaction_amount_edit_text.text.toString()
        transactionPayee = transaction_to_edit_text.text.toString()
        val details: String = transaction_description_edit_text.text.toString()
        transactionCategory = spinner.selectedItemPosition.toString()
        val transCategoryPos = transactionCategory.toInt()
        var transCategoryId = -1

        when(transCategoryPos){
            0 -> transCategoryId = 5
            1 -> transCategoryId = 8
            2 -> transCategoryId = 1
            3 -> transCategoryId = 4
            4 -> transCategoryId = 2
            5 -> transCategoryId = 6
            6 -> transCategoryId = 0
        }

        mT?.addTransaction(transactionAmount.toDouble(), transactionPayee, Timestamp(cal.timeInMillis), transCategoryId)
        mT?.saveTransactions(DashboardActivity().user)
    }

    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

//        // Fade animation for the Popup Window when you press the back button
//        popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
//            DecelerateInterpolator()
//        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }
}