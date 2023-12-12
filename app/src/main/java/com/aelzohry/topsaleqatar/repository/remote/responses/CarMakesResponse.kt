package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.CarMake

data class CarMakesResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<CarMake>?
)