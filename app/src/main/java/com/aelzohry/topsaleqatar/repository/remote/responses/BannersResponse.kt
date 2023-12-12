package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Banner

data class BannersResponse(
    val success: Boolean,
    val message: String,
    val data: BannersData?
)

data class BannersData(
    val banners: ArrayList<Banner>
)