package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Region

data class RegionsResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<Region>?
)