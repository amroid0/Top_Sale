package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.User

data class EditProfileResponse(
    val success: Boolean,
    val message: String,
    val data: EditProfile?
)

data class EditProfile(
    val user: User
)