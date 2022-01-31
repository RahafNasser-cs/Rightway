package com.rahafcs.co.rightway.ui.settings

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.utility.Constant.DARK_MODE
import com.rahafcs.co.rightway.utility.Constant.LANGUAGES
import com.rahafcs.co.rightway.utility.Constant.PROFILE
import com.rahafcs.co.rightway.utility.toast
import java.util.*

class SettingsFragment :
    PreferenceFragmentCompat() {

    private val args: SettingsFragmentArgs by navArgs()
    private lateinit var personalCategory: PreferenceCategory
    private lateinit var languagesSettingsCategory: PreferenceCategory
    private lateinit var profile: Preference
    private lateinit var languages: Preference
    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val languagePref = findPreference<Preference>(LANGUAGES)
        languagePref?.summary = getString(R.string.language_select)
        profile = findPreference(getString(R.string.profile_key))!!
        languages = findPreference(getString(R.string.languages_key))!!
        personalCategory = findPreference(getString(R.string.personal_key))!!
        languagesSettingsCategory =
            findPreference(getString(R.string.languages_settings_title_key))!!
    }

    override fun onResume() {
        super.onResume()
        languages.setDefaultValue(getString(R.string.language_code_select))
        onBackPressedDispatcher()
    }

    // Handel back press.
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

    // When preferences clicks.
    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference) {
            findPreference<Preference>(LANGUAGES) -> {
                preference.setOnPreferenceChangeListener { languagePref, languageCode ->
                    changeLanguage(languagePref, languageCode.toString())
                    true
                }
                true
            }
            findPreference<Preference>(DARK_MODE) -> {
                darkMode()
                true
            }
            findPreference<Preference>(PROFILE) -> {
                if (args.userType.equals(mContext?.getString(R.string.trainer), true)) {
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

    // To change languages. 
    private fun changeLanguage(languagePref: Preference, languageCode: String) {
        setLocale(languageCode)
        languagePref.summary = mContext?.getString(R.string.language_select)
    }

    // Go to trainee settings.
    private fun goToTraineeInfoSettings() =
        findNavController().navigate(R.id.action_settingsFragment_to_userInfoSettingsFragment2)

    // Go to coach settings.
    private fun goToCoachInfoSettings() =
        findNavController().navigate(R.id.action_settingsFragment_to_coachInfoSettingsFragment)

    private fun darkMode() =
        mContext?.toast(DARK_MODE)

    // Go to home page.
    private fun goToHomePage() =
        findNavController().navigateUp()

    // Set a new language.
    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resource = mContext?.resources
        val config = resource?.configuration
        config?.setLocale(locale)
        config?.setLayoutDirection(locale)
        resource?.updateConfiguration(config, resource.displayMetrics)
        config?.let {
            onConfigurationChanged(it)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        profile.title = mContext?.getString(R.string.profile_title)
        languages.title = mContext?.getString(R.string.languages_title)
        personalCategory.title = mContext?.getString(R.string.personal_title)
        languagesSettingsCategory.title =
            mContext?.getString(R.string.languages_settings_title)
        goToHomePage()
    }
}
