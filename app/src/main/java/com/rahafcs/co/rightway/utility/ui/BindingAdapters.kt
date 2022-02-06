package com.rahafcs.co.rightway.utility.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.LoadingStatus
import com.rahafcs.co.rightway.ui.coach.CoachAdapter
import com.rahafcs.co.rightway.ui.state.CoachInfoUiState
import com.rahafcs.co.rightway.ui.workout.WorkoutsInfoUiState
import com.rahafcs.co.rightway.ui.workout.WorkoutsUiState
import com.rahafcs.co.rightway.ui.workout.WorkoutHorizontalAdapter
import com.rahafcs.co.rightway.ui.workout.WorkoutVerticalAdapter
import com.rahafcs.co.rightway.utility.Constant.ALL_EQUIPMENT
import com.rahafcs.co.rightway.utility.Constant.BARBELL
import com.rahafcs.co.rightway.utility.Constant.BODY_WEIGHT
import com.rahafcs.co.rightway.utility.Constant.CABLE
import com.rahafcs.co.rightway.utility.Constant.DUMBBELL
import com.rahafcs.co.rightway.utility.Constant.ERROR_MESSAGE
import com.rahafcs.co.rightway.utility.Constant.KETTLE_BELL
import com.rahafcs.co.rightway.utility.Constant.PRE_MESSAGE
import com.rahafcs.co.rightway.utility.Constant.PRE_SUBJECT
import com.rahafcs.co.rightway.utility.Constant.RESISTANCE_BAND
import pl.droidsonroids.gif.GifImageView

@BindingAdapter("registrationStatus")
fun LottieAnimationView.bindRegistrationStatus(status: LoadingStatus?) {
    when (status) {
        LoadingStatus.SUCCESS -> {
            this.visibility = View.GONE
        }
        LoadingStatus.ERROR -> {
            this.visibility = View.GONE
        }
        LoadingStatus.LOADING -> {
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
fun RecyclerView.bindHorizontalRecyclerView(data: List<WorkoutsInfoUiState>?) {
    val adapter = this.adapter as WorkoutHorizontalAdapter
    adapter.submitList(data)
}

@BindingAdapter("coachListItem")
fun RecyclerView.binCoachRecyclerView(data: List<CoachInfoUiState>?) {
    val adapter = this.adapter as CoachAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun ImageView.findUrl(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
        this.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.broken_image)
        }
    }
}

@BindingAdapter("imageUrl")
fun GifImageView.findUrl(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
        this.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.broken_image)
        }
    }
}

@BindingAdapter("imageUrl")
fun ImageView.findUrlGlide(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(this)
            .asGif()
            .load(imgUri).fitCenter()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.broken_image)
            .into(this)
    }
}

@BindingAdapter("getStringFromResources")
fun TextView.getStringFromResources(messageKey: String?) {
    when (messageKey) {
        ERROR_MESSAGE -> this.text = this.context.getString(R.string.error_message)
        ALL_EQUIPMENT -> this.text = this.context.getString(R.string.all_equipment)
        BARBELL -> this.text = this.context.getString(R.string.barbell)
        DUMBBELL -> this.text = this.context.getString(R.string.dumbbell)
        CABLE -> this.text = this.context.getString(R.string.cable)
        KETTLE_BELL -> this.text = this.context.getString(R.string.kettle_bell)
        BODY_WEIGHT -> this.text = this.context.getString(R.string.body_weight)
        RESISTANCE_BAND -> this.text = this.context.getString(R.string.resistance_band)
    }
}

@BindingAdapter(value = ["messageKey", "userInfo"], requireAll = false)
fun TextInputEditText.getStringFromResourcesEditText(
    messageKey: String?,
    userInfo: ArrayList<String>?,
) {
    when (messageKey) {
        PRE_SUBJECT -> this.setText(context.getString(R.string.pre_subject))
        PRE_MESSAGE -> this.setText(
            context.getString(
                R.string.pre_message,
                userInfo?.get(0),
                userInfo?.get(1),
                userInfo?.get(2),
                userInfo?.get(3),
                userInfo?.get(4),
                userInfo?.get(5)
            )
        )
    }
}
