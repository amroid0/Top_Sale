package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Not

data class NotificationsResponse(
    val success: Boolean,
    val message: String,
    val data: NotificationsData?
)

data class NotificationsData(
    val notifications: ArrayList<Not>,
    val page: Int,
    val perPage: Int
)