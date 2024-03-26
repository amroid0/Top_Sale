package com.aelzohry.topsaleqatar.ui.selectLocationDialog.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentSelectLocationDialogBinding
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.ui.mapAndSearchList.MapAndSearchListFragment
import com.aelzohry.topsaleqatar.ui.selectLocationDialog.viewModel.SelectLocationViewModel
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.MapHelper
import com.aelzohry.topsaleqatar.utils.WorkaroundMapFragment
import com.aelzohry.topsaleqatar.utils.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton


class SelectLocationDialogFragment : BaseFragment<FragmentSelectLocationDialogBinding, SelectLocationViewModel>(),
    OnMapReadyCallback {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var currentLocation: GPSTracker
    private var selectedLocation: LatLng? = null
    private lateinit var map: GoogleMap

    //    private var currentDistanceValue = 3
    private var canRedrawCircle = true
    private var canMakeZoom = true
    private var canChangeProgress = true
    private var currentProgress = 1

    private lateinit var listener: LocationListener
    private var firstLoad = true


    companion object {
        var LOCATION_PERMISSION_ID = 101
        var ENABLE_GPS_REQUEST_CODE = 102

        fun newInstance(): SelectLocationDialogFragment {
            val fragment = SelectLocationDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutID(): Int {
        return R.layout.fragment_select_location_dialog
    }

    override fun getViewModel(): SelectLocationViewModel {
        return ViewModelProvider(this).get(SelectLocationViewModel::class.java)
    }

    override fun setupUI() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        initMapListener()
        onClickedListener()
    }

    override fun onClickedListener() {
//        binding.backBtn.setOnClickListener { v: View? -> dismiss() }

        binding.dynamicSeekbar.setSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, isSeek: Boolean) {

                if (progress <= 6) {
                    // this to avoid show zero :)
                    currentProgress = 1
                } else if (progress <= 120) {
                    // make every 6 step as one step (1-20)
                    val result = Math.ceil((progress / 6).toDouble())
                    currentProgress = result.toInt()
                } else {
                    // get steps for other value 21-200
                    val result: Double = 2.26576 * progress - 252.943
                    currentProgress = result.toInt()
                }

                binding.dynamicSeekbar.setInfoText(
                    String.format(getString(R.string.km_f), currentProgress),
                    progress
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

//                currentDistanceValue = seekBar.progress
                canMakeZoom =true
                reDrawCircle()
            }

        })

//        binding.dynamicSeekbar.incrementProgressBy(20);
        binding.dynamicSeekbar.setProgress(1)

        binding.btnApply.setOnClickListener {
            if (::listener.isInitialized) {
                if (selectedLocation != null) {
                    listener.onLocationSelected(selectedLocation, currentProgress)
//                    dismiss()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            if (::listener.isInitialized) {
              listener.onBackClick()
            }
        }
    }

    override fun observerLiveData() {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentFragmentView = requireParentFragment().view

        val parentButton = parentFragmentView?.findViewById<MaterialButton>(R.id.btn_apply)
        parentButton?.visibility = View.GONE
    }
    fun initMapListener() {
        if (activity == null) return
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
         requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_ID
            )
            return
        }
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             mapFragment.getMapAsync(this)
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }
    }



    fun getCurrentLocation() {
        changeLocation(
        Constants.QATAT_LOCATION)
        /*currentLocation = GPSTracker(requireActivity()) { location, err ->

            if (err != null) {
//                Toast.makeText(this, this.getString(R.string.location_err), Toast.LENGTH_LONG).show()
                changeLocation(LatLng(location!!.latitude, location.longitude))

            } else {
                changeLocation(LatLng(location!!.latitude, location.longitude))
                lifecycle.removeObserver(currentLocation)
            }

        }
        lifecycle.addObserver(currentLocation)*/
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        if (!::map.isInitialized) return
        map.isMyLocationEnabled = true

        getCurrentLocation()


        map.setOnCameraIdleListener {
//            vm.lat = map.cameraPosition.target.latitude
//            vm.lng = map.cameraPosition.target.longitude
//            getAddress(map.cameraPosition.target)

            selectedLocation =
                if (firstLoad) Constants.QATAT_LOCATION else map.cameraPosition.target
            if (firstLoad) {
                firstLoad = false
            }
            Log.e("test_idle", canMakeZoom.toString())
            if (canRedrawCircle) {
                reDrawCircle()
            } else {
                canRedrawCircle = true
            }

        }


        map.setOnCameraMoveStartedListener {

            val bottomSheetFragment: MapAndSearchListFragment? =
                parentFragment as MapAndSearchListFragment?
            when (it) {
                GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                    //handle this state
                    (mapFragment as WorkaroundMapFragment).setScrollable(false);

                    canMakeZoom = false
                    Log.e("test_reason", canMakeZoom.toString())

                }

            }
            Log.e("test_reason", canMakeZoom.toString() + " :: " + it)

        }
        val bottomSheetFragment: MapAndSearchListFragment? =
            parentFragment as MapAndSearchListFragment?
        mapFragment.view?.setOnTouchListener { v, event ->

            when (event.getAction()) {
                MotionEvent.ACTION_DOWN ->
                    bottomSheetFragment?.dialog?.setCanceledOnTouchOutside(false)

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->             // Enable collapsing when touch interaction ends
                    bottomSheetFragment?.dialog?.setCanceledOnTouchOutside(true)
            }
            false
        }

    }

    private fun changeLocation(address: LatLng) {

//        vm.lat = address.latitude
//        vm.lng = address.longitude


        map.apply {
            // just a random location our map will point to when its launched
//            addMarker(MarkerOptions().apply {
//                position(address)
//                title("")
//                draggable(false)
//            })
            // setup zoom level
            selectedLocation = if (firstLoad)Constants.QATAT_LOCATION else address
            if (firstLoad){
                firstLoad = false
            }
            canMakeZoom = true
            reDrawCircle()
//            canRedrawCircle =false
//            animateCamera(CameraUpdateFactory.newLatLngZoom(address, 15f))

        }

//        getAddress(address)

    }


    private fun reDrawCircle() {
//        canRedrawCircle = false
        context?.let {
            selectedLocation?.let { it1 ->
                val circle = MapHelper.drawCircle(
                    it,
                    map,
                    it1,
                    currentProgress * 1000.0
                )



                if (canMakeZoom) {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            circle.getCenter(),
                            if (selectedLocation == Constants.QATAT_LOCATION) 9.toFloat() else getZoomLevel(
                                circle
                            ).toFloat()
                        )
                    )
                }


            }
        }


        canMakeZoom = false

    }


    fun getZoomLevel(circle: Circle): Int {
        var zoomLevel = 11
        val radius: Double = circle.getRadius() + circle.getRadius() / 2
        val scale = radius / 200
        zoomLevel = (16 - Math.log(scale) / Math.log(2.0)).toInt()
        return zoomLevel
    }


    interface LocationListener {
        fun onLocationSelected(location: LatLng?, distance: Int)
        fun onBackClick()
    }

    fun setListener(listener: LocationListener) {
        this.listener = listener
    }

}