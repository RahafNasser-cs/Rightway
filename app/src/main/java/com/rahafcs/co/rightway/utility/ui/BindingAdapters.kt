package com.rahafcs.co.rightway.utility.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.rahafcs.co.rightway.data.RegistrationStatus
import com.rahafcs.co.rightway.data.Workout
import com.rahafcs.co.rightway.ui.WorkoutHorizontalAdapter
import com.rahafcs.co.rightway.ui.WorkoutVerticalAdapter

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
        }
        else -> {
            this.visibility = View.GONE
        }
    }
}

@BindingAdapter("verticalListItem")
fun RecyclerView.bindVerticalRecyclerView(data: List<Workout?>?) {
    data?.let {
        val adapter = this.adapter as WorkoutVerticalAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("horizontalListItem")
fun RecyclerView.bindHorizontalRecyclerView(data: List<Workout?>?) {
    val adapter = this.adapter as WorkoutHorizontalAdapter
    adapter.submitList(data)
}
