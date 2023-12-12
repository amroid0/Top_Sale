package com.aelzohry.topsaleqatar.repository.remote.responses

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val userId: String,
    val token: String,
    val didSavePushToken: Boolean
)