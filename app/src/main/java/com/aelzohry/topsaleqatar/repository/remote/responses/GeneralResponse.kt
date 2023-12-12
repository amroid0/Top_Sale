package com.aelzohry.topsaleqatar.repository.remote.responses

/*
{
    "success": false,
    "message": "User name or password wrong!"
}
 */
data class GeneralResponse(
    val success: Boolean,
    val message: String
)