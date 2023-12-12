package com.aelzohry.topsaleqatar.ui.messages.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.*
import com.aelzohry.topsaleqatar.model.TMessage
import com.aelzohry.topsaleqatar.ui.VideoPlayerActivity
import com.aelzohry.topsaleqatar.ui.messages.vm.ChatViewModel
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.IOException
import kotlin.math.ceil

class MessageViewHolder(itemView: View, val vm: ChatViewModel) : RecyclerView.ViewHolder(itemView) {
    val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView) // title
    val timeTextView = itemView.findViewById<TextView>(R.id.timeTextView) // message time
    val bodyTextView = itemView.findViewById<TextView>(R.id.bodyTextView)
    val bodyImageView = itemView.findViewById<ImageView>(R.id.bodyImageView)
    val videoLayout = itemView.findViewById<View>(R.id.videoLayout)
    val videoImageView = itemView.findViewById<ImageView>(R.id.videoThumbnailImageView)
    val playVideoImageView = itemView.findViewById<ImageView>(R.id.playVideoButton)
    val audioLayout = itemView.findViewById<View>(R.id.audioLayout)
    val iv = itemView.findViewById<ImageView>(R.id.iv)

    //val audioPlayerView = itemView.findViewById<VoicePlayerView>(R.id.audioPlayerView)
//    val audioView = itemView.findViewById<AudioView2>(R.id.audioView)

    // received only
    val usernameTextView = itemView.findViewById<TextView>(R.id.usernameTextView)
    val avatarImageView = itemView.findViewById<ImageView>(R.id.avatarImageView)


    //    var mediaPlayer: MediaPlayer? = null
    var btn_action = itemView.findViewById<ImageView>(R.id.btn_action)
    var seekBar = itemView.findViewById<SeekBar>(R.id.seekBar)


    private fun clear() {
        arrayOf(dateTextView, bodyTextView, bodyImageView, videoLayout, audioLayout).forEach {
            it.visibility = View.GONE
        }
    }

    fun fillFrom(position: Int, message: TMessage, isSender: Boolean, showTitle: Boolean) {
        clear()
        timeTextView.text = message.createdAt.toDate()?.toTimeOnlyString()
        when (message.type) {
            "text" -> {
                bodyTextView.text = message.body
                bodyTextView.visibility = View.VISIBLE
            }
            "photo" -> {
                Picasso.get().load(message.attachmentUrl).resize(200, 200).centerCrop()
                    .into(bodyImageView)
                bodyImageView.setOnClickListener {
                    StfalconImageViewer.Builder(
                        itemView.context,
                        arrayOf(message)
                    ) { view, message ->
                        Picasso.get().load(message.attachmentUrl).into(view)
                    }.show()
                }
                bodyImageView.visibility = View.VISIBLE
            }
            "video" -> {
                Picasso.get().load(message.thumbnailUrl).resize(200, 200).centerCrop()
                    .into(videoImageView)
                playVideoImageView.setOnClickListener {
                    val context = itemView.context
                    val url = message.attachmentUrl ?: return@setOnClickListener
                    val intent = VideoPlayerActivity.newInstance(context, url)
                    context.startActivity(intent)
                }
                videoLayout.visibility = View.VISIBLE
            }
            "audio" -> {

                btn_action.setVisible(true)
                audioLayout.setVisible(true)




//                btn_action.setOnClickListener {
//                    vm.onPlayClickedListener(message.attachmentUrl, position,seekBar,btn_action, { isPlay ->
//                        btn_action.setImageResource(if (!isPlay) R.drawable.ic_play_voice else R.drawable.ic__pause)
//                    }, {
//                        seekBar.progress = 0
//                        seekBar.max = 100
//                    })
//                }


            }
            else -> {

            }
        }

        if (showTitle) {
            message.createdAt.toDate()?.let {
                val context = itemView.context
                dateTextView.text = when {
                    it.isToday() -> context.getString(R.string.today)
                    it.isYesterday() -> context.getString(R.string.yesterday)
                    else -> it.toDateOnlyString()
                }
                dateTextView.visibility = View.VISIBLE
            }
        }

        if (!isSender) {
//            usernameTextView.text = message.user?._name
//            Picasso.get().load(message.user?.avatarUrl).resize(32, 32).centerCrop()
//                .placeholder(R.drawable.avatar).into(avatarImageView)
        } else {
            Picasso.get().load(message.user?.avatarUrl).resize(32, 32).centerCrop()
                .placeholder(R.drawable.avatar).into(iv)
        }
    }


}