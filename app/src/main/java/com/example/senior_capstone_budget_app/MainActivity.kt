package com.example.senior_capstone_budget_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.senior_capstone_budget_app.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.dashboardFragment)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabActions(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                tabActions(tab)
            }

        })


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun tabActions(tab: TabLayout.Tab) {
        when (tab.position) {
            0 -> {
                navController.navigate(R.id.pieChartFragment)
            }
            1 -> {
                navController.navigate(R.id.accountsFragment)
            }
            2 -> {
                navController.navigate(R.id.goalsFragment)
            }
        }
    }


}