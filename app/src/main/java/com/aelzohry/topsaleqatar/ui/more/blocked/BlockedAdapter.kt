package com.aelzohry.topsaleqatar.ui.more.blocked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.User

class BlockedAdapter(var users: ArrayList<User>, val clickListener: (User) -> Unit): RecyclerView.Adapter<BlockedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_blocked, parent, false)
        return BlockedViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: BlockedViewHolder, position: Int) {
        val user = users[position]
        holder.fillFrom(user)
        holder.unblockButton.setOnClickListener {
            clickListener(user)
        }
    }
}