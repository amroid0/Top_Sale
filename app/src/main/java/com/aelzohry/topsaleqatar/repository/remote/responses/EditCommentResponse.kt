package com.aelzohry.topsaleqatar.repository.remote.responses

import com.aelzohry.topsaleqatar.model.Comment

data class EditCommentResponse(
    val success: Boolean,
    val message: String,
    val data: EditComment?
)

data class EditComment(
    val comment: Comment
)