package com.example.senior_capstone_budget_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.completed_task_item.view.*
import kotlinx.android.synthetic.main.fragment_completed_tasks.completed_tasks_recycler_view
import kotlinx.android.synthetic.main.fragment_completed_tasks.view.*
import kotlinx.android.synthetic.main.goal_item_view_fragment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedTasksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedTasksFragment : Fragment() {

    private var prompt : TextView? = null

    //______________Variables For Recycler View____________________
    private val completedTasksItemAdapter = GroupAdapter<GroupieViewHolder>()

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<CompletedTasksItem> = ArrayList()
        set(value) {
            completedTasksItemAdapter.clear()

            for (sectionItem: CompletedTasksItem in value) {
                val task = CompletedTasksItemAdapter(sectionItem)
                completedTasksItemAdapter.add(task)
            }
            field = value
        }

    //_____________________________________________________________
    companion object {
        fun newInstance() = GoalItemViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        completed_tasks_recycler_view.apply {
            completed_tasks_recycler_view.layoutManager = LinearLayoutManager(context)
            completed_tasks_recycler_view.adapter = completedTasksItemAdapter
        }
        //put functional code here for function calls, etc.
        getCompletedTasksItems()
        if (completedTasksItemAdapter.itemCount == 0) {
            prompt?.visibility = View.VISIBLE
        }

        completedTasksItemAdapter.setOnItemClickListener { item, _ ->

            completedTasksItemAction(item)

        }
    }

    private fun getCompletedTasksItems() {

        //create items
        val completedTasksItems = ArrayList<CompletedTasksItem>()
        val item1 = CompletedTasksItem(
            "Task 1",
            0
        )
        val item2 = CompletedTasksItem(
            "Task 2",
            1
        )
        val item3 = CompletedTasksItem(
            "Task 3",
            2
        )


        //add home menu items to an array list
        completedTasksItems.add(item1)
        completedTasksItems.add(item2)
        completedTasksItems.add(item3)


        //pass array list to displayItems to pass through Adapter
        displayItems = completedTasksItems
    }

    private fun completedTasksItemAction(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>) {
        //handle click action


    }
}

class CompletedTasksItemAdapter(private val item: CompletedTasksItem) : Item() {
    val itemID = item.id
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view, in this case I just want the image
        //viewHolder.itemView.goalImageView.setImageDrawable(item.image)
        viewHolder.itemView.completed_task_name.text = item.title
    }

    override fun getLayout(): Int {
        return R.layout.completed_task_item
    }

}

data class CompletedTasksItem(var title: String, var id: Int)