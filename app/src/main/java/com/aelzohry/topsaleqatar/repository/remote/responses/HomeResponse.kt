package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.TopBanner

data class HomeResponse(
    val success: Boolean,
    val message: String,
    val data: HomeResponseData?
)

data class HomeResponseData(
    val ads: ArrayList<Ad>,
    val banners: ArrayList<TopBanner>
)