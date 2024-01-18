package com.aelzohry.topsaleqatar.helper

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.aelzohry.topsaleqatar.App.Companion.context
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.Region
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.StanderModel1
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.MainActivity
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.*
import java.net.URLEncoder
import java.util.*
import kotlin.math.min


object Helper {

    private val userPreferences: SharedPreferences =
        context.getSharedPreferences("user", Context.MODE_PRIVATE)

    private val langPreferences: SharedPreferences =
        context.getSharedPreferences("lang", Context.MODE_PRIVATE)

    private const val AUTH_TOKEN_KEY = "AUTH_TOKEN"
    private const val USER_ID_KEY = "USER_ID"
    private const val MOBILE_KEY = "MOBILE"
    private const val PUSH_USER_ID_KEY = "PUSH_USER_ID"
    private const val DID_SEND_PUSH_ID_TO_SERVER = "DID_SEND_PUSH_ID_TO_SERVER"
    private const val FIRST_RUN_KEY = "FIRST_RUN"
    private const val NIGHT_MODE_KEY = "NIGHT_MODE_KEY"
    private const val LAT_KEY = "LAT_KEY"
    private const val LNG_KEY = "LNG_KEY"
    private const val CATEGORY_LIST = "CATEGORY_LIST"
    private const val REGOIN_LIST = "REGOIN_LIST"
    private const val CITY_LIST = "CITY_LIST"
    private const val CAREMAKE_LIST = "CAREMAKE_LIST"
    private const val CAR_MODEL_LIST = "CAR_MODEL_LIST"

    fun logout() {
        authToken = null
        userId = null
        didSendPushIdToServer = false
        SocketHelper.disconnect()
    }

    var isFirstRun: Boolean
        get() = userPreferences.getBoolean(FIRST_RUN_KEY, true)
        set(value) = userPreferences.edit().putBoolean(FIRST_RUN_KEY, value).apply()

    fun setCategoryList(categories: ArrayList<Category>) {
        val gson = Gson()
        val json = gson.toJson(categories)
        userPreferences.edit().putString(CATEGORY_LIST, json).apply()
    }
    fun setCityList(list: ArrayList<Region>) {
        val gson = Gson()
        val json = gson.toJson(list)
        userPreferences.edit().putString(CITY_LIST, json).apply()
    }
    fun setRegionList(list: ArrayList<LocalStanderModel>) {
        val gson = Gson()
        val json = gson.toJson(list)
        userPreferences.edit().putString(REGOIN_LIST, json).apply()
    }
    fun setCarMakeList(list: ArrayList<StanderModel1>) {
        val gson = Gson()
        val json = gson.toJson(list)
        userPreferences.edit().putString(CAREMAKE_LIST, json).apply()
    }
    fun setCarModelList(list: ArrayList<StanderModel1>) {
        val gson = Gson()
        val json = gson.toJson(list)
        userPreferences.edit().putString(CAR_MODEL_LIST, json).apply()
    }
    fun getCityList() : ArrayList<Region>{
        val gson = Gson()
        val json: String? = userPreferences.getString(CITY_LIST, "")
        val type = object : TypeToken<List<Region>>() {}.type
        val list: ArrayList<Region>? = gson.fromJson<ArrayList<Region>>(json, type)
        return list ?: ArrayList<Region>()
    }
    fun getRegionList(): ArrayList<LocalStanderModel> {
        val gson = Gson()
        val json: String? = userPreferences.getString(REGOIN_LIST, "")
        val type = object : TypeToken<List<LocalStanderModel>>() {}.type
        val list: ArrayList<LocalStanderModel>? = gson.fromJson<ArrayList<LocalStanderModel>>(json, type)
        return  list ?: ArrayList<LocalStanderModel>()
    }
    fun getCarMakeList() : ArrayList<StanderModel1>{
        val gson = Gson()
        val json: String? = userPreferences.getString(CAREMAKE_LIST, "")
        val type = object : TypeToken<List<StanderModel1>>() {}.type
        val list: ArrayList<StanderModel1>? = gson.fromJson<ArrayList<StanderModel1>>(json, type)
        return  list ?: ArrayList<StanderModel1>()
    }
    fun getCarModelList() : ArrayList<StanderModel1>{
        val gson = Gson()
        val json: String? = userPreferences.getString(CAR_MODEL_LIST, "")
        val type = object : TypeToken<List<StanderModel1>>() {}.type
        val list: ArrayList<StanderModel1>? = gson.fromJson<ArrayList<StanderModel1>>(json, type)
        return  list ?: ArrayList<StanderModel1>()
    }



    var catList = ArrayList<Category>()

    fun getCategoryList(): ArrayList<Category> {
        return catList
    }
    fun populateCategoryList(){
        val gson = Gson()
        val json: String? = userPreferences.getString(CATEGORY_LIST, "")
        val type = object : TypeToken<List<Category>>() {}.type
        val list: ArrayList<Category>? = gson.fromJson<ArrayList<Category>>(json, type)
        catList =  list ?: ArrayList<Category>()
    }

    var userId: String?
        get() = userPreferences.getString(USER_ID_KEY, null)
        set(value) = userPreferences.edit().putString(USER_ID_KEY, value).apply()

    var mobile: String?
        get() = userPreferences.getString(MOBILE_KEY, null)
        set(value) = userPreferences.edit().putString(MOBILE_KEY, value).apply()

    var authToken: String?
        get() = userPreferences.getString(AUTH_TOKEN_KEY, null)
        set(value) = userPreferences.edit().putString(AUTH_TOKEN_KEY, value).apply()

    val isAuthenticated: Boolean
        get() = authToken != null

    var pushId: String?
        get() = userPreferences.getString(PUSH_USER_ID_KEY, null)
        set(value) = userPreferences.edit().putString(PUSH_USER_ID_KEY, value).apply()

    var isNightMode: Boolean
        get() = userPreferences.getBoolean(NIGHT_MODE_KEY, false)
        set(value) = userPreferences.edit().putBoolean(NIGHT_MODE_KEY, value).apply()

    var lat: Double
        get() = userPreferences.getString(LAT_KEY, "0.0")?.toDouble() ?: 0.0
        set(value) = userPreferences.edit().putString(LAT_KEY, value.toString()).apply()

    var lng: Double
        get() = userPreferences.getString(LNG_KEY, "0.0")?.toDouble() ?: 0.0
        set(value) = userPreferences.edit().putString(LNG_KEY, value.toString()).apply()

    var didSendPushIdToServer: Boolean
        get() = userPreferences.getBoolean(DID_SEND_PUSH_ID_TO_SERVER, false)
        set(value) = userPreferences.edit().putBoolean(DID_SEND_PUSH_ID_TO_SERVER, value).apply()

    val isEnglish: Boolean
        get() = language == AppLanguage.English

    val language: AppLanguage
        get() = if (languageCode == "en")
            AppLanguage.English
        else
            AppLanguage.Arabic

    val languageCode: String
        get() = langPreferences.getString("lang", "en") ?: "en"

    fun setLanguage(language: AppLanguage) {
        val pref = langPreferences.edit()
        val languageCode = if (language == AppLanguage.English) "en" else "ar"
        pref.putString("lang", languageCode)
        pref.apply()
    }

    fun restartApp(context: Context) {
        // restart app
        context.startActivity(
            Intent(
                context,
                MainActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    fun showToast(context: Context?, message: String, length: Int = Toast.LENGTH_LONG) {
        val toast = Toast.makeText(context, message, length)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.show()
    }

    fun openUrl(url: String, context: Context) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        context.startActivity(openURL)
    }


    fun copyNumber(phone: String, context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(
            "Phone",
            phone + ""
        )
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.copied_to_clipborad), Toast.LENGTH_SHORT)
            .show()
    }


    fun sendSMS(phone: String?, context: Context) {
        val uri = Uri.parse("smsto:${phone}")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        context.startActivity(intent)
    }


    fun newWhatsAppIntent(mActivity: Activity, pm: PackageManager?, text: String) {
        if (TextUtils.isEmpty(text)) return

        val whatsappNumber = "+974" + text
        val i = Intent(Intent.ACTION_VIEW)
        var url = ""
        url = try {
            "https://api.whatsapp.com/send?phone=" + whatsappNumber + "&text=" + URLEncoder.encode(
                "",
                "UTF-8"
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            "https://api.whatsapp.com/send?phone=$whatsappNumber"
        }
        try {
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if (i.resolveActivity(pm!!) != null) {
                mActivity.startActivity(i)
            } else {
                openInWebsite(mActivity, url)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            openInWebsite(mActivity, url)
        }

    }


    fun openInWebsite(mActivity: Activity, link: String) {
        var link = link
        if (TextUtils.isEmpty(link)) return
        if (!link.startsWith("https://") && !link.startsWith("http://")) {
            link = "http://$link"
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        try {
            mActivity.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
        }
    }


    fun callPhone(phone: String, context: Context) {
        Dexter.withContext(context)
            .withPermissions(Manifest.permission.CALL_PHONE)
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        callIntent.data = Uri.parse("tel:$phone")
                        context.startActivity(callIntent)
                    }

                    if (report?.isAnyPermissionPermanentlyDenied == true)
                        showSettingsDialog(context, context.getString(R.string.call_permission))

                }


                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    fun sendEmail(email: String?, context: Context) {
        if (TextUtils.isEmpty(email)) return
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        i.putExtra(Intent.EXTRA_SUBJECT, "")
        i.putExtra(Intent.EXTRA_TEXT, "")
        context.startActivity(Intent.createChooser(i, "Send email from Top Sale App..."))
    }

    fun showSettingsDialog(context: Context, permissionsString: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.need_permissions))
        builder.setMessage(
            context.getString(
                R.string.permission_settings_message,
                permissionsString
            )
        )
        builder.setPositiveButton(context.getString(R.string.go_to_settings)) { dialog, _ ->
            dialog.dismiss()
            openSettings(context)
        }
        builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.create().show()
    }

    // navigating user to app settings
    private fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun getImagePath(uri: Uri, context: Context?): String? {
        var filePath: String? = null
        uri.authority?.let {
            try {
                context?.contentResolver?.openInputStream(uri).use {
                    val photoFile: File? = createTemporalFileFrom(it)
                    filePath = photoFile?.path
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return filePath

//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor =
//            context?.contentResolver?.query(uri, projection, null, null, null) ?: return null
//        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        val s: String = cursor.getString(columnIndex)
//        cursor.close()
//
//        return s
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(inputStream: InputStream?): File? {
        var targetFile: File? = null
        return if (inputStream == null) targetFile
        else {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            targetFile = createTemporalFile()
            FileOutputStream(targetFile).use { out ->
                while (inputStream.read(buffer).also { read = it } != -1) {
                    out.write(buffer, 0, read)
                }
                out.flush()
            }
            targetFile
        }
    }

    private fun createTemporalFile(): File {
        val folder = Environment.getExternalStorageDirectory().absolutePath +
                "/topsale/ads/";
        val file = File(folder, "${UUID.randomUUID()}.jpeg")
        file.parentFile?.mkdirs()
        return file
        //File(getDefaultDirectory(), "tempPicture.jpg")
    }

    fun resizeBitmapImage(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap? {
        var resultImage = image
        val width = resultImage.width
        val height = resultImage.height

        if (maxHeight > 0 && maxWidth > 0 && (maxWidth < width || maxHeight < height)) {
            val maxWidth = min(maxWidth, width)
            val maxHeight = min(maxHeight, height)

            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            resultImage = Bitmap.createScaledBitmap(resultImage, finalWidth, finalHeight, true)
            return resultImage
        }

        return resultImage
    }

    fun setPushUserId(id: String) {
        if (this.pushId != id) {
            this.pushId = id
            didSendPushIdToServer = false
        }
        sendPushIdIfFound()
    }

    private fun sendPushIdIfFound() {
        if (!isAuthenticated) return
        if (didSendPushIdToServer) return
        val id = pushId ?: return

        val repository = RemoteRepository()
        repository.registerPushToken(id) { success ->
            if (success) {
                this.didSendPushIdToServer = true
            }
            Log.i("Helper", "registerPushToken: $success")
        }
    }


    fun goToMapDirection(
        mActivity: Activity,
        latitude: String,
        longitude: String,
        address: String
    ) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$latitude,$longitude($address)?z=12")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            mActivity.startActivity(mapIntent)
        } catch (ex: Exception) {
        }
    }


    fun getUnSeenNotificationNumber(repository: Repository, listener: Listener) {
        repository.getNumberOfNotificationsNotSeen {
            it?.data?.let { data ->
                listener.passUnSeenNotificationNumber(data.NumberOfNotificationsNotSeen,data.NumberOfMessagesNotSeen)
            }
        }
    }

    fun getAddress(latLng: LatLng, context: Context): String {
        var result: String = context.getString(R.string.unknown_address)

        try {
            val address = Geocoder(context, Locale.getDefault()).getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )!![0]


            val subThoroughfare: String = address.getSubThoroughfare()
            val thoroughfare: String = address.getThoroughfare()
            val adminArea: String = address.getAdminArea()
            val city: String = address.getLocality()
            val postalCode: String = address.getPostalCode()
            val addressText: String = address.getAddressLine(0)

            val list = ArrayList<String?>()

            if (!TextUtils.isEmpty(subThoroughfare)) list.add(subThoroughfare)
            if (!TextUtils.isEmpty(thoroughfare)) list.add(thoroughfare)
            if (!TextUtils.isEmpty(postalCode)) list.add(postalCode)
            if (!TextUtils.isEmpty(city)) list.add(city)
            if (!TextUtils.isEmpty(adminArea)) list.add(adminArea)

            if (!list.isEmpty()){
                result = TextUtils.join(", ", list)
            }else{
                if(!TextUtils.isEmpty(addressText))
                    result = addressText
            }


            return result
        } catch (ex: Exception) {
            return result

        }
    }



    interface Listener {
        fun passUnSeenNotificationNumber(notificationNumber: Int,messagesNumber: Int)
    }

}

enum class AppLanguage {
    Arabic, English
}

enum class AppMode(val value: String) { NIGHT("night"), LIGHT("light") }