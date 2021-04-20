package com.example.senior_capstone_budget_app

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
//import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.example.senior_capstone_budget_app.goals.Goal
import com.example.senior_capstone_budget_app.goals.Tasks
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.completed_task_item.view.*
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.goal_item_view_fragment.*


var tasks: ArrayList<Tasks> = arrayListOf()
var taskIndex: Int = -1


class GoalItemViewFragment : Fragment() {

private var dashboardActivity : DashboardActivity? = null
    companion object {
        fun newInstance() = GoalItemViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        goal = g?.getGoal(index)
        tasks = goal!!.goalTasks

        dashboardActivity = (activity as DashboardActivity)

        return inflater.inflate(R.layout.goal_item_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setValues()
    }

    override fun onResume() {
        super.onResume()
        setValues()
    }

    fun setValues(){
        var days: String = goal?.calculateDays().toString()
        days_left_goal_item_view.text = "You Have $days Days Left To Complete Your Goal On Time!"
        goal_item_progress_bar.progress = goal?.percent!!.toFloat()
        goal_item_progress_bar.progressText = goal?.percent.toString()+"%"

        timepickerbutton.setOnClickListener {
            // dashboardActivity?.getTimePicker()
            dashboardActivity?.launchTimePicker()
        }

    }
}