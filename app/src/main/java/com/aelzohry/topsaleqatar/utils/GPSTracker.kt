package com.aelzohry.topsaleqatar.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.helper.Helper
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.onesignal.BuildConfig


class GPSTracker(
    private val activity: Context,
    private val callback: (Location?, err: String?) -> Unit
) : LifecycleObserver,
    LocationListener {

    interface Listener {
        fun onLocationFound(location: Location)
    }

    var mLocationManager: LocationManager? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mLocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isAccessFineLocationGranted({
            picLocation()
        }, {

            Toast.makeText(
                activity,
                "Location permission not granted",
                Toast.LENGTH_LONG
            ).show()
        })

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // disconnect if connected
        mLocationManager = null
    }

    private fun getLastKnownLocation(): Location? {


        val providers: List<String> = mLocationManager!!.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null
            }
            val l: Location = mLocationManager!!.getLastKnownLocation(provider)
                ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        return bestLocation
    }

    @SuppressLint("MissingPermission")
    private fun picLocation() {


        val gpsEnable = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnable = mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!gpsEnable && !networkEnable) {
            showGPSNotEnabledDialog()
            return
        }

        try {

            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000 * 60 * 1,
                10.toFloat(),
                this
            )

            val location = getLastKnownLocation()
            Helper.lat = location?.latitude ?: 0.0
            Helper.lng = location?.longitude ?: 0.0
            if (BuildConfig.DEBUG)
                Log.e(
                    "test_location",
                    "lat is : ${location?.latitude} , lng : ${location?.longitude}"
                )
            if (location != null) {
                callback(location, null)
            } else {
                callback(null, "location error")
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000 * 1,
                    10.toFloat(),
                    this
                )
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000 * 1,
                    10.toFloat(),
                    this
                )
            }


        } catch (ex: Exception) {

            ex.message?.let {
                if (BuildConfig.DEBUG)
                    Log.e("test_location", it)
            }

        }
    }

    private fun isAccessFineLocationGranted(
        generated: () -> Unit,
        notGenerated: () -> Unit
    ) {
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) generated()

                    if (report?.isAnyPermissionPermanentlyDenied == true) notGenerated()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }


    private fun showGPSNotEnabledDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Enable Location")
            .setMessage("Required location for this application")
            .setCancelable(false)
            .setPositiveButton("Enable now") { _, _ ->
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

//        when (requestCode) {
//            Constants.REQUEST_FINE_LOCATION_PERMISSION -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    onStart()
//                } else {
//                    Toast.makeText(
//                        content,
//                        "Location permission not granted",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
    }

    override fun onLocationChanged(p0: Location) {

        p0?.let {
            if (BuildConfig.DEBUG)
                Log.e("test_location", "lat is : ${p0.latitude} , lng : ${p0.longitude}")
            Helper.lat = p0.latitude
            Helper.lng = p0.longitude


            callback(p0, null)

        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }
}