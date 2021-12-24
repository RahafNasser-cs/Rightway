package com.rahafcs.co.rightway.utility.ui

import android.view.View
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.rahafcs.co.rightway.data.RegistrationStatus

@BindingAdapter("registrationStatus")
fun LottieAnimationView.bindRegistrationStatus(status: RegistrationStatus?) {
    when (status) {
        RegistrationStatus.SUCCESS -> {
            this.visibility = View.GONE
        }
        RegistrationStatus.FAILURE -> {
            this.visibility = View.GONE
        }
        RegistrationStatus.LOADING -> {
            this.visibility = View.VISIBLE
        } else -> {
            this.visibility = View.GONE
        }
    }
}
