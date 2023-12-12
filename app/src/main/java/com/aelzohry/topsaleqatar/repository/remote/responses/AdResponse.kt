package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad

data class AdResponse(
    val success: Boolean,
    val message: String,
    val data: Ad?
)