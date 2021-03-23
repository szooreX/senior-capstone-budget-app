package com.example.senior_capstone_budget_app

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.senior_capstone_budget_app.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
//        setSupportActionBar(findViewById(R.id.toolbar))


        navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.pieChartFragment)


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabActions(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                tabActions(tab)
            }

        })

        settingsBtn.setOnClickListener { navController.navigate(R.id.settingsFragment) }

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