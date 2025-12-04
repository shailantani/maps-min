package com.sameerasw.minmaps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ScreenOffReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ScreenOffReceiver", "Broadcast received: ${intent.action}")
        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            Log.d("ScreenOffReceiver", "Screen off detected!")
            if (MapsState.hasNavigationNotification) {
                Log.d("ScreenOffReceiver", "Navigation notification is active, launching Maps minimal mode!")
                ShizukuUtils.runCommand("am start -n com.google.android.apps.maps/com.google.android.apps.gmm.features.minmode.MinModeActivity")
            } else {
                Log.d("ScreenOffReceiver", "No navigation notification found")
            }
        }
    }
}