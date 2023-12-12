package com.aelzohry.topsaleqatar.ui.new_ad.adLocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.App
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityAdLocationBinding
import com.aelzohry.topsaleqatar.helper.Constants.QATAT_LOCATION
import com.aelzohry.topsaleqatar.helper.Helper.isEnglish
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.PolyUtil
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlPolygon
import java.util.*

class AdLocationActivity : BaseActivity<ActivityAdLocationBinding, AdLocationViewModel>(), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment

    private var map: GoogleMap? = null
    private lateinit var currentLocation: GPSTracker

    var LOCATION_PERMISSION_ID = 101

    private var canSelectLocation = true
    private var firstZoom = true
    private var canDoCameraIdle = true
    private var zoomLevel = 15f
    private val shapeCoordinates = ArrayList<LatLng>()

    override fun getLayoutID(): Int = R.layout.activity_ad_location

    override fun getViewModel(): AdLocationViewModel = ViewModelProvider(this)[AdLocationViewModel::class.java]

    override fun setupUI() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        initMapListener()
    }

    override fun onClickedListener() {
        binding.imgClose.setOnClickListener { onBackPressed() }
        binding.rvLocation.setOnClickListener { openAutoCompleteScreen() }

    }

    override fun observerLiveData() {

    }

    fun getCurrentLocation() {
        currentLocation = GPSTracker(this) { location, err ->

            if (err == null && location != null) {
                val myLocation = LatLng(location.latitude, location.longitude)
                canSelectLocation = checkLatLngIsInsideQatar(myLocation)
                zoomAndShowTextLocation(myLocation)
                lifecycle.removeObserver(currentLocation)
            }

        }
        lifecycle.addObserver(currentLocation)
    }

    fun initMapListener() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_ID)
            return
        }
        mapFragment.getMapAsync(this)
    }

    private fun openAutoCompleteScreen() {
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
        intent.setCountries(Arrays.asList("QA"))
        activityLauncher.launch(intent.build(this)) { result ->
            if (result.resultCode === RESULT_OK) {
                if (result.data != null) {
                    val place = Autocomplete.getPlaceFromIntent(result.data)
                    place.let {
                        binding.tvSearch.text = it.address
                        zoomLevel = 13f
                        firstZoom = true
                        zoomAndShowTextLocation(it.latLng as LatLng)
                    }
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return
        this.map = map
        map.isMyLocationEnabled = true

        // zoom to qatar by default
        map.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(QATAT_LOCATION, 9f))
            getAddress(QATAT_LOCATION)
        }

        loadKmlFile()

        // start get current location and zoom to it
        getCurrentLocation()

        map.setOnCameraIdleListener {

            if (canDoCameraIdle) {
                canSelectLocation = checkLatLngIsInsideQatar(map.cameraPosition.target)
                zoomAndShowTextLocation(map.cameraPosition.target)
            } else {
                canDoCameraIdle = true
            }

        }
    }

    private fun loadKmlFile() {
        val layer = KmlLayer(map, R.raw.qatar_areas, this)
        layer.addLayerToMap()

        // Iterate through the KML layer containers
        // Iterate through the KML layer containers
        val containers = layer.containers
        Log.e("test_map_size", "containers size ${containers.count()}")

        for (container in containers) {
            // Iterate through the KML placemarks
            val placeMarks = container.placemarks
            Log.e("test_map_size", "placemarks size ${placeMarks.count()}")

            for (placemark in placeMarks) {
                val placeMarkName = placemark.getProperty("name")
                val placeMarkDesc = placemark.getProperty("description")
                Log.e("test_map_size", "$placeMarkName $placeMarkDesc")

                // Extract the coordinates from the placemark
                val polygon = placemark.geometry as KmlPolygon
                val coordinates = polygon.outerBoundaryCoordinates

                // Process the coordinates as needed
                for (coordinate in coordinates) {
                    val latitude = coordinate.latitude
                    val longitude = coordinate.longitude
                    val latLng = LatLng(latitude, longitude)
                    shapeCoordinates.add(latLng)
                    Log.e("test_map", "latitude $latitude," + "longitude $longitude")
                    // Do something with the latitude and longitude values
                }

            }
        }
//        layer.removeLayerFromMap()
    }

    private fun checkCurrentLocation(location: LatLng) {
        map?.clear()
        val isLocationInsideShape: Boolean = PolyUtil.containsLocation(location, shapeCoordinates, false)
        if (isLocationInsideShape) {
            val polygonOptions = PolygonOptions()
            polygonOptions.addAll(shapeCoordinates)
            polygonOptions.fillColor(Color.parseColor("#6FFF0000"))
            polygonOptions.strokeColor(Color.RED) // Set the width of the polyline
            polygonOptions.strokeWidth(4f) // Set the width of the polyline
            map?.addPolygon(polygonOptions)
        }
        Log.e("test_map_result", "isLocationInsideShape $isLocationInsideShape")

    }

    private fun zoomAndShowTextLocation(location: LatLng) {

        Log.e("test_location", location.latitude.toString() + " " + location.longitude)
        if (location != null) {
            if (canSelectLocation) {
                map?.let {
                    vm.lat = location.latitude
                    vm.lng = location.longitude
                }
                canDoCameraIdle = false
                zoomToLocation(location)
                getAddress(location)
            } else {

                vm.etAddress.set(getString(R.string.location_not_supported))

                zoomToLocation(QATAT_LOCATION)
            }
        }

//        checkCurrentLocation(location)

    }

    private fun checkLatLngIsInsideQatar(latLng: LatLng): Boolean {
//        return if (Utils.isNetworkConnected()) {
        val result: String? = Utils.getCountryCodeByLatLong(latLng.latitude, latLng.longitude)
        Log.e("test_location", result + "")
        return (!TextUtils.isEmpty(result) && result.equals("qa", ignoreCase = true))
//        } else {
//            Log.e("test_location","no Internet")
//
//            Toast.makeText(this,"No Internet", Toast.LENGTH_LONG).show()
//
////            if (mView != null) mView.showErrorMessage(mActivity.getString(R.string.noInternetConnection))
//            false
//        }
    }

    private fun zoomToLocation(address: LatLng) {
        vm.lat = address.latitude
        vm.lng = address.longitude


        if (!firstZoom) {
            map?.let {
                zoomLevel = it.cameraPosition.zoom
            }
        } else {
            firstZoom = false
        }

        if (address == QATAT_LOCATION) {
            zoomLevel = 9.5f
        }

        map?.apply {
            animateCamera(CameraUpdateFactory.newLatLngZoom(address, zoomLevel))
        }

    }

    private fun getAddress(latLng: LatLng) {
        var result: String? = getString(R.string.unknown_address)

        val geocoder: Geocoder
        val addresses: List<Address?>?
        geocoder = Geocoder(App.context, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && !addresses.isEmpty()) {
                addresses[0]?.let {

                    val subThoroughfare = it.subThoroughfare
                    val thoroughfare = it.thoroughfare
                    val adminArea = it.adminArea
                    val country = it.countryName
                    val city = it.locality
                    val address = it.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    val postalCode = it.postalCode
                    Log.e("test_data", "postalCode $postalCode ," + "subThoroughfare $subThoroughfare ," + "thoroughfare $thoroughfare ," + "adminArea $adminArea ," + "country $country ," + "city $city.")
                    val list = ArrayList<String?>()
                    if (!TextUtils.isEmpty(country)) list.add(country)
                    if (!TextUtils.isEmpty(subThoroughfare)) list.add(subThoroughfare)
                    if (!TextUtils.isEmpty(thoroughfare)) list.add(thoroughfare)
                    if (!TextUtils.isEmpty(postalCode)) list.add(postalCode)
                    if (!TextUtils.isEmpty(city)) list.add(city)
                    if (!TextUtils.isEmpty(adminArea)) list.add(adminArea)
                    val delimiter = if (isEnglish) ", " else "، "
                    if (!list.isEmpty()) result = TextUtils.join(delimiter, list)
                }

            }


        } catch (e: java.lang.Exception) {
        }
        return try {
//            val address = Geocoder(this, Locale.getDefault()).getFromLocation(
//                latLng.latitude,
//                latLng.longitude,
//                1
//            )!![0]

//            val strAddress = address.getAddressLine(0)
            vm.etAddress.set(result)
            binding.tvSearch.text = result

        } catch (ex: Exception) {
            vm.etAddress.set(result)
            binding.tvSearch.text = result
        }
    }

    override fun onBackPressed() {
        if (::currentLocation.isInitialized) lifecycle.removeObserver(currentLocation)
        finish()
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && requestCode == LOCATION_PERMISSION_ID && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                DialogUtils.showAlertDialog(
//                    mActivity,
//                    mActivity.getString(R.string.alert),
//                    mActivity.getString(R.string.permission_not_granted_error),
//                    mActivity.getString(R.string.ok),
//                    "",
//                    ACTION_CLOSE
//                )
            finish()
        } else if (requestCode == LOCATION_PERMISSION_ID && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initMapListener()
        }

    }
}