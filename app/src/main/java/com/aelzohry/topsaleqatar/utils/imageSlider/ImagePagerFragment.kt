package com.aelzohry.topsaleqatar.utils.imageSlider

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.aelzohry.topsaleqatar.databinding.FragmentImagePagerBinding
import com.aelzohry.topsaleqatar.ui.ad_details.AdDetailsActivity
import com.aelzohry.topsaleqatar.utils.Binding.setImageNoPlaceHolder

class ImagePagerFragment : Fragment(), View.OnClickListener {
    private var list: ArrayList<ImageSlider>? = ArrayList()
    private var position = 0
    private lateinit var binding: FragmentImagePagerBinding
    private lateinit var mediacontroller: MediaController

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val args = arguments
        binding = FragmentImagePagerBinding.inflate(layoutInflater)
        val view = binding.root
        if (args != null) {
            list = args.getSerializable("arrSlider") as ArrayList<ImageSlider>?
            position = args.getInt(ARG_OBJECT)
            val imageSlider = list!![position]
//
            if (imageSlider != null) {
//                if (activity is AdDetailsActivity) {
//                    binding.image.scaleType = ImageView.ScaleType.CENTER_CROP
//                    setImageNoPlaceHolder(requireContext(), binding.image, imageSlider.mediumMediaUrl)
//                    binding.imgPlay.setVisibility(if (imageSlider.image) GONE else VISIBLE)

//                } else {
                if (imageSlider.image) {
                    binding.image.setOnClickListener(this)

                    setImageNoPlaceHolder(requireContext(), binding.image, imageSlider.largeMediaUrl)
                    binding.imgPlay.setVisibility(GONE)
                    binding.video.setVisibility(GONE)

                } else {
                    setImageNoPlaceHolder(requireContext(), binding.image, imageSlider.mediumMediaUrl)
                    binding.imgPlay.setVisibility(VISIBLE)
                    val videoView = binding.video
                    binding.imgPlay.setVisibility(VISIBLE)
                    binding.video.setVisibility(VISIBLE)
                    binding.imgPlay.setOnClickListener { view1: View? ->
                        mediacontroller = MediaController(context)
                        if (::mediacontroller.isInitialized) {
                            mediacontroller.setAnchorView(videoView)
                            mediacontroller.setMediaPlayer(videoView)
                            videoView.setMediaController(mediacontroller)
                            val video = Uri.parse(imageSlider.largeMediaUrl)
                            videoView.setMediaController(mediacontroller)
                            videoView.setVideoURI(video)
                            binding.imgPlay.setVisibility(GONE)
                            binding.loading.setVisibility(VISIBLE)
                        }

                    }

                    binding.imgPlay.performClick()
                    videoView.setOnPreparedListener { mediaPlayer: MediaPlayer? ->
                        binding.image.setVisibility(GONE)
                        binding.loading.setVisibility(GONE)
                        binding.imgPlay.setVisibility(GONE)
                        videoView.start()
                    }
                }
//                }
            }
        }

        val swipeImageTouchListener = SwipeImageTouchListener(binding.image)
        binding.pagerFragment.setOnTouchListener(swipeImageTouchListener)

        swipeImageTouchListener.setSwipeListener(object : SwipeImageTouchListener.SwipeListener {
            override fun onDragStart() {
                Log.e("test", "onDragStart")
            }

            override fun onDragStop() {
                Log.e("test", "onDragStop")

            }

            override fun onDismissed() {
                requireActivity().finish()
            }

        })
        return view
    }

    override fun onPause() {
        super.onPause()
        if (binding.video .isPlaying) {
            binding.video.stopPlayback()
        }
    }

    override fun onResume() {
        super.onResume()
            binding.video.resume()
    }
    override fun onClick(v: View) {
        if (activity is AdDetailsActivity) {
            startActivity(ImageFullScreenActivity.newInstance(activity, list, position))
        }

//        if (getActivity() instanceof MainActivity) {
//            startActivity(ZoomActivity.newInstance(getActivity(), list, position));
//        }
    }

    companion object {
        const val ARG_OBJECT = "object"

        @JvmStatic
        fun newInstance(list: ArrayList<ImageSlider?>?, position: Int): ImagePagerFragment {
            val fragment = ImagePagerFragment()
            val args = Bundle()
            args.putInt(ARG_OBJECT, position)
            args.putSerializable("arrSlider", list)
            fragment.arguments = args
            return fragment
        }
    }
}

class SwipeImageTouchListener(private val swipeView: View) : View.OnTouchListener {

    interface SwipeListener {
        fun onDragStart()
        fun onDragStop()
        fun onDismissed()
    }

    companion object {
        private const val TAG = "SwipeImageTouchListener"
    }

    // Allows us to know if we should use MotionEvent.ACTION_MOVE
    private var tracking = false

    // The Position where our touch event started
    private var startY: Float = 0.0f
    private var swipeListener: SwipeListener? = null
    private var isDragStarted = false

    fun setSwipeListener(swipeListener: SwipeListener) {
        this.swipeListener = swipeListener
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    val hitRect = Rect()
                    swipeView.getHitRect(hitRect)
                    if (hitRect.contains(event.x.toInt(), event.y.toInt())) tracking = true
                    startY = it.y
                    return true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    tracking = false
                    animateSwipeView(v!!.height)
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (tracking) {
                        swipeView.translationY = it.y - startY
                        if (!isDragStarted) {
                            isDragStarted = true
                            swipeListener?.onDragStart()
                        }

                    }
                    return true
                }

                else -> {
                    false
                }
            }
        }

        return false
    }

    /**
     * Using the current translation of swipeView, decide if it has moved
     * to the point where we want to remove it.
     */
    private fun animateSwipeView(parentHeight: Int) {
        Log.i(TAG, "animateSwipeView: parentHeight : $parentHeight")
        val halfHeight = parentHeight / 2
        Log.i(TAG, "animateSwipeView :: octalHeight : $halfHeight")
        val currentPosition = swipeView.translationY

        Log.i(TAG, "animateSwipeView: currentPosition : $currentPosition")

        var animateTo = 0.0f
        if (currentPosition < -halfHeight) {
            animateTo = (-parentHeight).toFloat()
        } else if (currentPosition > halfHeight) {
            animateTo = parentHeight.toFloat()
        }

        if (animateTo == 0.0f) {
            swipeListener?.onDragStop()
            isDragStarted = false
        } else {
            swipeListener?.onDismissed()
        }

        ObjectAnimator.ofFloat(swipeView, "translationY", currentPosition, animateTo).setDuration(200).start()
    }



}