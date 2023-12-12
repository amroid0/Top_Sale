package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad

data class AdsResponse(
    val success: Boolean,
    val message: String,
    val data: AdsDataResponse?
)

data class AdsDataResponse(
    val ads: ArrayList<Ad>,
    val page: Int,
    val perPage: Int
)