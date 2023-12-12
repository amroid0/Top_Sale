package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.TChannel


data class NewChannelResponse(
    val success: Boolean,
    val message: String,
    val data: NewChannelData?
)

data class NewChannelData(
    val channel: TChannel
)