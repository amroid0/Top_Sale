package com.aelzohry.topsaleqatar.ui.search

import android.location.Location
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aelzohry.topsaleqatar.helper.Constants.MAP_API_KEY
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.googleApi.GoogleNetworkShared
import com.aelzohry.topsaleqatar.repository.googleApi.RequestListener
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceResult
import com.aelzohry.topsaleqatar.repository.remote.AdsDataSource
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class SearchViewModel(private val txt: String?) : BaseViewModel() {

    var etSearch = ObservableField(txt)
    var selectedCat: Category? = null
    var selectedSubCat: LocalStanderModel? = null
    var selectedType: LocalStanderModel? = null
    var selectedCarMake: StanderModel? = null
    var selectedModelLocal: ArrayList<StanderModel>? = null
    var selectedSubModelLocal: ArrayList<StanderModel>? = null
    var selectedCarShow: User? = null
    var selectedYear: ArrayList<StanderModel>? = null
    var selectedGearbox: ArrayList<StanderModel>? = null
    var selectedEngineSize: ArrayList<StanderModel>? = null

    var selectedFuelType: ArrayList<StanderModel>? = null
    var selectedEngineDriveSystem: ArrayList<StanderModel>? = null
    var selectedCarColor: ArrayList<StanderModel>? = null

    var selectedKm: ArrayList<StanderModel>? = null
    var selectedRegion: LocalStanderModel? = null
    var selectedCity: LocalStanderModel? = null
    var selectedSort: Int = 0
    var selectedLatLng: LatLng? = null
    var distance: Int = 1

    var fromPrice: String? = null
    var toPrice: String? = null
    var fromRooms: String? = null
    var toRooms: String? = null
    var fromBathRooms: String? = null
    var toBathRooms: String? = null

    var listLocation: ArrayList<PlaceAutocomplete>? = null

    private val dataSourceFactory: AdsDataSource.DataSourceFactory

    var adsRes: LiveData<PagedList<Ad>>

    init {

        val prams = HashMap<String, Any>()
        etSearch.get()?.let {
            prams["keyword"] = it
        }

        dataSourceFactory = AdsDataSource.DataSourceFactory(prams)
        val pageSize = 10
        adsRes = LivePagedListBuilder<Int, Ad>(dataSourceFactory, PagedList.Config.Builder().setPageSize(pageSize).setInitialLoadSizeHint(pageSize * 2).setEnablePlaceholders(false).build()).build()
    }

    fun onSortClickedListener(type: Int, location: Location?) {
        val prams = HashMap<String, Any>()
        selectedCat?._id?.let {
            prams["category"] = it
        }
        // for location
        if (type == 5) {
            if (location != null) {
                prams["sortField"] = "location"
                prams["latitude_sort"] = location.latitude
                prams["longitude_sort"] = location.longitude
            }
        }

        dataSourceFactory.changePrams(prams, type)

//        dataSourceFactory.changePrams(HashMap(), type)
    }

    fun onFilterCallBack() {

        val prams = HashMap<String, Any>()

        txt?.let {
            if (it.isNotEmpty()) prams["keyword"] = it
        }

        selectedCat?.let {
            prams["category"] = it._id
        }

        selectedCarMake?.let {
            prams["carMake"] = it._id
        }

        selectedRegion?.let {
            prams["region"] = it._id
        }

        selectedCity?.let {
            prams["city"] = it._id
        }

        selectedSubCat?.let {
            prams["subcategory"] = it._id
        }

        selectedCarShow?.let {
            prams["user"] = it._id
        }

        selectedType?.let {
            prams["type"] = it._id
        }
        fromPrice?.let {
            prams["minPrice"] = it

        }

        toPrice?.let {
            prams["maxPrice"] = it

        }

        fromRooms?.let {
            prams["minNumberOfRooms"] = it

        }

        toRooms?.let {
            prams["maxNumberOfRooms"] = it
        }

        fromBathRooms?.let {
            prams["minNumberOfBathroom"] = it
        }

        toBathRooms?.let {
            prams["maxNumberOfBathroom"] = it
        }



        selectedLatLng?.let {
            if (selectedLatLng != null) {
                prams["latitude"] = selectedLatLng!!.latitude
                prams["longitude"] = selectedLatLng!!.longitude
                distance?.let {
                    prams["km"] = it
                }
            }
        }

        val listModel = ArrayList<String>()
        val listSubModel = ArrayList<String>()
        val listYears = ArrayList<String>()
        val listGearbox = ArrayList<String>()
        val listEngineSize = ArrayList<String>()
        val listKm = ArrayList<String>()

        selectedModelLocal?.forEach { listModel.add(it._id) }
        selectedSubModelLocal?.forEach { listSubModel.add(it._id) }

//        selectedModelLocal?.let {
//            listModel.add(it._id)
//        }
//        selectedSubModelLocal?.let {
//            listSubModel.add(it._id) }


        selectedYear?.forEach { listYears.add(it._id) }
        selectedGearbox?.forEach { listGearbox.add(it._id) }
        selectedEngineSize?.forEach { listEngineSize.add(it._id) }
        selectedKm?.forEach { listKm.add(it._id) }

        if (listModel.isNotEmpty()) prams["carModels"] = listModel.joinToString(separator = ",")
//            prams["carModels"] = listModel

        if (listSubModel.isNotEmpty()) prams["carSubModels"] = listSubModel.joinToString(separator = ",")

        if (listYears.isNotEmpty()) prams["carYears"] = listYears.joinToString(separator = ",")
//            prams["carYears"] = listYears

        if (listGearbox.isNotEmpty()) prams["gearBoxs"] = listGearbox.joinToString(separator = ",")
//            prams["gearBoxs"] = listGearbox

        if (listEngineSize.isNotEmpty()) prams["engineCapacitys"] = listEngineSize.joinToString(separator = ",")
//            prams["engineCapacitys"] = listEngineSize

        if (listKm.isNotEmpty()) prams["kms"] = listKm.joinToString(separator = ",")
//            prams["kms"] = listKm

        listLocation?.let {
            getLocationLatLng(it, object : RequestListener<ArrayList<ArrayList<Double>>> {
                override fun onSuccess(data: ArrayList<ArrayList<Double>>) {
                    Log.e("test_params", "Address:" + data)
                    Log.e("test_params", data.joinToString(separator = ","))
                    prams["locations"] = data.toString()
                    Log.e("test_params", prams.toString())

                    dataSourceFactory.changePrams(prams)
                }

                override fun onFail(message: String?, code: Int) {
                }

            })
        } ?: dataSourceFactory.changePrams(prams)


    }

    fun getLocationLatLng(list: ArrayList<PlaceAutocomplete>, mRequestListener: RequestListener<ArrayList<ArrayList<Double>>>) {
        val listLocationToSend = ArrayList<ArrayList<Double>>()


        for (i in 0 until list.size) {
            val item = list.get(i)
            GoogleNetworkShared.getAsp().general.getPlaceById(MAP_API_KEY, item.placeId, "name,geometry", object : RequestListener<PlaceResult> {
                override fun onSuccess(data: PlaceResult) {
                    val array = arrayListOf<Double>(data.geometry.location.lng, data.geometry.location.lat)
                    listLocationToSend.add(array)
                    if (listLocationToSend.size == list.size) {
                        mRequestListener.onSuccess(listLocationToSend)
                    }

                }

                override fun onFail(message: String, code: Int) {}
            })

        }

        if (list.size == 0) {
            mRequestListener.onSuccess(listLocationToSend)
        }

    }

    fun onSearchClickedListener(v: View) {
        v.hideKeyboard()

        if (etSearch.get()?.isNotEmpty() == true) onFilterCallBack()

    }

    fun getLayoutState(): LiveData<CustomFrame.FrameState> = Transformations.switchMap(dataSourceFactory.dataSourceLiveData, AdsDataSource::layoutState)

    fun getFooterState(): LiveData<Boolean> = Transformations.switchMap(dataSourceFactory.dataSourceLiveData, AdsDataSource::footerState)
}