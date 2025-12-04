package com.sameerasw.minmaps

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("NotificationListener", "Notification posted: ${sbn.packageName}, channel: ${sbn.notification.channelId}")
        if (sbn.packageName == "com.google.android.apps.maps") {
            Log.d("NotificationListener", "Maps notification detected, channel: ${sbn.notification.channelId}")
            if (sbn.notification.channelId == "NAVIGATION_NOTIFICATION") {
                Log.d("NotificationListener", "Navigation notification detected!")
                MapsState.hasNavigationNotification = true
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationListener", "Notification removed: ${sbn.packageName}")
        if (sbn.packageName == "com.google.android.apps.maps" && sbn.notification.channelId == "NAVIGATION_NOTIFICATION") {
            Log.d("NotificationListener", "Navigation notification removed!")
            MapsState.hasNavigationNotification = false
        }
    }
}