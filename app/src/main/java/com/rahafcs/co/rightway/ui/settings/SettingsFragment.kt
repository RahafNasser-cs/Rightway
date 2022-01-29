package com.rahafcs.co.rightway.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.utility.Constant.LANGUAGES
import com.rahafcs.co.rightway.utility.Constant.PROFILE
import com.rahafcs.co.rightway.utility.toast

class SettingsFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val TAG = "SettingsFragment"
    private val args: SettingsFragmentArgs by navArgs()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val languagePref = findPreference<Preference>(LANGUAGES)
        languagePref?.summary =
            sharedPreferences.getString(LANGUAGES, "")
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
    }

    private fun onBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference) {
            findPreference<Preference>(LANGUAGES) -> {
                Log.e(TAG, "onPreferenceTreeClick: key ${preference.key}")
                Log.e(TAG, "onPreferenceTreeClick: key ${preference.key}")
                changeLanguage()
                true
            }
            findPreference<Preference>("darkMode") -> {
                darkMode()
                true
            }
            findPreference<Preference>(PROFILE) -> {
                if (args.userType.equals(requireContext().getString(R.string.trainer), true)) {
                    goToCoachInfoSettings()
                } else {
                    goToTraineeInfoSettings()
                }
                true
            }
            else -> {
                false
            }
        }
    }

    // Go to trainee settings.
    private fun goToTraineeInfoSettings() =
        findNavController().navigate(R.id.action_settingsFragment_to_userInfoSettingsFragment2)

    // Go to coach settings.
    private fun goToCoachInfoSettings() =
        findNavController().navigate(R.id.action_settingsFragment_to_coachInfoSettingsFragment)

    private fun changeLanguage() {
        requireContext().toast("changeLanguage")
    }

    private fun darkMode() {
        requireContext().toast("darkMode")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.e(TAG, "onSharedPreferenceChanged: key $key")
        when (key) {
            LANGUAGES -> {
                val languagePref = findPreference<Preference>(LANGUAGES)
                languagePref?.summary =
                    sharedPreferences?.getString(key, "")
                changeLanguage()
            }
            "darkMode" -> {
                darkMode()
            }
            PROFILE -> {
                if (args.userType.equals(requireContext().getString(R.string.trainer), true)) {
                    goToCoachInfoSettings()
                } else {
                    goToTraineeInfoSettings()
                }
            }
            else -> {
            }
        }
    }
}
