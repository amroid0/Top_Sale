package com.aelzohry.topsaleqatar.ui.more.followings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.utils.extenions.setVisible

class FollowingsAdapter(
    var users: ArrayList<User>,
    val clickListener: (User) -> Unit,
    val tapHandler: (user: User) -> Unit
) : RecyclerView.Adapter<FollowingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_following, parent, false)
        return FollowingViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val user = users[position]
        holder.fillFrom(user)
        holder.itemView.setOnClickListener { tapHandler(user) }
        holder.unfollowButton.setVisible(user.isFollowing)
        holder.unfollowButton.setOnClickListener {
            clickListener(user)
        }
    }
}