package com.aelzohry.topsaleqatar.ui.messages.adapter

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ItemMessageReceivedBinding
import com.aelzohry.topsaleqatar.databinding.ItemMessageSentBinding
import com.aelzohry.topsaleqatar.helper.*
import com.aelzohry.topsaleqatar.model.TMessage
import com.aelzohry.topsaleqatar.ui.messages.vm.ChatViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseViewHolder
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import kotlinx.android.parcel.Parcelize

class MessagesAdapter(
    private val senderId: String,
    private val vm: ChatViewModel,
    val newSeenAction: (TMessage) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    private val messages: ArrayList<TMessage> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_MESSAGE_RECEIVED)
            BaseViewHolder<TMessage, ItemMessageReceivedBinding>(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_message_received,
                    parent,
                    false
                ), vm
            )
        else BaseViewHolder<TMessage, ItemMessageSentBinding>(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_message_sent,
                parent,
                false
            ), vm
        )
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.user?._id.equals(senderId)) VIEW_TYPE_MESSAGE_SENT else VIEW_TYPE_MESSAGE_RECEIVED
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val message = messages[position]
        Log.e("message_type", message.type)

        message.messageState = when (message.type) {
            "audio" -> MessageState.AUDIO
            "video" -> MessageState.VIDEO
            "photo" -> MessageState.PHOTO
            "text" -> MessageState.TEXT
            else -> MessageState.EMPTY
        }
        val isSender = getItemViewType(position) == VIEW_TYPE_MESSAGE_SENT
        val showDateTitle = when (position) {
            0 -> true
            else -> {
                val previousMessage = messages[position - 1]
                previousMessage.createdAt.toDate()?.toDateOnlyString() != message.createdAt.toDate()
                    ?.toDateOnlyString()
            }
        }

        var seekBar: SeekBar? = null
        when (getItemViewType(position)) {
            VIEW_TYPE_MESSAGE_SENT -> {
                var h = (holder as BaseViewHolder<TMessage, ItemMessageSentBinding>)
                h.bind(message)
                setupDateView(
                    h.binding.dateTextView, message, showDateTitle
                )
                h.binding.timeTextView.text = message.createdAt.toDate()?.toTimeOnlyString()
                seekBar = holder.binding.seekBar

            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                var h = (holder as BaseViewHolder<TMessage, ItemMessageReceivedBinding>)
                h.bind(message)
                setupDateView(
                    h.binding.dateTextView, message, showDateTitle
                )
                h.binding.timeTextView.text = message.createdAt.toDate()?.toTimeOnlyString()
                seekBar = holder.binding.seekBar
            }
        }

//        holder.fillFrom(position, message, isSender, showDateTitle)

        if (!isSender && !message.seen) {
            newSeenAction(message)
            message.seen = true
        }

        if (message.messageState != MessageState.AUDIO)
            return
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                vm.removeCallback()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                vm.onStopTacking(seekBar)
            }

        })
    }

    private fun setupDateView(tv: TextView, message: TMessage, isShow: Boolean) {
        tv.setVisible(isShow)
        message.createdAt.toDate()?.let {
            val context = tv.context
            tv.text = when {
                it.isToday() -> context.getString(R.string.today)
                it.isYesterday() -> context.getString(R.string.yesterday)
                else -> it.toDateOnlyString()
            }
        }
    }

    fun addMessages(newMessages: ArrayList<TMessage>) {
        this.messages.addAll(newMessages)
        sort()
        notifyDataSetChanged()
    }

    fun addMessage(newMessage: TMessage) {
        this.messages.add(newMessage)
        sort()
        notifyDataSetChanged()
    }

    fun isMessageAdded(messageId: String): Boolean {
        return this.messages.firstOrNull { it._id == messageId } != null
    }

    private fun sort() {
        this.messages.sortBy { message -> message.createdAt }
    }

    fun clear() {
        messages.clear()
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = messages.isEmpty()

    val messagesCount: Int
        get() = messages.size

    @Parcelize
    enum class MessageState : Parcelable {
        EMPTY,
        AUDIO,
        VIDEO,
        PHOTO,
        TEXT,
    }


}