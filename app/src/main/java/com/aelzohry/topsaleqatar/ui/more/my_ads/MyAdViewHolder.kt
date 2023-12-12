package com.aelzohry.topsaleqatar.ui.more.my_ads

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AdStatus
import com.squareup.picasso.Picasso

class MyAdViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
//    private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
//    private val thumbImageView: ImageView = itemView.findViewById(R.id.thumbImageView)
//    private val fixedImageView: ImageView = itemView.findViewById(R.id.fixedImageView)
//    private val commentsCountTextView: TextView = itemView.findViewById(R.id.commentsCountTextView)
//    private val likesCountTextView: TextView = itemView.findViewById(R.id.likesCountTextView)
//    private val viewsCountTextView: TextView = itemView.findViewById(R.id.viewsCountTextView)
//    private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
//    val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
//    val editButton: ImageButton = itemView.findViewById(R.id.editButton)
//    val republishButton: Button = itemView.findViewById(R.id.republishButton)
//    val toggleActivationButton: Button = itemView.findViewById(R.id.toggleActivationButton)
//    private val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
//    private val rejectReasonTextView: TextView = itemView.findViewById(R.id.rejectReasonTextView)
//
//    private lateinit var ad: Ad

    fun fillFrom(ad: Ad) {
//        this.ad = ad
//        titleTextView.text = ad.title
//        detailsTextView.text = ad.details
//        fixedImageView.visibility = if (ad.isFixed) View.VISIBLE else View.GONE
//        priceTextView.text = ad.price.toInt().toString()
//        commentsCountTextView.text = ad.commentsCount.toString()
//        likesCountTextView.text = ad.likesCount.toString()
//        viewsCountTextView.text = ad.viewsCount.toString()
//        Picasso.get().load(ad.imageUrl).placeholder(R.mipmap.logo).into(thumbImageView)
//        republishButton.isEnabled = ad.canRepublish
//        republishButton.alpha = if (ad.canRepublish) 1F else 0.5F
//
//        toggleActivationButton.text = itemView.context.getString(if (ad.isActive == true) R.string.deactivate_ad else R.string.activate_ad)
//
//        statusTextView.text = ad.status.getTitle(itemView.context)
//        statusTextView.setTextColor(ContextCompat.getColor(itemView.context, if (ad.status == AdStatus.ACTIVE) R.color.statusActive else R.color.statusNonActive))
//
//        rejectReasonTextView.visibility = if (ad.status == AdStatus.REJECTED) View.VISIBLE else View.GONE
//        rejectReasonTextView.text = itemView.resources.getString(R.string.reject_note, ad.rejectReason?.localized ?: "")
    }
}