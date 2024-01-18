package com.aelzohry.topsaleqatar

import android.app.Application
import android.content.Context
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.utils.enumClasses.CarColor
import com.aelzohry.topsaleqatar.utils.enumClasses.EngineSize
import com.aelzohry.topsaleqatar.utils.enumClasses.Kilometer
import com.google.android.libraries.places.api.Places
import com.onesignal.OneSignal
import io.branch.referral.Branch
import java.util.*

class App : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()

        if (BuildConfig.DEBUG)
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)


        // Branch logging for debugging

        // Branch logging for debugging
        Branch.enableLogging()
        // Branch object initialization
        // Branch object initialization
        Branch.getAutoInstance(this)

        Places.initialize(this, "AIzaSyBfxQvogOA3VXhik1abMCGy98oPn_yDn98")
//        Places.initialize(instance, "AIzaSyDA9AYnlXQ5ysrifNFT6MnaVSYl-rJQWcI")
        CarColor.populateList()
        CarColor.populateYears()
        Kilometer.populateList()
        EngineSize.populate()
        Helper.populateCategoryList()


    }

    companion object {
        private var instance: App? = null

        // or return instance.getApplicationContext();
        val context: Context
            get() = instance!!
    }
}