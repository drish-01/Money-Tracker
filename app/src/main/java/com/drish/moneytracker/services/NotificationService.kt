package com.drish.moneytracker.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle incoming FCM messages
        remoteMessage.notification?.let { notification ->
            // TODO: Show local notification
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send token to server for push notifications
    }
}
