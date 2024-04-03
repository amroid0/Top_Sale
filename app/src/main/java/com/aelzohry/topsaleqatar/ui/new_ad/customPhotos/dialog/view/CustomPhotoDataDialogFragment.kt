package com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.dialog.view

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.App
import com.aelzohry.topsaleqatar.BuildConfig
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentCustomPhotoDataDialogBinding
import com.aelzohry.topsaleqatar.ui.new_ad.saveImageToGallery
import com.aelzohry.topsaleqatar.utils.base.BaseDialogFragment
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class CustomPhotoDataDialogFragment :
    BaseDialogFragment<FragmentCustomPhotoDataDialogBinding, BaseViewModel>() {

    private var selectedItemId: Int = 0

    private lateinit var mListener: Listener


    private var currentBitmap: Bitmap? = null


    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance(selectedItemId: Int) =
            CustomPhotoDataDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt("selectedItemId", selectedItemId)
                }
            }
    }

    override fun getLayoutID(): Int = R.layout.fragment_custom_photo_data_dialog


    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]


    override fun setupUI() {
        if (arguments != null) {
            selectedItemId = requireArguments().getInt("selectedItemId")
        }
        setEditTextLimitById(selectedItemId)
        fillData()
    }

    override fun onClickedListener() {
        binding.ibClose.setOnClickListener { dismiss() }
        binding.detailsTextView.doAfterTextChanged {
            fillData()
        }
        binding.btnNext.setOnClickListener {
                if (currentBitmap != null) {
                    Log.e("test_image",currentBitmap.toString())
                    val imgName = UUID.randomUUID().toString()

                    saveImageToGallery(currentBitmap!!,imgName).toString()
                    val downloadedFile = File(
                        Environment.getExternalStorageDirectory().path,
                        Environment.DIRECTORY_PICTURES + File.separator + "topsale/ads" + "/Image_" + imgName + ".jpeg"
                    )

                    mListener.onItemSelected(downloadedFile.absolutePath)

                }
            dismiss()
        }
    }


    //    public static Bitmap loadBitmapFromView(Context context, View view) {
    //        DisplayMetrics displayMetrics = new DisplayMetrics();
    //        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    //        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    //
    ////        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
    ////        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
    //        view.buildDrawingCache();
    //        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    //
    //        Canvas canvas = new Canvas(bitmap);
    //        view.draw(canvas);
    //
    //        return bitmap;
    //    }
    fun saveImage(mActivity: Activity, image: Bitmap): Uri? {
        //TODO - Should be processed in another thread

        val imagesFolder = File(
            Environment.getExternalStorageDirectory().path, Environment.DIRECTORY_DCIM
        )
//        val imagesFolder = File(mActivity.cacheDir, "images")
        var uri: Uri? = null
        val imgName = UUID.randomUUID().toString()
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, imgName+".png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            uri = FileProvider.getUriForFile(
                mActivity,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
        } catch (e: IOException) {
        }
        return uri
    }

    override fun observerLiveData() {
    }

    private fun fillData() {
        binding.llContainer.removeAllViews()
        binding.llContainer.addView(
            getViewForTemplate(
                layoutInflater,
                binding.llContainer,
                selectedItemId,
                binding.detailsTextView.text.toString()
            )
        )
    }

    fun getViewForTemplate(
        inflater: LayoutInflater,
        viewGroup: ViewGroup,
        selectedTemplateId: Int,
        text: String
    ): View {
        val view: View = inflater.inflate(getResourceById(selectedTemplateId), viewGroup, false)
        val tvText = view.findViewById<TextView>(R.id.tv_text)
        tvText.text = text

        currentBitmap = convertViewToDrawable(view)
        return view
    }

    fun getResourceById(selectedTemplateId: Int): Int {
        when (selectedTemplateId) {
            1 -> return R.layout.item_normal_template_1
            2 -> return R.layout.item_normal_template_2
            3 -> return R.layout.item_normal_template_3
            4 -> return R.layout.item_normal_template_4
            5 -> return R.layout.item_normal_template_5
            6 -> return R.layout.item_normal_template_6
            else -> return R.layout.item_normal_template_1
        }
    }

    fun setEditTextLimitById(selectedTemplateId: Int) {
        var maxLength = 100
        when (selectedTemplateId) {
            1 -> maxLength = 13
            2 -> maxLength = 150
            3 -> maxLength = 150
            4 -> maxLength = 50
            5 -> maxLength = 200
            6 -> maxLength = 200

        }

        binding.detailsTextView.limitLength(maxLength)
    }

    fun EditText.limitLength(maxLength: Int) {
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }


//    fun getBitmapFromView(view: View): Bitmap {
//        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        return bitmap
//    }


    private fun convertViewToDrawable(view: View): Bitmap {
        val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(spec, spec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val b = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        c.translate((-view.scrollX).toFloat(), (-view.scrollY).toFloat())
        view.draw(c)
        return b
    }

    fun loadBitmapFromView(context: Context?, v: View): Bitmap {
        v.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(v.drawingCache)
        v.isDrawingCacheEnabled = false
        return bitmap
    }

    interface Listener {
        fun onItemSelected(bitmap: String)
    }
}