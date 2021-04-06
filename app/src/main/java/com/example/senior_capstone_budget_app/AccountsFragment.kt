package com.example.senior_capstone_budget_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.account_item.view.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_accounts.view.*
import com.example.senior_capstone_budget_app.data.paypalAPI.BaseTransactionAPIClass
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class AccountsFragment : Fragment() {
    //______________Variables For Recycler View____________________
    private val accountItemAdapter = GroupAdapter<GroupieViewHolder>()
    private var accountBalance = 0.0
//    private var paypalAPI : PayPalTransactionAPI? = null

    //here we are adding items to the recycler view using the adapter we created to use images as buttons in a list
    private var displayItems: ArrayList<AccountItem> = ArrayList()
        set(value) {
            accountItemAdapter.clear()

            for (sectionItem: AccountItem in value) {
                val account = AccountAdapter(sectionItem)
                accountItemAdapter.add(account)
            }
            field = value
        }
    //_____________________________________________________________
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //view has been been instantiated yet
        //assign values here

//        paypalAPI = PayPalTransactionAPI()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //now you have access to the view to make changes
        accountsRecyclerView.apply {
            accountsRecyclerView.layoutManager = LinearLayoutManager(context)
            accountsRecyclerView.adapter = accountItemAdapter
        }
        //put functional code here for function calls, etc.
        getAccountItems()

        accountItemAdapter.setOnItemClickListener { item, _ ->

            accountItemAction(item)
//            when ((item as accountAdapter).itemID) {
//                0 -> {
//                    item1Action()
//                }
//                1 -> {
//                    item2Action()
//                }
//            }
        }
    }

    fun displayBalance(){

        val balanceObservable: Single<String> = Single.just(BaseTransactionAPIClass.payPalAPI.findBalance())
        balanceObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { accountBalance = it.toDouble() }
            .doOnError { print(it) }

    }

    private fun getAccountItems() {
        displayBalance()
        //create home menu items
        val accountItems = ArrayList<AccountItem>()
        val item1 = AccountItem(
           "ACCOUNT NAME",
           0, accountBalance
        )
        val item2 = AccountItem(
            "ACCOUNT NAME",
            1, 3000.00
        )


        //add home menu items to an array list
//        accountItems.add(item1)
        accountItems.add(item2)

        //pass array list to displayItems to pass through Adapter
        displayItems = accountItems
    }

    private fun accountItemAction(item: com.xwray.groupie.Item<com.xwray.groupie.GroupieViewHolder>) {
        //handle item click action

    }

}

class AccountAdapter(private val item: AccountItem) : Item() {
    private var itemID = item.id
    private var itemBalance = item.balance.toString()
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //this this a function to add item properties to the recycler view
        viewHolder.itemView.accountName.text = item.title
        viewHolder.itemView.currentBalance.text = itemBalance
    }

    override fun getLayout(): Int {
        return R.layout.account_item
    }

}

data class AccountItem(var title: String, var id: Int, var balance : Double)

