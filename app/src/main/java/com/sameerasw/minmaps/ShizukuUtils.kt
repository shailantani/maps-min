package com.sameerasw.minmaps

import android.content.pm.PackageManager
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import moe.shizuku.server.IShizukuService
import rikka.shizuku.Shizuku

object ShizukuUtils {

    private var binder: IBinder? = null

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        binder = Shizuku.getBinder()
        Log.d("ShizukuUtils", "Binder received!")
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        binder = null
        Log.d("ShizukuUtils", "Binder dead!")
    }

    private val isBinderAlive: Boolean
        get() = binder?.isBinderAlive == true

    /**
     * This should be called once, e.g., in Application#onCreate.
     */
    fun initialize() {
        Log.d("ShizukuUtils", "Initializing Shizuku...")
        Shizuku.addBinderReceivedListener(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
    }

    private fun hasPermission(): Boolean {
        if (!isBinderAlive) {
            Log.d("ShizukuUtils", "Binder not alive!")
            return false
        }
        return try {
            val permission = Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            Log.d("ShizukuUtils", "Shizuku permission check: $permission")
            permission
        } catch (e: Exception) {
            Log.e("ShizukuUtils", "Error checking permission", e)
            false
        }
    }

    fun runCommand(command: String) {
        Log.d("ShizukuUtils", "Attempting to run command: $command")
        Log.d("ShizukuUtils", "Binder alive: $isBinderAlive, Has permission: ${hasPermission()}")

        if (hasPermission() && isBinderAlive) {
            val service = IShizukuService.Stub.asInterface(binder)
            try {
                Log.d("ShizukuUtils", "Creating process for command...")
                val process = service.newProcess(arrayOf("sh", "-c", command), null, "/")
                process?.waitFor()
                Log.d("ShizukuUtils", "Command executed successfully!")
            } catch (e: RemoteException) {
                Log.e("ShizukuUtils", "Error executing command", e)
            }
        } else {
            Log.d("ShizukuUtils", "Cannot execute command - permission denied or binder dead")
        }
    }
}