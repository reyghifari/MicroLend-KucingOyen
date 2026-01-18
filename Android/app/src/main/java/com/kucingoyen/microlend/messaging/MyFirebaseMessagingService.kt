package com.kucingoyen.microlend.messaging

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kucingoyen.entity.model.DataPushNotification
import com.kucingoyen.microlend.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var bundlePushNotification: DataPushNotification

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        bundlePushNotification = DataPushNotification(
            pushNotification = true,
            title = remoteMessage.data["title"]?: "",
            image = remoteMessage.data["imageUrl"].toString(),
            body =  remoteMessage.data["body"] ?: "",
            transactionId = remoteMessage.data["transactionId"] ?: "",
            registrationStatus = remoteMessage.data["registrationStatus"] ?: "",
            goto = remoteMessage.data["goto"] ?: "",
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = System.currentTimeMillis().toInt()

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, "1"
        )


        val audio = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val channel = NotificationChannel(
            "1",
            "Push_Notification",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)


        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(notificationPending(applicationContext, notificationId))

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun notificationPending(context: Context, requestCode: Int): PendingIntent {
        val targetIntent = Intent(context, MainActivity::class.java)
        targetIntent.putExtra(
            FROM_PUSH_NOTIFICATION,
            bundlePushNotification.pushNotification
        )
        targetIntent.putExtra(
            TRANSACTION_ID,
            bundlePushNotification.transactionId
        )
        targetIntent.putExtra(
            GOTO,
            bundlePushNotification.goto
        )
        targetIntent.putExtra(
            REGISTRATION_STATUS,
            bundlePushNotification.registrationStatus
        )
        targetIntent.putExtra(DESCRIPTION_NOTIFICATION, bundlePushNotification.body)
        targetIntent.putExtra(IMAGE_NOTIFICATION, bundlePushNotification.image)
        targetIntent.putExtra(TITLE_NOTIFICATION, bundlePushNotification.title)

        return PendingIntent.getActivity(
            context,
            requestCode,
            targetIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val TITLE_NOTIFICATION = "MyFirebaseMessagingService.TITLE_NOTIFICATION"
        const val DESCRIPTION_NOTIFICATION = "MyFirebaseMessagingService.DESCRIPTION_NOTIFICATION"
        const val FROM_PUSH_NOTIFICATION = "MyFirebaseMessagingService.FROM_PUSH_NOTIFICATION"
        const val TRANSACTION_ID = "MyFirebaseMessagingService.TRANSACTION_ID"
        const val REGISTRATION_STATUS = "MyFirebaseMessagingService.REGISTRATION_STATUS"
        const val GOTO = "MyFirebaseMessagingService.GOTO"
        const val IMAGE_NOTIFICATION = "MyFirebaseMessagingService.IMAGE_NOTIFICATION"
        const val PENDING_INTENT_PUSH_NOTIFICATION = 1
    }
}
