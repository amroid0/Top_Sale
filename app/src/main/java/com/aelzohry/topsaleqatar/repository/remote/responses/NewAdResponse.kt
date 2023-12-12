package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad

data class NewAdResponse(
    val success: Boolean,
    val message: String,
    val data: NewAd?
)

data class NewAd(
    val ad: Ad
)