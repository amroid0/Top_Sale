package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Category

data class CategoriesResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<Category>?
)