package com.aelzohry.topsaleqatar.ui.selectLocationDialog.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentAdsLocationDialogBinding
import com.aelzohry.topsaleqatar.ui.selectLocationDialog.viewModel.SelectLocationViewModel
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.MapHelper
import com.aelzohry.topsaleqatar.utils.WorkaroundMapFragment
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng

class AdsLocationDialogFragment : BaseBottomSheet<FragmentAdsLocationDialogBinding, SelectLocationViewModel>(), OnMapReadyCallback {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var currentLocation: GPSTracker
    private var selectedLocation: LatLng? = null
    private lateinit var map: GoogleMap

    private var canRedrawCircle = true

    private lateinit var listener: LocationListener

    companion object {
        var LOCATION_PERMISSION_ID = 101
        var ENABLE_GPS_REQUEST_CODE = 102
        fun newInstance(lat: Double, lng: Double): AdsLocationDialogFragment {
            val fragment = AdsLocationDialogFragment()
            val args = Bundle()
            args.putDouble("lat", lat)
            args.putDouble("lng", lng)
            fragment.arguments = args
            return fragment
        }
    }

    private fun getIntentData() {
        arguments?.let {
            selectedLocation = LatLng(it.getDouble("lat", 0.0), it.getDouble("lng", 0.0))
        }
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_ads_location_dialog
    }

    override fun getViewModel(): SelectLocationViewModel {
        return ViewModelProvider(this).get(SelectLocationViewModel::class.java)
    }

    override fun setupUI() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        initMapListener()
        getIntentData()
    }

    override fun onClickedListener() {
        binding.backBtn.setOnClickListener { v: View? -> dismiss() }
        binding.btnAdLocation.setOnClickListener {
            drawCircleLocation()
        }
        binding.btnMyLocation.setOnClickListener {
            getCurrentLocation()
        }


    }

    override fun observerLiveData() {}

    fun initMapListener() {
        if (activity == null) return
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_ID)
            return
        }
        mapFragment.getMapAsync(this)
    }

    override fun isFullHeight(): Boolean {
        return true
    }


    fun getCurrentLocation() {
        currentLocation = GPSTracker(requireActivity()) { location, err ->

            if (err != null) {
//                Toast.makeText(this, this.getString(R.string.location_err), Toast.LENGTH_LONG).show()
            } else {
                changeLocation(LatLng(location!!.latitude, location.longitude))
                lifecycle.removeObserver(currentLocation)
            }

        }
        lifecycle.addObserver(currentLocation)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        if (!::map.isInitialized) return
        map.isMyLocationEnabled = true

        drawCircleLocation()
    }

    private fun changeLocation(address: LatLng) {
        map.apply {
            animateCamera(CameraUpdateFactory.newLatLngZoom(address, 15f))
        }
        lifecycle.removeObserver(currentLocation)
    }

    private fun drawCircleLocation() {
        if (!::map.isInitialized) return

        canRedrawCircle = false
        context?.let {
            selectedLocation?.let { it1 ->
                val circle = MapHelper.drawCircle(it, map, it1, 500.0)




                map.animateCamera(CameraUpdateFactory.newLatLngZoom(circle.getCenter(), getZoomLevel(circle).toFloat()))
            }
        }

    }

    fun getZoomLevel(circle: Circle): Int {
        var zoomLevel = 11
        val radius: Double = circle.getRadius() + circle.getRadius() / 2
        val scale = radius / 400
        zoomLevel = (16 - Math.log(scale) / Math.log(2.0)).toInt()
        return zoomLevel
    }

    interface LocationListener {
        fun onLocationSelected(location: LatLng?, distance: Int)
    }

    fun setListener(listener: LocationListener) {
        this.listener = listener
    }

}