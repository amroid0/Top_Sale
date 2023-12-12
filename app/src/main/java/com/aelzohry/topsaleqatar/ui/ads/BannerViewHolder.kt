package com.aelzohry.topsaleqatar.ui.ads

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Banner
import com.squareup.picasso.Picasso

class BannerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    fun fillFrom(banner: Banner) {
        Picasso.get().load(banner.photoUrl).fit().into(imageView)
    }
}