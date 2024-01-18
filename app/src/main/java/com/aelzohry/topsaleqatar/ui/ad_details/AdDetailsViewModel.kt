package com.aelzohry.topsaleqatar.ui.ad_details

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.BuildConfig
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Comment
import com.aelzohry.topsaleqatar.model.Photo
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.RecentComments
import com.aelzohry.topsaleqatar.ui.MainActivity
import com.aelzohry.topsaleqatar.ui.ad_details.relatedAds.RelatedAdsActivity
import com.aelzohry.topsaleqatar.ui.comments.CommentsFragment
import com.aelzohry.topsaleqatar.ui.messages.ChatFragment
import com.aelzohry.topsaleqatar.ui.user.UserFragment
import com.aelzohry.topsaleqatar.utils.SingleLiveEvent
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.base.BetterActivityResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.BranchError
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import java.io.*
import java.util.*

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class AdDetailsViewModel(var ad: Ad?, val adId: String) : BaseViewModel() {

    private var repository: Repository = RemoteRepository()

    var adRes = MutableLiveData<Ad>()
    var commentsRes = MutableLiveData<RecentComments>()
    var relatedAdsRes = MutableLiveData<ArrayList<Ad>>()
    var userAdsRes = MutableLiveData<ArrayList<Ad>>()
    var notFoundAds = SingleLiveEvent<Boolean>()

    var viewCount = ObservableField(ad?.viewsCount)
    val favState = ObservableField(ad?.isFavourite)
    val followState = ObservableField(ad?.user?.isFollowing)
    val likeCount = ObservableField(ad?.likesCount)
    val commentsCount = ObservableField(ad?.commentsCount)
    val likeState = ObservableField(ad?.isLiked)
    val fixed = ObservableField(ad?.isFixed)
    val selectedPhoto = ObservableField(0)
    val etComment = ObservableField("")
    val relatedAdsState = ObservableField(false)
    val userAdsState = ObservableField(false)

    var cameFromChat = false

    fun onImageClickedListener(model: Photo, i: Int) {
        selectedPhoto.set(i)
        model.orgUrl
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {
        isLoading.postValue(true)
        if (ad?.user?._id  == Helper.userId){
            repository.getMyAdDetail(adId){ ad ->
                isLoading.postValue(false)
                if (ad!=null){
                    adRes.postValue(ad)
                    incrementViews()
                }else{
                    notFoundAds.postValue(true)
                }
            }
        }else{
        repository.getAd(adId) { ad ->
            isLoading.postValue(false)
            if (ad!=null){
            adRes.postValue(ad)
            incrementViews()
            }else{
              notFoundAds.postValue(true)
            }
        }
        }
        loadComments()
        loadUserAds()
        loadRelatedAds()
    }

    private fun incrementViews() {
        val ad = ad ?: return
        if (ad.user?._id == Helper.userId) return
        repository.viewAd(ad._id) { success, _ ->
            if (success) {
                ad.viewsCount?.let {
                    viewCount.set(it + 1)
                }

            }
        }
    }

    private fun loadRelatedAds() {
        val ad = this.ad ?: return
        repository.getRelatedAds(ad._id) {
            it?.data?.let { response ->
                Log.e("ads_count_related", it.data.ads.size.toString())
                if (response.ads.isNotEmpty()) {

                    val newList = ArrayList<Ad>()
                    response.ads.forEachIndexed { index, ad ->
                        if (index < 3) newList.add(ad)
                    }
                    relatedAdsRes.postValue(newList)
                    relatedAdsState.set(true)
                }
            }
        }
    }

    private fun loadUserAds() {
        val ad = this.ad?.user ?: return
        repository.getUserAds(ad._id, 1) {
            it?.data?.let { response ->

                Log.e("ads_count_user", it.data.ads.size.toString())
                if (response.ads.isNotEmpty()) {
                    val newList = ArrayList<Ad>()
                    response.ads.forEachIndexed { index, ad ->
                        if (index < 3) newList.add(ad)
                    }
                    userAdsRes.postValue(newList)
                    userAdsState.set(true)
                }
            }
        }
    }

    fun onCallPhoneClickedListener(v: View) {

        val phone = ad?.user?.mobile ?: return
        Helper.callPhone(phone, v.context)

    }

    fun onWhatsappClickedListener(v: View) {
        val mobile = this.ad?.user?.mobile ?: return
        val url = "https://wa.me/${mobile}"
        Helper.openUrl(url, v.context)
    }

    fun onChatClickedListener(v: View) {
        if (cameFromChat) {
            (v.context as AppCompatActivity).onBackPressed()
            return
        }

        val ad = this.ad ?: return
        isLoading.postValue(true)
        repository.newChannel(ad._id, null) { channel, message ->
            isLoading.postValue(false)
            if (channel == null) {
                showToast.postValue(message)
                return@newChannel
            }

            ChatFragment.newInstance(v.context, channel, ad.user?.mobile, true)
        }
    }

    fun onToggleFollowClickedListener() {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return
        val user = ad.user ?: return
        isLoading.postValue(true)

        if (user.isFollowing) {
            repository.unFollowUser(user._id) { success, message ->
                if (success) {
                    this.ad?.user?.isFollowing = false
                    followState.set(false)
                }
                showToast.postValue(message)
                isLoading.postValue(false)
            }
        } else {
            repository.followUser(user._id) { success, message ->
                if (success) {
                    this.ad?.user?.isFollowing = true
                    followState.set(true)
                }
                showToast.postValue(message)
                isLoading.postValue(false)
            }
        }
    }

    fun onToggleLikeClickedListener() {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return
        isLoading.postValue(true)

        repository.toggleLike(ad._id) { success, message ->
            if (success) {
                if (ad.isLiked == true) ad.likesCount = ad.likesCount!! - 1 else ad.likesCount = ad.likesCount!! + 1
                ad.isLiked = !ad.isLiked!!
                likeCount.set(ad.likesCount)
                likeState.set(ad.isLiked)
            }
            showToast.postValue(message)
            isLoading.postValue(false)
        }
    }

    fun onToggleFavClickedListener() {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return
        isLoading.postValue(true)
        repository.toggleFavoriteAd(ad._id) { success, message ->
            if (success) {
                ad.isFavourite = !ad.isFavourite!!
                favState.set(ad?.isFavourite)
            }
            showToast.postValue(message)
            isLoading.postValue(false)
        }
    }

//    fun onShareClickedListener(v: View) {
//        val ad = ad ?: return
//        val shareBody =
//            "https://topsale.qa/ads/" + ad._id
//        val share = Intent.createChooser(Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, ad.title + "\n\nLink : " + shareBody)
//            putExtra(Intent.EXTRA_TITLE, ad.title)
//            putExtra(Intent.EXTRA_SUBJECT, ad.title)
//            type = "text/plain"
//        }, ad.title)
//
//        v.context.startActivity(share)
//    }

    fun shareImage(bitmap: Bitmap?, mActivity: Activity) {
//        if (bitmap != null) {
//            val uriToShare = getImageUri(mActivity, bitmap)
//            shareImageUri(uriToShare!!, mActivity)
//            shareImageWithLink(uriToShare!!, mActivity)
        shareLink(mActivity)
//        }
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing")
        return Uri.parse(path)
    }

    private fun saveImage(image: Bitmap, mActivity: Activity): Uri? {
        val imagesFolder: File = File(mActivity.getCacheDir(), "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", file)
        } catch (e: IOException) {
        }
        return uri
    }

    private fun shareImageUri(uri: Uri, context: Context) {
        val ad = ad ?: return
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        var shareMessage = "\nLet me recommend you this application\n\n"
        shareMessage = """
            ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
            
            
            """.trimIndent()
        val shareBody = "https://andrejgajdos.com/how-to-create-a-link-preview/"
//        val shareBody = context.getString(R.string.title_hint) + " " + ad.title +
//                "\n\nLink : " +
//                "\n\n" + context.getString(R.string.for_more_details) +"\n\n"+
//                "https://topsale.qa/ads/" + ad._id

        intent.putExtra(Intent.EXTRA_TEXT, shareBody)

//        intent.putExtra(Intent.EXTRA_STREAM, uri);

//        intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        val openInChooser = Intent.createChooser(intent, "Share Image!")
        context.startActivity(openInChooser)
    }

    private fun shareImageWithLink(uri: Uri, context: Context) {
        val ad = ad ?: return

        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, "https://topsale.qa/ads/")

//            putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
//            val shareBody = "https://andrejgajdos.com/how-to-create-a-link-preview/"
            val shareBody = "https://www.google.com"
            putExtra(Intent.EXTRA_TEXT, shareBody)

            // (Optional) Here we're passing a content URI to an image to be displayed
            data = uri
//            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)
        context.startActivity(share)
    }

    private fun shareLink(context: Context) {
        val ad = ad ?: return
//        val linkToShare ="Hi Hello every user \n\n"+"https://andrejgajdos.com/how-to-create-a-link-preview/"
        val linkToShare = "https://topsale.qa/ads/" + ad._id
        val targetedShareIntents: MutableList<Intent> = ArrayList()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, linkToShare)

        val resInfo: List<ResolveInfo> = context.getPackageManager().queryIntentActivities(shareIntent, 0)
        if (!resInfo.isEmpty()) {
            for (resolveInfo in resInfo) {
                val packageName = resolveInfo.activityInfo.packageName
                val targetedShareIntent = Intent(Intent.ACTION_SEND)
                targetedShareIntent.type = "text/plain"
//                targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject to be shared")
                if (TextUtils.equals(packageName, "com.facebook.katana")) {
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, linkToShare)
                } else {
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, linkToShare)
                }
                targetedShareIntent.setPackage(packageName)
                targetedShareIntents.add(targetedShareIntent)
            }
            val chooserIntent = Intent.createChooser(shareIntent, "Select app to share")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
            context.startActivity(chooserIntent)
        }
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageInQ(bitmap: Bitmap, v: View): Uri {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        var imageUri: Uri? = null
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = v.context.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(imageUri!!, contentValues, null, null)

        return imageUri!!
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    fun saveTheImageLegacyStyle(bitmap: Bitmap, v: View): Uri {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
        fos.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        fos.flush()
        fos.close()
        MediaScannerConnection.scanFile(v.context, arrayOf(image.absolutePath), null, null)
        return FileProvider.getUriForFile(v.context, "${v.context.packageName}.provider", image)
    }

    fun onRelateAdAllBtnClickedListener(v: View) {
        RelatedAdsActivity.newInstance(v.context, ad?.id ?: "")
    }

    fun onUserProfileClickedListener(v: View, activityLauncher: BetterActivityResult<Intent, ActivityResult>) {
        val user = ad?.user ?: return
        val intent: Intent = UserFragment.newInstance(v.context, user, ad, user._id)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                loadData()
            }
        }
    }

    fun onBlocUserClickedListener(v: View) {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return
        val user = ad.user ?: return
        isLoading.postValue(true)
        repository.blockUser(user._id) { success, message ->
            showToast.postValue(message)
            isLoading.postValue(false)
            if (success) {
                v.context.startActivity(Intent(v.context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }

    fun onReportAdClickedListener(text: String) {
        val ad = this.ad ?: return

        isLoading.postValue(true)
        repository.reportAd(ad._id, text) { _, message ->
            showToast.postValue(message)
            isLoading.postValue(false)

        }
    }

    fun onSendCommentClickedListener(v: View) {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return

        val comment = etComment.get() ?: ""
        if (comment.isEmpty()) {
            showToast.postValue(R.string.enter_comment)
            return
        }

        isLoading.postValue(true)
        repository.newComment(ad._id, comment) {
            isLoading.postValue(false)
            showToast.postValue(it?.message ?: "Network Error")
            it?.let {
                it.data?.let { newComment ->
                    loadComments()
                    etComment.set("")
                    ad.commentsCount = newComment.newCommentsCount
                    commentsCount.set(newComment.newCommentsCount)
                }
            }
        }
    }

    fun onMoreCommentsClickedListener(v: View) {
        val ad = ad ?: return
        CommentsFragment.newInstance(v.context, ad)
    }

// comment action

    fun loadComments() {
        val ad = this.ad ?: return
        repository.getRecentComments(ad._id) { response ->
            commentsRes.postValue(response)
        }
    }

    fun onDeleteCommentClickedListener(comment: Comment, v: View, result: () -> Unit) {
        isLoading.postValue(true)
        repository.deleteComment(comment._id) { success, message ->
            isLoading.postValue(false)
            showToast.postValue(message)
            if (success) {
                result()
            }
        }
    }

    fun onEditCommentClickedListener(comment: Comment, text: String, v: View, result: () -> Unit) {

        isLoading.postValue(true)
        repository.editComment(comment._id, text) { response ->
            val message = response?.message ?: "Error"
            val success = response?.success ?: false
            val updatedComment = response?.data?.comment

            isLoading.postValue(false)
            showToast.postValue(message)

            if (success) {
                if (updatedComment != null) {
                    result()
                } else {
                    loadComments()
                }
            }
        }

    }

    fun onBlocCommentClickedListener(comment: Comment, v: View, result: () -> Unit) {
        if (!isLogin) {
            snackLogin.postValue(true)
            return
        }

        val ad = this.ad ?: return
        val adUser = ad.user ?: return
        val user = comment.user ?: return
        val isAuthor = user._id == adUser._id

        isLoading.postValue(true)
        repository.blockUser(user._id) { success, message ->
            showToast.postValue(message)
            isLoading.postValue(false)
            if (success) {
                result()
            }
        }
    }

    fun onProfileCommentClickedListener(comment: Comment, v: View, result: () -> Unit) {
        val user = comment.user ?: return
        UserFragment.newInstance_(v.context, user, null, user._id)
    }

    fun onCallCommentClickedListener(comment: Comment, v: View, result: () -> Unit) {
        val phone = comment.user?.mobile ?: return
        Helper.callPhone(phone, v.context)
    }

    fun onChatCommentClickedListener(comment: Comment, v: View, result: () -> Unit) {
        val context = v.context ?: return

        isLoading.postValue(true)
        repository.newChannel(ad?._id, comment.user?._id) { channel, message ->
            isLoading.postValue(false)
            if (channel == null) {
                showToast.postValue(message)
                return@newChannel
            }

            ChatFragment.newInstance(context, channel, ad?.user?.mobile)
        }
    }

    fun onEmailCommentClickedListener(comment: Comment, v: View, result: () -> Unit) {
        val user = comment.user ?: return
        val email = user.email ?: return
        Helper.sendEmail(email, v.context)
    }

    fun initDeepLink(mActivity: Activity, withShare: Boolean) {
        if (ad == null) return

        val lp = LinkProperties().setChannel("facebook").setFeature("sharing")

        ad?.let {
            val buo = BranchUniversalObject().setCanonicalIdentifier("content/12345")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC).setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC).setContentMetadata(ContentMetadata().addCustomMetadata("ad_id", it.id))

            var title = ""
//            var title = "تمت مشاركة هذا الإعلان '${it.title}' معك من تطبيق توب سيل"
            title = title + "\n" + it.title
            buo.setTitle(title)
            buo.setContentDescription(it.details)

            if (it.adPhoto.isNotEmpty()) {
                buo.setContentImageUrl(it.adPhoto.get(0).orgUrl)
            }


            buo.generateShortUrl(mActivity, lp) { url: String, error: BranchError? ->
                if (error == null) {
                    if (withShare) {
                        shareLink(url, mActivity)
                    }
                    Log.i("MyApp", "got my Branch link to share: $url")
                }
            }

        }


    }

    fun shareLink(url: String, mActivity: Activity) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mActivity.getString(R.string.app_name))
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            mActivity.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }


}