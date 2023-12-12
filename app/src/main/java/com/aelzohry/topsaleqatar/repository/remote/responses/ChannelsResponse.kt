package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.TChannel


data class ChannelsResponse(
    val success: Boolean,
    val message: String,
    val data: ChannelsData?
)

data class ChannelsData(
    val channels: ArrayList<TChannel>,
    val page: Int,
    val perPage: Int
)