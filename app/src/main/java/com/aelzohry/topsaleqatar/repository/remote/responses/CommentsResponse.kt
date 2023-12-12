package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Comment

data class CommentsResponse(
    val success: Boolean,
    val message: String,
    val data: CommentsData?
)

data class CommentsData(
    val comments: ArrayList<Comment>,
    val page: Int,
    val perPage: Int
)