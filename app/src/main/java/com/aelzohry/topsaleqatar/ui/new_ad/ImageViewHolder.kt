package com.aelzohry.topsaleqatar.ui.new_ad

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.squareup.picasso.Picasso

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    fun clear() {
        imageView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.new_image))
        imageView.background = null
    }

    fun fillFrom(image: NewImage, isDefault: Boolean) {
        imageView.background = if (isDefault) ContextCompat.getDrawable(itemView.context, R.drawable.default_image_background) else null
        if (image.localPath != null) {
            imageView.setImageBitmap(image.bitmap)
            return
        }
        Picasso.get().load(image.photo?.thumb).placeholder(R.mipmap.logo).into(imageView)
    }
}