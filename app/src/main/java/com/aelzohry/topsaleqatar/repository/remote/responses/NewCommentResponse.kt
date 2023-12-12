package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Comment

data class NewCommentResponse(
    val success: Boolean,
    val message: String,
    val data: NewComment?
)

data class NewComment(
    val newCommentsCount: Int,
    val comment: Comment
)