package com.aelzohry.topsaleqatar.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import java.util.*

object Localization {


    fun setLanguage(context: Context, lang: String) {

        val mLocale: Locale = if (lang == "ar")
            Locale("ar")
        else
            Locale("en")

        Locale.setDefault(mLocale)
        val config = context.resources.configuration

        if (config.locale != mLocale) {
            config.locale = mLocale
            config.setLayoutDirection(config.locale)
            context.resources.updateConfiguration(config, null)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                (context as Activity).window
                    .decorView.layoutDirection =
                    if (Locale.getDefault().language.equals("ar", ignoreCase = true))
                        View.LAYOUT_DIRECTION_RTL
                    else
                        View.LAYOUT_DIRECTION_LTR
            }
        }
    }


    fun getLanguage() =
        if (Locale.getDefault().language.contains("ar", true)) "ar" else "en"

}
