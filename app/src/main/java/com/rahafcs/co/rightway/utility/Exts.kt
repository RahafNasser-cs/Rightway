package com.rahafcs.co.rightway.utility

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.capitalizeFormat(): String {
    var temp = this[0]
    return this.lowercase().replace('_', ' ').replace(temp, temp.uppercaseChar())
}
