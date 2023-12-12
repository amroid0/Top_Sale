package com.aelzohry.topsaleqatar.ui.new_ad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R

class ImagesAdapter(
    var images: ArrayList<NewImage>,
    var defaultImageIndex: Int = 0,
    val clickListener: (position: Int,v:View) -> Unit,
    val newListener: () -> Unit
) : RecyclerView.Adapter<ImageViewHolder>() {
    val MAX_IMAGES = 15

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_new_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = images.size + if (images.size < MAX_IMAGES) 0 else 0

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (position == images.size) {
            holder.itemView.setOnClickListener { newListener() }
            holder.clear()
            return
        }

        val image = images[position]
        holder.fillFrom(image, position == defaultImageIndex)
        holder.itemView.setOnClickListener { clickListener(position,it) }
    }
}