package com.example.senior_capstone_budget_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senior_capstone_budget_app.data.paypalAPI.PayPalTransactionAPI
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.account_item.view.*
import kotlinx.android.synthetic.main.fragment_accounts.*
import kotlinx.android.synthetic.main.fragment_accounts.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates


class AccountsFragment : Fragment() {
    //______________Variables For Recycler View____________________
    private val accountItemAdapter = GroupAdapter<GroupieViewHolder>()
    private var paypalAPI: PayPalTransactionAPI? = null


    companion object {
        fun newInstance() = GoalItemViewFragment()
    }

    private lateinit var viewModel: AccountsFragmentViewModel

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


    // Use the 'by viewModels()' Kotlin property delegate
    // from the activity-ktx artifact
    private val model: AccountsFragmentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //view has been been instantiated yet
        //assign values here
//thread overrider-----
//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
        //-----------

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountsFragmentViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launch {
            val params = TextViewCompat.getTextMetricsParams(test_text_view)
            val precomputedText = withContext(Dispatchers.Default) {

                // TODO: Use the ViewModel
                viewModel.launchDataLoad()
                PrecomputedTextCompat.create("Loading Accounts...", params)
            }
            TextViewCompat.setPrecomputedText(test_text_view, precomputedText)
        }
        //now you have access to the view to make changes
        accountsRecyclerView.apply {
            accountsRecyclerView.layoutManager = LinearLayoutManager(context)
            accountsRecyclerView.adapter = accountItemAdapter
        }
        //put functional code here for function calls, etc.


        // Create the observer which updates the UI.
        val nameObserver = Observer<String> { newBalance ->
            // Update the UI, in this case, a TextView.
            //test_text_view.text = newBalance
            test_text_view.visibility = View.INVISIBLE
            getAccountItems(newBalance)
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.liveData.observe(viewLifecycleOwner, nameObserver)
    }


    //prevent network on main thread error using view model below this class, AccountsFragmentViewModel

//    private fun displayBalance(){
//
//        val balanceObservable: Single<String> = Single.just(BaseTransactionAPIClass.payPalAPI.findBalance())
//        balanceObservable
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSuccess { accountBalance = it.toDouble() }
//            .doOnError { print(it) }
//
//    }

    fun getAccountItems(newBalance : String) {
        //create home menu items
        val accountItems = ArrayList<AccountItem>()
        val item1 = AccountItem(
            "ACCOUNT NAME",
            0, newBalance
        )
//        val item2 = AccountItem(
//            "ACCOUNT NAME",
//            1, accountBalance
//        )


        //add home menu items to an array list
        accountItems.add(item1)
//        accountItems.add(item2)

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

class AccountsFragmentViewModel : ViewModel() {

    val liveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    /**
     * Heavy operation that cannot be done in the Main Thread
     */
    fun launchDataLoad() {
        viewModelScope.launch {
            callPayPal()
            // Modify UI
        }
    }

    suspend fun callPayPal() = withContext(Dispatchers.Default) {
        // Heavy work

        var paypalAPI = PayPalTransactionAPI()

        //postValue - Posts a task to a main thread to set the given value.
        liveData.postValue(paypalAPI.findBalance().toString())



    }

}

data class AccountItem(var title: String, var id: Int, var balance: String) {

}

