package com.aelzohry.topsaleqatar.ui.more.my_ads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Ad

class MyAdsAdapter(val ads: ArrayList<Ad>, val listener: MyAdListener, val tapHandler: (ad: Ad)->Unit): RecyclerView.Adapter<MyAdViewHolder>() {

    interface MyAdListener {
        fun deleteAd(ad: Ad)
        fun editAd(ad: Ad)
        fun republishAd(ad: Ad)
        fun toggleActivationForAd(ad: Ad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_my_ad, parent, false)
        return MyAdViewHolder(view)
    }

    override fun getItemCount(): Int = ads.size

    override fun onBindViewHolder(holder: MyAdViewHolder, position: Int) {
        val ad = ads[position]
        holder.fillFrom(ad)


//        holder.deleteButton.setOnClickListener { listener.deleteAd(ad) }
//        holder.editButton.setOnClickListener { listener.editAd(ad) }
//        holder.republishButton.setOnClickListener { listener.republishAd(ad) }
//        holder.toggleActivationButton.setOnClickListener { listener.toggleActivationForAd(ad) }
        holder.itemView.setOnClickListener { tapHandler(ad) }
    }
}