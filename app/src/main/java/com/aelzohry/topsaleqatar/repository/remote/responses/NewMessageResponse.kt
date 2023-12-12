package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.TMessage


data class NewMessageResponse(
    val success: Boolean,
    val message: String,
    val data: NewMessageData?
)

data class NewMessageData(
    val message: TMessage
)