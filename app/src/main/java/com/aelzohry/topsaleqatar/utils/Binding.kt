package com.aelzohry.topsaleqatar.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AdStatus
import com.aelzohry.topsaleqatar.utils.customViews.CustomEditText
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import com.wang.avi.AVLoadingIndicatorView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object Binding {

    @BindingAdapter("android:load_image")
    @JvmStatic
    fun loadImage(imageView: ImageView, path: String?) {
        if (path.isNullOrEmpty()) {
            Picasso
                .get()
                .load(R.mipmap.logo)
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo)
                .into(imageView)
            return
        }



        Picasso
            .get()
            .load(path)
            .error(R.mipmap.logo)
            .placeholder(R.mipmap.logo)
            .into(imageView)
    }

    @JvmStatic
    fun setImage(context: Context, imageView: ImageView, path: String?) {
        if (path.isNullOrEmpty()) {
            Picasso
                .get()
                .load(R.mipmap.logo)
                .error(R.mipmap.logo)
                .placeholder(R.mipmap.logo)
                .into(imageView)
            return
        }

        Glide.with(context).load(path)
            .error(R.mipmap.logo)
            .placeholder(0)
            .into(imageView)

//        Picasso
//            .get()
//            .load(path)
//            .error(R.mipmap.logo)
//            .placeholder(R.mipmap.logo)
//            .into(imageView)
    }


    @JvmStatic
    fun setImageNoPlaceHolder(context: Context, imageView: ImageView, path: String?) {
//        if (path.isNullOrEmpty()) {
//            Picasso
//                .get()
//                .load(R.mipmap.logo)
//                .error(R.mipmap.logo)
//                .into(imageView)
//            return
//        }

        Glide.with(context)
            .load(path)
            .into(object : CustomTarget<Drawable?>() {

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    imageView.setImageDrawable(resource)
//                    iv.setVisible(true)

//                    lv.hide()
//                    lv.setVisible(false)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

    }

    fun setImageNoPlaceHolderWithLoader(context: Context, imageView: ImageView,loading: AVLoadingIndicatorView, path: String?) {
//        if (path.isNullOrEmpty()) {
//            Picasso
//                .get()
//                .load(R.mipmap.logo)
//                .error(R.mipmap.logo)
//                .into(imageView)
//            return
//        }
        loading.isVisible=true
        Glide.with(context)
            .load(path)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.no_img_placeholder)
            .error(R.drawable.no_img_placeholder)
            .listener(object : RequestListener<Drawable?> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    loading.isVisible=false

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    loading.isVisible=false
                    return false

                }
            })
            .into(imageView)

    }

    @BindingAdapter("android:load_image")
    @JvmStatic
    fun loadImage(liv: FrameLayout, path: String?) {

        val LIST_LOADING = arrayListOf(
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator"
        )

        val iv = liv.findViewById<ImageView>(R.id.iv)
        val lv = liv.findViewById<AVLoadingIndicatorView>(R.id.loading)
        val position = Random.nextInt(0, LIST_LOADING.size.minus(1))
//        val selectedPosition: String = LIST_LOADING[position]
        val selectedPosition = "BallScaleIndicator"
        lv.setIndicator(selectedPosition)
        iv.setVisible(true)
        Glide
            .with(liv)
            .load(path)
            .error(R.mipmap.logo)
            .into(object : CustomTarget<Drawable?>() {

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    iv.setImageDrawable(resource)
                    iv.setVisible(true)

                    lv.hide()
                    lv.setVisible(false)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    iv.setImageDrawable(errorDrawable)
                    iv.setVisible(true)

                    lv.hide()
                    lv.setVisible(false)
                }
            })
    }


    @BindingAdapter("android:load_image_new")
    @JvmStatic
    fun loadImageNoPlaceHolder(liv: FrameLayout, ad: Ad) {

        val iv = liv.findViewById<ImageView>(R.id.iv)
        val lv = liv.findViewById<AVLoadingIndicatorView>(R.id.loading)
        val selectedPosition = "BallScaleIndicator"
        lv.setIndicator(selectedPosition)
        iv.setVisible(true)

        var photoUrl = ""
        if (TextUtils.isEmpty(ad.thumbImageUrl)) {
            ad.video?.let {
                photoUrl = it.thumbUrl
            }
        } else {
            ad.thumbImageUrl?.let {
                photoUrl = it
            }
        }
        Glide
            .with(liv)
            .load(photoUrl)
            .error(R.drawable.no_img_placeholder)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    iv.setImageDrawable(resource)
                    iv.setVisible(true)

                    lv.hide()
                    lv.setVisible(false)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    iv.setImageDrawable(errorDrawable)
                    iv.setVisible(true)

                    lv.hide()
                    lv.setVisible(false)
                }
            })
    }

//    @BindingAdapter("android:load_image_2")
//    @JvmStatic
//    fun loadImage2(liv: FrameLayout, path: String?) {
//
//        val LIST_LOADING = arrayListOf(
//            "BallPulseIndicator",
//            "BallGridPulseIndicator",
//            "BallClipRotateIndicator",
//            "BallClipRotatePulseIndicator",
//            "SquareSpinIndicator",
//            "BallClipRotateMultipleIndicator",
//            "BallPulseRiseIndicator",
//            "BallRotateIndicator",
//            "CubeTransitionIndicator",
//            "BallZigZagIndicator",
//            "BallZigZagDeflectIndicator",
//            "BallTrianglePathIndicator",
//            "BallScaleIndicator",
//            "LineScaleIndicator",
//            "LineScalePartyIndicator",
//            "BallScaleMultipleIndicator",
//            "BallPulseSyncIndicator",
//            "BallBeatIndicator",
//            "LineScalePulseOutIndicator",
//            "LineScalePulseOutRapidIndicator",
//            "BallScaleRippleIndicator",
//            "BallScaleRippleMultipleIndicator",
//            "BallSpinFadeLoaderIndicator",
//            "LineSpinFadeLoaderIndicator"
//        )
//
//        val iv = liv.findViewById<ImageView>(R.id.iv2)
//        val lv = liv.findViewById<AVLoadingIndicatorView>(R.id.loading2)
//        val position = Random.nextInt(0, LIST_LOADING.size.minus(1))
//        val selectedPosition: String = LIST_LOADING[position]
//        lv.setIndicator(selectedPosition)
//        iv.setVisible(true)
//        Glide
//            .with(liv)
//            .load(path)
//            .error(R.mipmap.logo)
//            .into(object : CustomTarget<Drawable?>() {
//
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable?>?
//                ) {
//                    iv.setImageDrawable(resource)
//                    iv.setVisible(true)
//
//                    lv.hide()
//                    lv.setVisible(false)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    super.onLoadFailed(errorDrawable)
//                    iv.setImageDrawable(errorDrawable)
//                    iv.setVisible(true)
//
//                    lv.hide()
//                    lv.setVisible(false)
//                }
//            })
//    }

    @BindingAdapter("android:load_res_image")
    @JvmStatic
    fun loadResImage(imageView: ImageView, int: Int) {

        if (int == -1)
            return
        Glide.with(imageView).load(int).into(imageView)
//        imageView.setImageResource(int)
    }

    @BindingAdapter("android:is_visible")
    @JvmStatic
    fun setVisible(v: View, isVisible: Boolean) {
        v.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("android:setInt")
    @JvmStatic
    fun setInt(textView: TextView, value: Int) {
        textView.text = value.toString()
    }

    @BindingAdapter("android:set_double")
    @JvmStatic
    fun setDouble(textView: TextView, value: Double) {
        textView.text = String.format(Locale.ENGLISH, "%.0f", value)

    }


    @BindingAdapter("android:day")
    @JvmStatic
    fun setDay(textView: TextView, value: String) {
        var day = value.substring(0, 2)
        textView.text = day
    }

    @BindingAdapter("android:month")
    @JvmStatic
    fun setMonth(textView: TextView, value: String) {
        var month = value.substring(2)
        textView.text = month
    }

    @BindingAdapter("android:set_rate")
    @JvmStatic
    fun setRate(ratingBar: RatingBar, rate: String) {
        ratingBar.rating = rate.toFloat()
    }

    @BindingAdapter("android:add_divider")
    @JvmStatic
    fun addDivider(rv: RecyclerView, @DrawableRes drawableId: Int) {
        rv.addItemDecoration(
            DividerItemDecorator(
                (ContextCompat.getDrawable(
                    rv.context,
                    drawableId
                ))
            )
        )
    }

    @BindingAdapter("android:set_error")
    @JvmStatic
    fun setError(et: TextInputLayout, @StringRes errorRes: Int) {

        if (errorRes == 0)
            return

        et.error = et.context.getString(errorRes)
        et.editText?.requestFocus()
        et.startAnimation(AnimationUtils.loadAnimation(et.context, R.anim.shake))
        Handler().postDelayed({
            et.error = null
        }, 2000)
    }


    @BindingAdapter("android:frame_state")
    @JvmStatic
    fun frameState(customFrame: CustomFrame, frameState: CustomFrame.FrameState) {
        customFrame.setVisible(frameState)
    }

    @BindingAdapter("android:frame_layout")
    @JvmStatic
    fun frameLayout(customFrame: CustomFrame, viewId: Int) {

        for (x in 0 until customFrame.childCount) {
            val nextChild = customFrame.getChildAt(x)
            if (nextChild.id == viewId) {
                customFrame.setLayout(nextChild)
            }
        }
    }

    @BindingAdapter("android:set_font")
    @JvmStatic
    fun setFont(et: TextInputLayout, fontName: String) {
//        et.setfont(fontName)
    }

    @BindingAdapter("android:html")
    @JvmStatic
    fun displayHtml(tv: TextView, @Nullable html: String) {
        tv.text = Html.fromHtml(html)
//        view.setHtml(html, HtmlResImageGetter(view.context))
    }

    @BindingAdapter("android:format_time")
    @JvmStatic
    fun formatTime(tv: TextView, inputDateString: String) {
        val inputDateFormat = SimpleDateFormat("HH:mm", Locale.US)
        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale(Helper.languageCode))
        tv.text = try {
            val date = inputDateFormat.parse(inputDateString)
            outputDateFormat.format(date)
        } catch (ex: Exception) {
            inputDateString
        }
    }

    @BindingAdapter("android:format_date")
    @JvmStatic
    fun formatDate(tv: TextView, inputDateString: String) {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputDateFormat =
            SimpleDateFormat("EEE dd-MMM-yyyy", Locale(Helper.languageCode))
        tv.text = try {
            val date = inputDateFormat.parse(inputDateString)
//            if (date.isToday())
//                tv.context.getString(R.string.today)
            outputDateFormat.format(date)
        } catch (ex: Exception) {
            inputDateString
        }
    }

    @BindingAdapter("android:set_text")
    @JvmStatic
    fun setText(tv: TextView, value: Any) {
        when (value) {
            is String -> tv.text = value
            is Int -> tv.text = tv.context.resources.getString(value)
        }
    }

    @BindingAdapter("android:discount_line")
    @JvmStatic
    fun setLine(tv: TextView, isDraw: Boolean) {
        if (isDraw)
            tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

    }

    @BindingAdapter("android:is_refresh")
    @JvmStatic
    fun isRefresh(v: SwipeRefreshLayout, isRefresh: Boolean) {
        v.isRefreshing = isRefresh
    }

    @BindingAdapter("android:ad_status")
    @JvmStatic
    fun adStatus(tv: TextView, ad: Ad) {
        tv.text = ad.status.getTitle(tv.context)
        tv.setTextColor(
            ContextCompat.getColor(
                tv.context,
                if (ad.status == AdStatus.ACTIVE) R.color.statusActive else R.color.statusNonActive
            )
        )

    }

    @BindingAdapter("android:ad_rejected_status")
    @JvmStatic
    fun adRejectedStatus(tv: TextView, ad: Ad) {
        tv.visibility = if (ad.status == AdStatus.REJECTED) View.VISIBLE else View.GONE
        tv.text =
            tv.context.resources.getString(R.string.reject_note, ad.rejectReason?.localized ?: "")

    }

    @BindingAdapter("android:set_text_gone")
    @JvmStatic
    fun setWithHidenText(tv: TextView, value: String?) {
        tv.setVisible(value?.isNotEmpty() == true)
        value?.let {
            tv.text = value
        }
    }


    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(customView: CustomEditText, text: String) {
        customView.text = text
    }
}