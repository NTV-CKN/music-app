package com.infix.musicappv1.ui.settings

import android.app.UiModeManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.infix.musicappv1.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        initWhenAppOpenFirst()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEventPrefs()
    }

    private fun setupEventPrefs() {
        //ui mode
        val darkModePref = findPreference<SwitchPreferenceCompat>(KEY_DARK_MODE)
        darkModePref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                changeDarkMode(pref, newValue)
                true
            }

        //language
        val languagesPref = findPreference<ListPreference>(KEY_CHOOSE_LANGUAGE)
        languagesPref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                changeLanguage(pref, newValue)
                true
            }
    }

    private fun changeDarkMode(pref: Preference, newValue: Any) {
        val isNightMode = pref.sharedPreferences?.getBoolean(KEY_DARK_MODE, false) ?: false
        if (isNightMode == newValue) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiManager = requireContext().getSystemService(UiModeManager::class.java)
            val uiMode = if (newValue.toString().toBoolean())
                UiModeManager.MODE_NIGHT_YES
            else
                UiModeManager.MODE_NIGHT_NO
            uiManager.setApplicationNightMode(uiMode)
        } else {
            val uiMode = if (newValue.toString().toBoolean())
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(uiMode)
        }
    }

    private fun changeLanguage(pref: Preference, newValue: Any) {
        val oldLanguage = pref.sharedPreferences?.getString(KEY_CHOOSE_LANGUAGE, "en") ?: "en"
        if (newValue.toString() == oldLanguage) return
        val localeList = LocaleListCompat.forLanguageTags(newValue.toString())
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    private fun initWhenAppOpenFirst() {
        val prefs = preferenceManager.sharedPreferences
        val darkModePref = findPreference<SwitchPreferenceCompat>(KEY_DARK_MODE)
        val languagesPref = findPreference<ListPreference>(KEY_CHOOSE_LANGUAGE)

        //is app first visit, we guarantee UI sync with system
        if (prefs != null && !prefs.contains(KEY_DARK_MODE)) {
            val isSystemDark = (resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            darkModePref?.isChecked = isSystemDark
        }
        if (prefs != null && !prefs.contains(KEY_CHOOSE_LANGUAGE)) {
            val localeList = ConfigurationCompat.getLocales(resources.configuration)
            val tag = localeList.get(0)?.language ?: "en"
            languagesPref?.value = tag
        }
    }

    companion object {
        const val KEY_DARK_MODE = "dark_mode"
        const val KEY_CHOOSE_LANGUAGE = "choose_language"
    }
}