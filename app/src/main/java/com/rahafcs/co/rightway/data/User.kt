package com.rahafcs.co.rightway.data

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val subscriptionStatus: SubscriptionStatus = SubscriptionStatus.NONE,
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val gender: String = "",
    val activity: Int = 0
)
