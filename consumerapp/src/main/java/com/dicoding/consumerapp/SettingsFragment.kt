package com.dicoding.consumerapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var notification: String

    private lateinit var switchPreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        init()
        setSummaries()
    }

    private fun init() {
        notification = resources.getString(R.string.reminder)
        switchPreference = findPreference<SwitchPreference>(notification) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == notification) {
            switchPreference.isChecked = sharedPreferences.getBoolean(notification, false)
            val alarmReceiver = AlarmReceiver()
            if (switchPreference.isChecked){
                activity?.let { alarmReceiver.setRepeatingAlarm(it) }
                switchPreference.isChecked = true
            }
            else{
                Toast.makeText(activity,"mati", Toast.LENGTH_SHORT).show()
                activity?.let { alarmReceiver.cancelAlarm(it) }
                switchPreference.isChecked = false
            }

        }
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        switchPreference.isChecked = sh.getBoolean(notification, false)
    }

}