package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AnotherAd

data class AnotherAdsResponse(
    val success: Boolean,
    val message: String,
    val data: AnotherAdsDataResponse?
)

data class AnotherAdsDataResponse(
    val ads: ArrayList<AnotherAd>,
    val page: Int,
    val perPage: Int
)