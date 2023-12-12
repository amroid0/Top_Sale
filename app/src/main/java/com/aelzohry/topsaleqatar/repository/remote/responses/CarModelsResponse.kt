package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.CarModel

data class CarModelsResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<CarModel>?
)