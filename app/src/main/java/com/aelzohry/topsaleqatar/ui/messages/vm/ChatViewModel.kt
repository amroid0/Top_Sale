package com.aelzohry.topsaleqatar.ui.messages.vm

import android.media.MediaPlayer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.TMessage
import com.aelzohry.topsaleqatar.ui.VideoPlayerActivity
import com.aelzohry.topsaleqatar.utils.SingleLiveEvent
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.IOException

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class ChatViewModel : BaseViewModel(), MediaPlayer.OnCompletionListener {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    var playCurrentPosition = ObservableField(-1)
    var progressState = ObservableField(0)
    var play = ObservableField(false)
    private var mHandler = Handler()
    var mUpdateTimeTask = object : Runnable {
        override fun run() {

            try {
                val totalDuration = mediaPlayer.duration
                val currentDuration = mediaPlayer.currentPosition

                // Displaying Total Duration time
//        songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
//        songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
                val progress = getProgressPercentage(currentDuration.toLong(), totalDuration.toLong())
                progressState.set(progress)
                mHandler.postDelayed(this, 100)
            }catch (ex:Exception){

            }

        }

    }

    init {
        mediaPlayer.setOnCompletionListener(this)
    }

    private fun playVoice(
        model: TMessage,
    ) {

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(model.attachmentUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()
            play.set(true)
            model.audio_state = true
            mHandler.postDelayed(mUpdateTimeTask, 100);

        } catch (ex: IllegalArgumentException) {
        } catch (ex: IllegalStateException) {
        } catch (ex: IOException) {
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {

        progressState.set(0)
//        currentSeekBar?.max = 100
        play.set(false)
//        currentBtn?.setImageResource(R.drawable.ic_play_voice)
    }

    private fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {

        val currentSeconds = (currentDuration / 1000).toInt()
        val totalSeconds = (totalDuration / 1000).toInt()

        // calculating percentage
        val percentage = ((currentSeconds.toDouble()) / totalSeconds) * 100

        // return percentage
        return percentage.toInt()
    }

    fun removeCallback() {
        mHandler.removeCallbacks(mUpdateTimeTask)
    }


    fun onStopTacking(seekBar: SeekBar?) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        val totalDuration = mediaPlayer.duration
        val currentPosition = progressToTimer(seekBar?.progress, totalDuration)

        mediaPlayer.seekTo(currentPosition)
        mHandler.postDelayed(mUpdateTimeTask, 100)

    }

    private fun progressToTimer(progress: Int?, totalDuration: Int): Int {
        var currentDuration =
            (((progress?.toDouble()) ?: 0.0 / 100) * (totalDuration / 1000)).toInt()
        return currentDuration * 1000
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mediaPlayer.release()
    }

    fun onBtnAudioClickedListener(position: Int, model: TMessage) {
        if (position == playCurrentPosition.get()) {

            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
            play.set(mediaPlayer.isPlaying)
            return
        }

        model.attachmentUrl ?: return

        playCurrentPosition.set(position)
        playVoice(model)
    }

    fun onImageClickedListener(v: View, model: TMessage) {
        StfalconImageViewer.Builder(
            v.context,
            arrayOf(model)
        ) { view, message ->
            Picasso.get().load(message.attachmentUrl).into(view)
        }.show()
    }

    fun onVideoClickedListener(v: View, message: TMessage) {
        val url = message.attachmentUrl ?: return
        val intent = VideoPlayerActivity.newInstance(v.context, url)
        v.context.startActivity(intent)
    }
}