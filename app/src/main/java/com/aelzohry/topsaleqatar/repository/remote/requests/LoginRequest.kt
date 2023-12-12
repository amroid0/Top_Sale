package com.aelzohry.topsaleqatar.repository.remote.requests

class LoginRequest(
    val name: String,
    val mobile: String,
    val code: String?,
    val pushToken: String?
)