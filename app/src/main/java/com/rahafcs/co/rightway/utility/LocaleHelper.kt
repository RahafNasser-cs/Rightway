package com.rahafcs.co.rightway.utility

import android.content.Context
import android.content.res.Configuration
import java.util.*

// Help to Change app language.
class LocaleHelper {

    lateinit var configuration: Configuration
    lateinit var locale: Locale

    // Set a new language.
    fun setLocale(language: String, context: Context) {
        locale = if (language != "")
            Locale(language)
        else Locale.getDefault()

        Locale.setDefault(locale)
        val resource = context.resources
        val config = resource?.configuration
        config?.setLocale(locale)
        config?.setLayoutDirection(locale)
        resource?.updateConfiguration(config, resource.displayMetrics)
        config?.let {
            configuration = config
        }
    }
}
