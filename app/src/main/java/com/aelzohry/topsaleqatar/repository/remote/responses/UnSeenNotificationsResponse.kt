package com.aelzohry.topsaleqatar.repository.remote.responses

data class UnSeenNotificationsResponse(
    val success: Boolean,
    val message: String,
    val data: UnSeenNotificationsResponseData?
)

data class UnSeenNotificationsResponseData(
    val NumberOfNotificationsNotSeen: Int = 0,
    val NumberOfMessagesNotSeen: Int = 0
)