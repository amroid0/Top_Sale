package com.aelzohry.topsaleqatar.ui.ad_details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Comment

class RecentCommentsAdapter(
    var ad: Ad,
    var comments: ArrayList<Comment>,
    private val listener: CommentActionsListener
) : RecyclerView.Adapter<RecentCommentViewHolder>() {

    interface CommentActionsListener {
        fun delete(comment: Comment)
        fun edit(comment: Comment)
        fun block(comment: Comment)
        fun profile(comment: Comment)
        fun call(comment: Comment)
        fun chat(comment: Comment)
        fun email(comment: Comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_comment, parent, false)
        return RecentCommentViewHolder(view)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: RecentCommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.fillFrom(comment, ad.user?._id == comment.user?._id)
        holder.actionsButton.setOnClickListener { actions(comment, holder.actionsButton) }
    }

    private fun actions(comment: Comment, button: ImageButton) {
        val context = button.context
        val popupMenu = PopupMenu(context, button)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.comments_menu, menu)
        menu.findItem(R.id.edit).isVisible = comment.isCommentOwner
        menu.findItem(R.id.delete).isVisible = comment.isAdOwner || comment.isCommentOwner
        menu.findItem(R.id.email).isVisible = (comment.user?.email?.isNotEmpty()
            ?: false) && !(comment.isAdOwner || comment.isCommentOwner)
        menu.findItem(R.id.profile).isVisible = true
        menu.findItem(R.id.block).isVisible = true
        menu.findItem(R.id.call).isVisible = true
        menu.findItem(R.id.chat).isVisible = comment.isAdOwner

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> this.listener.delete(comment)
                R.id.edit -> this.listener.edit(comment)
                R.id.block -> this.listener.block(comment)
                R.id.profile -> this.listener.profile(comment)
                R.id.call -> this.listener.call(comment)
                R.id.chat -> this.listener.chat(comment)
                R.id.email -> this.listener.email(comment)
            }
            true
        }

        popupMenu.show()
    }
}