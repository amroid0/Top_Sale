package com.aelzohry.topsaleqatar.ui.more.followers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.User

class FollowersAdapter(
    val context: Context,
    val clickListener: OnClickListener,

    var users: ArrayList<User>,
    val tapHandler: (user: User) -> Unit


) : RecyclerView.Adapter<FollowerViewHolder>() {

    private var IS_FOLLOWED_ARG: String? = "is_followed"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_follower, parent, false)
        return FollowerViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val user = users[position]
        holder.fillFrom(user)
        holder.itemView.setOnClickListener { tapHandler(user) }
//        holder.unfollowButton.setVisible(user.isFollowing)
        holder.unfollowButton.setOnClickListener {
            clickListener.onFollowClick(position, user)
        }


        if (user.isFollowing) {
            holder.unfollowButton.setBackgroundResource(R.drawable.custom_solid_btn)
            holder.unfollowButton.setText(context.getString(R.string.unfollow))
            holder.unfollowButton.setTextColor(context.getColor(R.color.white))
        } else {
            holder.unfollowButton.setBackgroundResource(R.drawable.custom_border_btn)
            holder.unfollowButton.setText(context.getString(R.string.follow))
            holder.unfollowButton.setTextColor(context.getColor(R.color.text_color_black))
        }
    }

    override fun onBindViewHolder(
        holder: FollowerViewHolder,
        position: Int,
        payloads: MutableList<Any>) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            for (key in bundle.keySet()) {
                if (key == IS_FOLLOWED_ARG) {
                    val isFollowed = bundle.getBoolean(IS_FOLLOWED_ARG)
                    if (isFollowed) {
                        holder.unfollowButton.setBackgroundResource(R.drawable.custom_solid_btn)
                        holder.unfollowButton.setText(context.getString(R.string.unfollow))
                        holder.unfollowButton.setTextColor(context.getColor(R.color.white))
                    } else {
                        holder.unfollowButton.setBackgroundResource(R.drawable.custom_border_btn)
                        holder.unfollowButton.setText(context.getString(R.string.follow))
                        holder.unfollowButton.setTextColor(context.getColor(R.color.text_color_black))
                    }
                }
            }
        }
    }


    fun toggleIsFollowed(position: Int) {
        val userInfo: User = users[position]
        userInfo.isFollowing = !userInfo.isFollowing
        users.set(position, userInfo)
        val bundle = Bundle()
        bundle.putBoolean(IS_FOLLOWED_ARG, userInfo.isFollowing)
        notifyItemChanged(position, bundle)
    }


    interface OnClickListener {
        fun onItemClick(item: User)
        fun onFollowClick(position: Int, item: User)
    }
}