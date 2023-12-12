package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.User

data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val data: ProfileData?
)

data class ProfileData(
    val user: User
)