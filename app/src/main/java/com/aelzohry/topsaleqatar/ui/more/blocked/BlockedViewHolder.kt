package com.aelzohry.topsaleqatar.ui.more.blocked

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.User
import com.squareup.picasso.Picasso

class BlockedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val usernameTextView = itemView.findViewById<TextView>(R.id.usernameTextView)
    val mobileTextView = itemView.findViewById<TextView>(R.id.mobileTextView)
    val avatarImageView = itemView.findViewById<ImageView>(R.id.avatarImageView)
    val unblockButton = itemView.findViewById<Button>(R.id.unblockButton)

    private lateinit var user: User

    fun fillFrom(user: User) {
        this.user = user
        usernameTextView.text = user._name
        mobileTextView.text = user.mobile
        Picasso.get().load(user.avatarUrl).placeholder(R.drawable.avatar).into(avatarImageView)
    }
}