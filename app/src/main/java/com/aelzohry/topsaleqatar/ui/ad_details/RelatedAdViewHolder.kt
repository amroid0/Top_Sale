package com.aelzohry.topsaleqatar.ui.ad_details

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Ad
import com.squareup.picasso.Picasso

class RelatedAdViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
    val thumbImageView = itemView.findViewById<ImageView>(R.id.thumbImageView)
    val fixedImageView = itemView.findViewById<ImageView>(R.id.fixedImageView)
//    val commentsCountTextView = itemView.findViewById<TextView>(R.id.commentsCountTextView)
//    val likesCountTextView = itemView.findViewById<TextView>(R.id.likesCountTextView)
    val priceTextView = itemView.findViewById<TextView>(R.id.priceTextView)

    private lateinit var ad: Ad

    fun fillFrom(ad: Ad) {
        this.ad = ad
        titleTextView.text = ad.title
        fixedImageView.visibility = if (ad.isFixed == true) View.VISIBLE else View.GONE
        priceTextView.text = ad.price?.toInt().toString()
//        commentsCountTextView.text = ad.commentsCount.toString()
//        likesCountTextView.text = ad.likesCount.toString()
        Picasso.get().load(ad.imageUrl).placeholder(R.mipmap.logo).into(thumbImageView)
    }
}