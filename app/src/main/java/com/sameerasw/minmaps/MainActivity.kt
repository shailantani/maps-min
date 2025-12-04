package com.sameerasw.minmaps

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import rikka.shizuku.Shizuku

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a simple LinearLayout to display status
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        // Shizuku permission status
        val shizukuStatus = TextView(this).apply {
            text = "Shizuku Permission: ${if (hasShizukuPermission()) "✓ GRANTED" else "✗ NOT GRANTED"}"
            textSize = 16f
            setPadding(0, 8, 0, 8)
        }
        layout.addView(shizukuStatus)

        // Notification Listener permission status
        val notificationStatus = TextView(this).apply {
            text = "Notification Listener: ${if (isNotificationListenerEnabled()) "✓ ENABLED" else "✗ NOT ENABLED"}"
            textSize = 16f
            setPadding(0, 8, 0, 8)
        }
        layout.addView(notificationStatus)

        // Navigation Notification status
        val navNotifStatus = TextView(this).apply {
            text = "Has Navigation Notification: ${if (MapsState.hasNavigationNotification) "✓ YES" else "✗ NO"}"
            textSize = 16f
            setPadding(0, 8, 0, 8)
        }
        layout.addView(navNotifStatus)

        // Instructions
        val instructions = TextView(this).apply {
            text = """
                To enable Notification Listener:
                1. Go to Settings > Apps and notifications > Notification access
                2. Find "Min Maps" and enable it
                
                To enable Shizuku:
                1. Install Shizuku Manager from GitHub
                2. Grant API access to this app
            """.trimIndent()
            textSize = 14f
            setPadding(0, 16, 0, 8)
        }
        layout.addView(instructions)

        // Add button to open notification access settings
        val openSettingsBtn = TextView(this).apply {
            text = "→ Open Notification Access Settings"
            textSize = 14f
            setPadding(0, 16, 0, 8)
            setBackgroundColor(0xFF6200EE.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            isClickable = true
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        layout.addView(openSettingsBtn)

        setContentView(layout)
    }

    private fun hasShizukuPermission(): Boolean {
        return try {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (_: Exception) {
            false
        }
    }

    private fun isNotificationListenerEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        ) ?: return false

        val componentName = ComponentName(this, NotificationListener::class.java)
        return enabledServices.contains(componentName.flattenToString())
    }
}