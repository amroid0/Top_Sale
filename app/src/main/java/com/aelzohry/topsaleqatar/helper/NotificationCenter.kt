package com.aelzohry.topsaleqatar.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Parcelable
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.Serializable


object NotificationCenter {
    fun addObserver(
        context: Context,
        notification: NotificationType,
        responseHandler: BroadcastReceiver?
    ) {
        LocalBroadcastManager.getInstance(context).registerReceiver(responseHandler!!, IntentFilter(notification.name))
    }

    fun removeObserver(context: Context, responseHandler: BroadcastReceiver?) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(responseHandler!!)
    }

    fun postNotification(
        context: Context,
        notification: NotificationType,
        params: HashMap<String, Parcelable>?
    ) {
        val intent = Intent(notification.name)
        // insert parameters if needed
        params?.let {
            for ((key, value) in it) {
                intent.putExtra(key, value)
            }
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}

enum class NotificationType {
    NewMessageReceived,
    NewNotificationReceived
}