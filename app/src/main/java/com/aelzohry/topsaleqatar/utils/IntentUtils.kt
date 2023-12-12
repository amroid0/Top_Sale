package com.aelzohry.topsaleqatar.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aelzohry.topsaleqatar.R

object IntentUtils {

    fun shareContent(context:Context,content:String){

        /*Create an ACTION_SEND Intent*/
        val intent =  Intent(android.content.Intent.ACTION_SEND)
        /*The type of the content is text, obviously.*/
        intent.type = "text/plain";
        /*Applying information Subject and Body.*/
        intent.putExtra(Intent.EXTRA_SUBJECT,context. getString(R.string.share_app));
        /*This will be the actual content you wish you share.*/
        intent.putExtra(Intent.EXTRA_TEXT, content)

        /*Fire!*/
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }
    fun viewLocation(c: Context, lat: String, lng: String) {
        c.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=$lat,$lng")
            )
                .setPackage("com.google.android.apps.maps")
        )

    }

    fun callPhone(context: Context, phone: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:$phone")
            )
        )
    }

    fun openWhatsApp(c: Context, number: String) {
        var i = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$number"))
        i.setPackage("com.whatsapp")
        c.startActivity(Intent.createChooser(i, ""))
    }

    fun openTwitterProfile(c: Context, userName: String) {
        try {
            c.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=$userName")
                )
            )

        } catch (ex: Exception) {
            c.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$userName")))
        }

    }

    fun openInstProfile(c: Context, userName: String) {

        try {
            c.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$userName"))
                    .setPackage("com.instagram.android")
            )
        } catch (ex: Exception) {
            c.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/$userName")
                )
            )
        }
    }

    fun openFbProfile(c: Context, userName: String) {

        try {
            c.packageManager
                .getPackageInfo("com.facebook.katana", 0)
            c.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/$userName")
                )
            )
        } catch (ex: Exception) {
            c.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/$userName")
                )
            )
        }
    }


    fun openSnapChatProfile(c: Context, userName: String) {
        try {
            c.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://snapchat.com/add/$userName"))
                    .setPackage("com.snapchat.android")
            )
        } catch (ex: Exception) {
            c.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://snapchat.com/add/$userName")
                )
            )
        }
    }
}