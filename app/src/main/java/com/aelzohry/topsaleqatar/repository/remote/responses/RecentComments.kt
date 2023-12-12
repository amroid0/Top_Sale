package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Comment

data class RecentCommentsResponse(
    val success: Boolean,
    val message: String,
    val data: RecentComments?
)

data class RecentComments(
    val count: Int,
    val total: Int,
    val loadMore: Boolean,
    val comments: ArrayList<Comment>?
)