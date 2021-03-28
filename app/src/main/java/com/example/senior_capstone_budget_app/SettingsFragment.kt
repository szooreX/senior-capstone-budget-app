package com.example.senior_capstone_budget_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference?>("logoutBtn")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                var intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(activity, "You Have Successfully Logged Out", Toast.LENGTH_SHORT).show()
                true
            }
    }
}