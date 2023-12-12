package com.aelzohry.topsaleqatar.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.aelzohry.topsaleqatar.R

private const val ARG_URL = "URL"

class VideoPlayerActivity : AppCompatActivity() {

    private var url: String? = null
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        videoView = findViewById(R.id.videoView)
        intent?.let {
            url = it.getStringExtra(ARG_URL)
        }

        play()
    }

    private fun play() {
        val url = url ?: return

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        val uri = Uri.parse(url)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(fromContext: Context, url: String) = Intent(fromContext, VideoPlayerActivity::class.java).apply {
            putExtra(ARG_URL, url)
        }
    }

}