package com.aelzohry.topsaleqatar.ui.ad_details

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.ago
import com.aelzohry.topsaleqatar.helper.toDate
import com.aelzohry.topsaleqatar.model.Comment
import com.squareup.picasso.Picasso

class RecentCommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val usernameTextView = itemView.findViewById<TextView>(R.id.usernameTextView)
    val mobileTextView = itemView.findViewById<TextView>(R.id.mobileTextView)
    val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
    val commentTextView = itemView.findViewById<TextView>(R.id.commentTextView)
    val avatarImageView = itemView.findViewById<ImageView>(R.id.avatarImageView)
    val actionsButton = itemView.findViewById<ImageButton>(R.id.actionsButton)

    private lateinit var comment: Comment

    @SuppressLint("SetTextI18n")
    fun fillFrom(comment: Comment, isAuthor: Boolean) {
        this.comment = comment
        val username = comment.user?._name ?: ""
        usernameTextView.text = username + if (isAuthor) " [${itemView.context.getString(R.string.author)}]" else ""
        dateTextView.text = comment.createdAt.toDate()?.ago() ?: ""
        mobileTextView.text = comment.user?.mobile
        commentTextView.text = comment.text
        Picasso.get().load(comment.user?.avatarUrl).placeholder(R.drawable.avatar).into(avatarImageView)
    }
}