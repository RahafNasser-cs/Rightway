package com.rahafcs.co.rightway.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.ViewPagerFragment

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "com.rahafcs.co.rightway"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    // generate notification
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, ViewPagerFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_app)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        Log.e("MyFirebaseMessagingService", "generateNotification: ")
        notificationManager.notify(0, builder.build())
    }

    // create custom layout
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.rahafcs.co.rightway", R.layout.notification)
        remoteViews.setTextViewText(R.id.title_textview, title)
        remoteViews.setTextViewText(R.id.message_textview, message)
        remoteViews.setImageViewResource(R.id.logo_img, R.drawable.logo_app)
        return remoteViews
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null)
            generateNotification(
                remoteMessage.notification?.title!!,
                remoteMessage.notification?.body!!
            )
        Log.e("TAG", "onMessageReceived: message resecedv")
    }
}
