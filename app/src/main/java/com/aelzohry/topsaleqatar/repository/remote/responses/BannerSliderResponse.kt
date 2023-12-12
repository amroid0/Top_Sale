package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.TopBanner

data class BannerSliderResponse(val success: Boolean, val message: String, val data: BannerSliderResponseData?)

data class BannerSliderResponseData(val banners: ArrayList<TopBanner>)