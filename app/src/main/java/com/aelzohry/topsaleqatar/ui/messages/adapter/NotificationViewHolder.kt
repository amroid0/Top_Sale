package com.aelzohry.topsaleqatar.ui.messages.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.ago
import com.aelzohry.topsaleqatar.helper.toDate
import com.aelzohry.topsaleqatar.ui.messages.NotWrapper
import com.squareup.picasso.Picasso

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val containerLayout = itemView.findViewById<View>(R.id.containerLayout)
    val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
    val bodyTextView = itemView.findViewById<TextView>(R.id.bodyTextView) // body or date
    val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    // imageView -> user i mage

    //adImageView -> adImageView
    val messageView = itemView.findViewById<View>(R.id.lastMessageView)
    val messageDateTextView = itemView.findViewById<TextView>(R.id.messageDataTextView)
    val messageUserTextView = itemView.findViewById<TextView>(R.id.messageUserTextView)
    val messageBodyTextView = itemView.findViewById<TextView>(R.id.messageBodyTextView)

    val adImageView = itemView.findViewById<ImageView>(R.id.adImageView)

    fun fillFrom(wrapper: NotWrapper) {
        clear()

        containerLayout.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                if (wrapper.seen) R.color.not_background else R.color.not_unseen_background
            )
        )
        dateTextView.text = wrapper.createdAt.toDate()?.ago()

        wrapper.not?.let {

            bodyTextView.text = it.body.localized
            val isAvatar = it.objectComment != null || it.objectUser != null

            val placeholderId = if (isAvatar) R.drawable.avatar else R.mipmap.logo
            Picasso.get().load(it.imageUrl).resize(150, 150).centerCrop().placeholder(placeholderId)
                .into(imageView)


            if (it.objectComment != null && it.objectAd != null) {
                Picasso.get().load(it.objectAd.imageUrl).resize(150, 150).centerCrop()
                    .placeholder(R.mipmap.logo).into(adImageView)
                adImageView.visibility = View.VISIBLE
            } else {
                adImageView.visibility = View.GONE
            }
        }

        wrapper.channel?.let {
            bodyTextView.text = it.partner?.mobile

            it.lastMessage?.let { message ->
                messageDateTextView.text = message.createdAt
                messageUserTextView.text = message.user?._name
                messageBodyTextView.text = message.body ?: message.type
                messageView.visibility = View.VISIBLE
            }

            it.ad?.let { ad ->
                Picasso.get().load(ad.imageUrl).resize(150, 150).centerCrop()
                    .placeholder(R.mipmap.logo).into(adImageView)
                adImageView.visibility = View.VISIBLE
            }

            it.partner?.let { partner ->
                Picasso.get().load(partner.avatarUrl).resize(150, 150).centerCrop()
                    .placeholder(R.drawable.avatar).into(imageView)
            }
        }

        if (wrapper.seen) {
            containerLayout.background = null
        } else {
            containerLayout.background =
                ContextCompat.getDrawable(
                    containerLayout.context,
                    R.drawable.bg_unseen_notification
                )
        }
    }

    fun clear() {
        messageView.visibility = View.GONE
        adImageView.visibility = View.GONE
        adImageView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.mipmap.logo))
        imageView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.avatar))
    }
}