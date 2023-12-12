package com.aelzohry.topsaleqatar.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImageUtils {
    private const val BASE_IMAGE_NAME = "i_prefix_"
    fun savePicture(context: Context, bitmap: Bitmap, imageSuffix: String): String {
        val savedImage: File = getTemporalFile(context, "$imageSuffix.jpeg")
        var fos: FileOutputStream? = null
        if (savedImage.exists()) {
            savedImage.delete()
        }
        try {
            fos = FileOutputStream(savedImage.getPath())
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return savedImage.getAbsolutePath()
    }

    fun getTemporalFile(context: Context, payload: String): File {
        return File(context.getExternalCacheDir(), BASE_IMAGE_NAME + payload)
    }
}