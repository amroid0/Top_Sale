package com.aelzohry.topsaleqatar.ui.ad_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Ad

class RelatedAdsAdapter(var ads: ArrayList<Ad>, val clickListener: (Ad) -> Unit): RecyclerView.Adapter<RelatedAdViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedAdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_related_ad, parent, false)
        return RelatedAdViewHolder(view)
    }

    override fun getItemCount(): Int = ads.size

    override fun onBindViewHolder(holder: RelatedAdViewHolder, position: Int) {
        val ad = ads[position]
        holder.fillFrom(ad)
        holder.itemView.setOnClickListener {
            clickListener(ad)
        }
    }
}