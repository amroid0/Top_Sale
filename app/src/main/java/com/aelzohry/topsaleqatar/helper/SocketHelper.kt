package com.aelzohry.topsaleqatar.helper

import android.util.Log
import com.aelzohry.topsaleqatar.App
import com.aelzohry.topsaleqatar.model.TMessage
import com.aelzohry.topsaleqatar.model.TNewMessage
import com.aelzohry.topsaleqatar.model.TNewNotification
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import org.json.JSONObject
import java.net.URISyntaxException

object SocketHelper {
    private const val TAG = "test_SocketHelper"
    private var socket: Socket? = null

    fun connect() {
        object : Thread() {
            override fun run() {
                try {
                    if (socket == null) {
                        val options = IO.Options()
                        options.port = Constants.SOCKET_PORT
                        socket = IO.socket(Constants.SOCKET_URL, options)
//                        socket = IO.socket(Constants.SOCKET_URL)
                    }

                    if (socket?.connected() == false) {
                        listen()
                        socket?.connect()
                    }
                } catch(e: URISyntaxException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    fun listen() {
        socket?.off()
        listenToNewMessages()
        listenToNewNotifications()
        socket?.on(Socket.EVENT_CONNECT) {
            Log.i(TAG, "connected")
        }
        socket?.on(Socket.EVENT_DISCONNECT) {
            Log.i(TAG, "disconnected")
        }
        socket?.on(Socket.EVENT_ERROR) {
            Log.i(TAG, "error")
        }
        socket?.on(Socket.EVENT_CONNECT_ERROR) {
            Log.i(TAG, "connect_error")
        }
        socket?.on(Socket.EVENT_RECONNECT_ERROR) {
            Log.i(TAG, "reconnect_error")
        }
        socket?.on(Socket.EVENT_RECONNECTING) {
            Log.i(TAG, "reconnecting")
        }
        socket?.on(Socket.EVENT_RECONNECT) {
            Log.i(TAG, "reconnected")
        }
    }

    fun disconnect() {
        socket?.off()
        socket?.disconnect()
    }

    private fun listenToNewMessages() {
        val userId = Helper.userId ?: return
        val event = "user_${userId}_new_message"
        Log.i(TAG, "event: $event")
        socket?.on(event) {
            Log.i(TAG, "onEvent: $event")
            val first = it.firstOrNull()?.toString() ?: return@on
            Log.i(TAG, first)
            val gson = Gson()
            val response = gson.fromJson(first, TNewMessage::class.java)
            response?.newMessage?.let { message ->
                NotificationCenter.postNotification(App.context, NotificationType.NewMessageReceived, hashMapOf("new_message" to message))
            }
        }
    }

    private fun listenToNewNotifications() {
        val userId = Helper.userId ?: return
        val userEvent = "user_${userId}_new_notification"
        Log.i(TAG, "userEvent: $userEvent")
        socket?.on(userEvent) {
            Log.i(TAG, "onEvent: $userEvent")
            val first = it.firstOrNull()?.toString() ?: return@on
            Log.i(TAG, first)
            val gson = Gson()
            val response = gson.fromJson(first, TNewNotification::class.java)
            response?.newNotification?.let { not ->
                NotificationCenter.postNotification(App.context, NotificationType.NewNotificationReceived, hashMapOf("new_notification" to not))
            }
        }

        val generalEvent = "general_notifications"
        Log.i(TAG, "generalEvent: $generalEvent")
        socket?.on(generalEvent) {
            Log.i(TAG, "onEvent: $generalEvent")
            val first = it.firstOrNull()?.toString() ?: return@on
            Log.i(TAG, first)
            val gson = Gson()
            val response = gson.fromJson(first, TNewNotification::class.java)
            response?.newNotification?.let { not ->
                NotificationCenter.postNotification(App.context, NotificationType.NewNotificationReceived, hashMapOf("new_notification" to not))
            }
        }
    }

    fun sendNewTextMessage(data: Map<String, Any>) {
        val jsonData = JSONObject(data)

//        socket?.on("connect") {
//            Log.e("test_socket", "connect" + it.toString())
            socket?.emit("new_message", arrayOf(jsonData)) {
                val first = it.firstOrNull()?.toString() ?: return@emit
                val gson = Gson()
                val newMessage = gson.fromJson(first, TMessage::class.java)
                newMessage?.let { message ->
                    NotificationCenter.postNotification(
                        App.context,
                        NotificationType.NewMessageReceived,
                        hashMapOf("new_message" to message)
                    )
                }
            }
//        }


    }

    fun sendNewSeenMessage(messageId: String, channelId: String) {
        val data = mapOf(
            "message_id" to messageId,
            "channel_id" to channelId
        )
        val jsonData = JSONObject(data)
        socket?.emit("new_seen", arrayOf(jsonData)) {

        }
    }

}