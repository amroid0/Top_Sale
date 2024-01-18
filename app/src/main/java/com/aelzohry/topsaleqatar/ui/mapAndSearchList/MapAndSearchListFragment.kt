package com.aelzohry.topsaleqatar.ui.mapAndSearchList

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.App
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentMapAndSearchListBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.ui.autoCompleteSearchLocation.view.SelectMultipleLocationFragment
import com.aelzohry.topsaleqatar.ui.selectLocationDialog.view.SelectLocationDialogFragment
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Locale


class MapAndSearchListFragment : BaseBottomSheet<FragmentMapAndSearchListBinding, BaseViewModel>() {

    private var isMapView = false

    override fun getLayoutID(): Int = R.layout.fragment_map_and_search_list

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)

    override fun setupUI() {
        changeIconUi()
        onClickedListener()
    }

    override fun onClickedListener() {
//        binding.ibView.setOnClickListener {
//            isMapView = !isMapView
//            changeIconUi()
//        }

        binding.ibClose.setOnClickListener {
            dismiss()
        }
    }



    override fun observerLiveData() {
    }

    override fun isFullHeight(): Boolean = true

    companion object {
        @JvmStatic
        fun newInstance() = MapAndSearchListFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    private fun changeIconUi() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val bottomSheetBehavior = bottomSheetDialog.behavior
        if (isMapView) {
            bottomSheetBehavior.isDraggable = false
            val fragment = SelectLocationDialogFragment.newInstance()
            fragment.setListener(object : SelectLocationDialogFragment.LocationListener {
                override fun onLocationSelected(location: LatLng?, distance: Int) {
                    if (::listener.isInitialized) {
                        location?.let {
                            val place = PlaceAutocomplete(getAddress(it), it)
                            listener.passLocations(arrayListOf(place))
                            dismiss()
                        }
                    }
                }

                override fun onBackClick() {
                    isMapView= false
                    changeIconUi()
                }

            })
            replaceFragment(fragment)
        } else {
            bottomSheetBehavior.isDraggable = true
            val fragment = SelectMultipleLocationFragment.newInstance()
            fragment.setListener(object : SelectMultipleLocationFragment.Listener {
                override fun onItemSelected(list: ArrayList<PlaceAutocomplete>) {
                    if (::listener.isInitialized) {
                        listener.passLocations(list)
                        dismiss()
                        dismiss()
                    }
                }

                override fun onMapViewClick() {
                    isMapView = true
                    changeIconUi()
                }

            })
            replaceFragment(fragment)
        }
    }

    private fun getAddress(latLng: LatLng) : String {
        var result: String = getString(R.string.unknown_address)

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
                    val list = java.util.ArrayList<String?>()
                    if (!TextUtils.isEmpty(country)) list.add(country)
                    if (!TextUtils.isEmpty(subThoroughfare)) list.add(subThoroughfare)
                    if (!TextUtils.isEmpty(thoroughfare)) list.add(thoroughfare)
                    if (!TextUtils.isEmpty(postalCode)) list.add(postalCode)
                    if (!TextUtils.isEmpty(city)) list.add(city)
                    if (!TextUtils.isEmpty(adminArea)) list.add(adminArea)
                    val delimiter = if (Helper.isEnglish) ", " else "، "
                    if (!list.isEmpty()) result = TextUtils.join(delimiter, list)
                }

            }


        } catch (e: java.lang.Exception) {
        }
        return result
    }


    private fun replaceFragment(fragment: Fragment) {
//        childFragmentManager.beginTransaction().replace(R.id.detail_container, fragment).commit()

        childFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    interface SearchResult {
        fun passLocations(item: ArrayList<PlaceAutocomplete>)
        fun passLatLng(item: LatLng)
    }

    private lateinit var listener: SearchResult

    fun setListener(listener: SearchResult) {
        this.listener = listener
    }

}