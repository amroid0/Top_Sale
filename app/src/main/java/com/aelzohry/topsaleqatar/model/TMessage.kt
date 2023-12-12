package com.aelzohry.topsaleqatar.model

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.helper.toFilePath
import com.aelzohry.topsaleqatar.ui.messages.adapter.MessagesAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TMessage(
    val _id: String,
    val user: User?,
    val createdAt: String,
    @SerializedName("channel") val channelId: String?,
    val type: String,
    val body: String?,
    val attachment: String?,
    val thumbnail: String?,
    var seen: Boolean,
    val latitude: Double?,
    val longitude: Double?,
    val duration: Double?,
    var audioState: Boolean = false,
) : Parcelable, BaseObservable() {

    var messageState: MessagesAdapter.MessageState = MessagesAdapter.MessageState.EMPTY

    val thumbnailUrl: String?
        get() = this.thumbnail?.toFilePath()

    val attachmentUrl: String?
        get() = this.attachment?.toFilePath()

    var audio_state: Boolean
        @Bindable get() = audioState
        set(value) {
            audioState = audio_state
            notifyPropertyChanged(BR.audio_state)
        }
}

data class TNewMessage(
    @SerializedName("new_message") val newMessage: TMessage
)