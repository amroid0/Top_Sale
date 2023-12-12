package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad

data class RelatedAdResponse(
    val success: Boolean,
    val message: String,
    val data: RelatedAdDataResponse?
)

data class RelatedAdDataResponse(
    val ads: ArrayList<Ad>,
    val count: Int
)