package com.example.mycryptowallet.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mycryptowallet.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FireBaseService: FirebaseMessagingService() {
    val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
    val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME"


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (remoteMessage.notification != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(notificationChannel)
                }
                val notificationBuilder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                notificationBuilder.setAutoCancel(true)
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.body)
                    )
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setTicker(remoteMessage.notification!!.title)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(remoteMessage.notification!!.title)
                    .setContentText(remoteMessage.notification!!.body)
                notificationManager.notify(1, notificationBuilder.build())
            }
        }
    }

}