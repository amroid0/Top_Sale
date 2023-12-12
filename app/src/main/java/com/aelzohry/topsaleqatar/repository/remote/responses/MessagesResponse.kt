package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.TMessage


data class MessagesResponse(
    val success: Boolean,
    val message: String,
    val data: MessagesData?
)

data class MessagesData(
    val messages: ArrayList<TMessage>,
    val page: Int,
    val perPage: Int
)