package com.example.senior_capstone_budget_app

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.completed_task_item.view.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.goal_item_view_fragment.*

class GoalItemViewFragment : Fragment() {

private var dashboardActivity : DashboardActivity? = null
    companion object {
        fun newInstance() = GoalItemViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println()

        dashboardActivity = (activity as DashboardActivity)
        //var days: String = g!!.goals[].calculateDays().toString()
        //days_left_goal_item_view.text = "You have $days days left to complete you Goal on time!"
        return inflater.inflate(R.layout.goal_item_view_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timepickerbutton.setOnClickListener {
           // dashboardActivity?.getTimePicker()
            dashboardActivity?.launchTimePicker()
        }

    }


}