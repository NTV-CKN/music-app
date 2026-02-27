package com.infix.musicappv1.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.infix.musicappv1.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}