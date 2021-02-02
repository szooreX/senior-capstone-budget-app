package com.example.senior_capstone_budget_app

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_goals.*
import kotlinx.android.synthetic.main.goal_item.view.*

class OptionsFragment : Fragment() {

    //______________Variables For Recycler View____________________
    private val goalItemAdapter = GroupAdapter<GroupieViewHolder>()

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<GoalItem> = ArrayList()
        set(value) {
            goalItemAdapter.clear()

            for (sectionItem: GoalItem in value) {
                val imageButton = GoalAdapter(sectionItem)
                goalItemAdapter.add(imageButton)
            }
            field = value
        }
    //_____________________________________________________________

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //items to add with the view, view does not exist at this point

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view is now created and can be accessed
        super.onViewCreated(view, savedInstanceState)
        //here we will add the recycler view items
        goalsRecyclerView.apply {
            goalsRecyclerView.layoutManager = LinearLayoutManager(context)
            goalsRecyclerView.adapter = goalItemAdapter
        }
        //put functional code here for function calls, etc.
        getGoalItems()

        goalItemAdapter.setOnItemClickListener { item, _ ->

            goalItemAction(item)
//            when ((item as GoalAdapter).itemID) {
//                0 -> {
//                    item1Action()
//                }
//                1 -> {
//                    item2Action()
//                }
//            }
        }
    }

    private fun getGoalItems() {
        val image1: Drawable? =
            context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_button_background, null) }
        val image2: Drawable? =
            context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_button_background, null) }
        val image3: Drawable? =
            context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_button_background, null) }
        val image4: Drawable? =
            context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_button_background, null) }
        val image5: Drawable? =
            context?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_button_background, null) }
        //create home menu items
        val goalItems = ArrayList<GoalItem>()
        val item1 = GoalItem(
            "Save 500 dollars by March 30th, 2021",
            0, image1
        )
        val item2 = GoalItem(
            "Buy a House",
            1,
            image2
        )
        val item3 = GoalItem(
            "Pay off debt",
            2,
            image3
        )
        val item4 = GoalItem(
            "Get a new Credit Card",
            3,
            image4
        )
        val item5 = GoalItem(
            "Earn 50,000/yr from my personal business",
            4,
            image5
        )

        //add home menu items to an array list
        goalItems.add(item1)
        goalItems.add(item2)
        goalItems.add(item3)
        goalItems.add(item4)
        goalItems.add(item5)

        //pass array list to displayItems to pass through Adapter
        displayItems = goalItems
    }

    private fun goalItemAction(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>) {
        //handle goal click action

    }

}

class GoalAdapter(private val item: GoalItem) : Item() {
    val itemID = item.id
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view, in this case I just want the image
        viewHolder.itemView.goalImageView.setImageDrawable(item.image)
        viewHolder.itemView.goalTitle.text = item.title
    }

    override fun getLayout(): Int {
        return R.layout.goal_item
    }

}


data class GoalItem(var title: String, var id: Int, var image: Drawable?)