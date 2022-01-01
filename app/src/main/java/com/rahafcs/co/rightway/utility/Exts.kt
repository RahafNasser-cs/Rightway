package com.rahafcs.co.rightway.utility

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.capitalizeFormatIfFirstLatterSmall(): String {
    val temp = this[0]
    return this.lowercase().replace('_', ' ').replace(temp, temp.uppercaseChar())
}

fun String.capitalizeFormatIfFirstLatterCapital(): String {
    val temp = this[0]
    return this.lowercase().replace('_', ' ').replace(temp.lowercaseChar(), temp)
}
