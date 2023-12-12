package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.User

data class UsersListResponse(
    val success: Boolean,
    val message: String,
    val data: UsersListData?
)

data class UsersListData(
    val users: ArrayList<User>,
    val page: Int,
    val perPage: Int
)