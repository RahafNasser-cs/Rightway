package com.rahafcs.co.rightway.utility.ui

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.RegistrationStatus
import com.rahafcs.co.rightway.ui.WorkoutHorizontalAdapter
import com.rahafcs.co.rightway.ui.WorkoutVerticalAdapter
import com.rahafcs.co.rightway.ui.state.WorkoutsInfoUiState
import com.rahafcs.co.rightway.ui.state.WorkoutsUiState

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
fun RecyclerView.bindVerticalRecyclerView(data: List<WorkoutsUiState?>?) {
    data?.let {
        val adapter = this.adapter as WorkoutVerticalAdapter
        adapter.submitList(data)
    }
}

@BindingAdapter("horizontalListItem")
fun RecyclerView.bindHorizontalRecyclerView(data: List<WorkoutsInfoUiState?>?) {
    val adapter = this.adapter as WorkoutHorizontalAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun ImageView.findUrl(imgUrl: String?) {
    imgUrl?.let {
        var imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
        this.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.broken_image)
        }
    }
}
