package com.rahafcs.co.rightway.data

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val subscriptionStatus: SubscriptionStatus = SubscriptionStatus.NONE,
    val weight: String = "",
    val height: String = "",
    val gender: String = "",
    val activity: String = ""
)
