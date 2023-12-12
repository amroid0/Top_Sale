package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad

data class RepublishAdResponse(
    val success: Boolean,
    val message: String,
    val data: RepublishAd?
)

data class RepublishAd(
    val ad: Ad
)