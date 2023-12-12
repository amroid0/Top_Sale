package com.aelzohry.topsaleqatar.ui.ads

import android.content.Context
import android.location.Location
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.aelzohry.topsaleqatar.helper.Constants
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.model.*
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.googleApi.GoogleNetworkShared
import com.aelzohry.topsaleqatar.repository.googleApi.RequestListener
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceResult
import com.aelzohry.topsaleqatar.repository.remote.AdsDataSource
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class AdsViewModel(var category: Category?, subCat: LocalStanderModel?) : BaseViewModel() {

    private var location: Location? = null
    private var repository: Repository = RemoteRepository()
    var bannersRes = MutableLiveData<List<Banner>>()
    var catsRes = MutableLiveData<List<Category>>()
    var carMakesRes = MutableLiveData<List<StanderModel1>>()
    var regionRes = MutableLiveData<List<LocalStanderModel>>()
    var modelRes = MutableLiveData<List<StanderModel1>>()
    var yearRes = MutableLiveData<List<StanderModel>>()
    var subCatsRes = MutableLiveData<List<LocalStanderModel>>()

    val currentStep = ObservableField(0) //
    val tvTitle = ObservableField("")

    var etSearch = ObservableField("")

    var typeRes = MutableLiveData<List<LocalStanderModel>>()
    var carModelRes = MutableLiveData<List<StanderModel>>()
    var carSubModelRes = MutableLiveData<List<StanderModel1>>()

    var selectedSubCat: LocalStanderModel? = subCat
    var selectedType: LocalStanderModel? = null

    var selectedCarMakeItem: StanderModel? = null
    var selectedModelItem: ArrayList<StanderModel>? = null
    var selectedSubModelItem: ArrayList<StanderModel>? = null



    var selectedCarShow: User?= null

    var selectedYear: ArrayList<StanderModel>? = null
    var selectedGearbox: ArrayList<StanderModel>? = null
    var selectedEngineSize: ArrayList<StanderModel>? = null

    var selectedFuelType: ArrayList<StanderModel>? = null
    var selectedEngineDriveSystem: ArrayList<StanderModel>? = null
    var selectedCarColor: ArrayList<StanderModel>? = null

    var selectedKm: ArrayList<StanderModel>? = null
    var selectedLatLng: LatLng? = null
    var distance: Int = 1

    var selectedRegion: LocalStanderModel? = null
    var selectedCity: LocalStanderModel? = null
    var selectedSort: Int = 0

    var fromPrice: String? = null
    var toPrice: String? = null
    var fromRooms: String? = null
    var toRooms: String? = null
    var fromBathRooms: String? = null
    var toBathRooms: String? = null

    var selectedCarMakeText = ObservableField("")
    var selectedModelText = ObservableField("")
    var selectedSubModelText = ObservableField("")
    var carCatMakeState = ObservableField(false)

    var carCategoryState = ObservableField(category?.categoryClass == CategoryClass.CARS)
    var aqarCategoryState = ObservableField(category?.categoryClass == CategoryClass.LANDS)

    var forRentItem: LocalStanderModel? = null
    var forSaleItem: LocalStanderModel? = null

    var listLocation: ArrayList<PlaceAutocomplete>? = null

    private val dataSourceFactory: AdsDataSource.DataSourceFactory

    var adsRes: LiveData<PagedList<Ad>>

    init {

        val prams = HashMap<String, Any>()
        category?._id?.let {
            prams["category"] = it
            prams["sortField"] = selectedSort
        }
        dataSourceFactory = AdsDataSource.DataSourceFactory(prams)
        dataSourceFactory.changePrams(prams, selectedSort)
        val pageSize = 10
        adsRes = LivePagedListBuilder<Int, Ad>(dataSourceFactory, PagedList.Config.Builder().setPageSize(pageSize).setInitialLoadSizeHint(pageSize * 2).setEnablePlaceholders(false).build()).build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {
        loadBanners()
        loadCategory()
        loadCarMakes()
    }

    private fun loadCategory() {
        repository.getCategories {
            it?.data?.let { categories ->
                catsRes.postValue(categories)
                for (item in categories) {
                    if (item._id == category?._id) {
                        category = item
                        loadSubCats()
                    }
                }
                loadType()

            }
        }
    }

    private fun loadSubCats() {
        val list = ArrayList<LocalStanderModel>()
        category?.subcategories?.forEach {
            list.add(LocalStanderModel(it._id, it.title))
        }
        subCatsRes.postValue(list)
    }

    private fun loadBanners() {
        repository.getBanners {
            it?.let { banners ->
                bannersRes.postValue(banners)
            }
        }
    }

    private fun loadType() {
        val list = ArrayList<LocalStanderModel>()
        category?.types?.forEach {
            list.add(LocalStanderModel(it._id, it.title))
        }

        if (list.size > 1) {

            forSaleItem = list.get(0)
            forRentItem = list.get(1)
        }
        typeRes.postValue(list)
    }

    private fun loadCarMakes() {
        repository.getCarMake {
            it?.response?.let { makes ->
                carMakesRes.postValue(makes)
                if (makes.isNotEmpty()) loadCarModel(makes[0]._id)
            }
        }
    }

    fun loadCarModel(makeItem: String) {
        repository.getCarModel(makeItem) {
            it?.response?.let { models ->
                modelRes.postValue(models)
            }
        }
    }

    fun onSortClickedListener(type: Int, ctx: Context, location: Location?) {
        val prams = HashMap<String, Any>()
        category?._id?.let {
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

//            GPSTracker(ctx) { location, err ->
////                if (err == null) {
//                this.location = location
//                prams["sortField"] = "location"
//                prams["latitude_sort"] = location?.latitude!!
//                prams["longitude_sort"] = location?.longitude!!
//
//                dataSourceFactory.changePrams(prams, type)
//
////                }
//
//            }

        dataSourceFactory.changePrams(prams, type)
    }


    fun onMakeSelectedListener(model: StanderModel?, data: (List<StanderModel>) -> Unit?) {
        val modelLocal = model ?: return
        this.selectedCarMakeItem = modelLocal
        selectedModelItem = null
        repository.getCarModel(model._id) {
            it?.response?.let { makes ->
                val list = arrayListOf<StanderModel>()
                makes.forEach {
                    list.add(StanderModel(it._id, it.localized, false))
                }
                carModelRes.postValue(list)
                data(list)
            }
        }
    }

    fun refreshModel() {
        val listModel = ArrayList<String>()
        selectedModelItem?.forEach {
            listModel.add(it.title)
        }
        val modelTitle = listModel.toString().replace("[", "").replace("]", "").replace(",", " - ")
        selectedModelText.set(modelTitle)

        val listModelId = ArrayList<String>()
        selectedModelItem?.forEach {
            listModelId.add(it._id)
        }

        val string = java.lang.String.join(",", listModelId)


        loadCarSubModel(string)

    }

    fun refreshSubModel() {
        val listModel = ArrayList<String>()
        selectedSubModelItem?.forEach {
            listModel.add(it.title)
        }
        val modelTitle = listModel.toString().replace("[", "").replace("]", "").replace(",", " - ")
        selectedSubModelText.set(modelTitle)

    }

    fun loadCarSubModel(listModel: String) {
        repository.getCarSubModel(listModel) {
            it?.response?.let { makes ->
                carSubModelRes.postValue(makes)
            }
        }
    }

//    fun onModelSelectedListener(model: StanderModel?, data: (List<StanderModel>) -> Unit?) {
//        val item = model ?: return
//        selectedModelItem = item
//
//        repository.getCarSubModel(model._id) {
//            it?.response?.let { modelsList ->
//
//                val list = arrayListOf<StanderModel>()
//                modelsList.forEach {
//                    list.add(StanderModel(it._id, it.localized, false))
//                }
//
//                carSubModelRes.postValue(modelsList)
//                data(list)
//            }
//        }
//    }

//    fun onSubModelSelectedListener(model: StanderModel?) {
//        val item = model ?: return
//        selectedSubModelItem = item
//    }


    fun onFilterCallBack() {

        val prams = HashMap<String, Any>()

        etSearch.get()?.let {
            if (it.isNotEmpty()) prams["keyword"] = it
        }

        category?.let {
            prams["category"] = it._id
        }

        selectedCarMakeItem?.let {
            prams["carMake"] = it._id
        }

        selectedCarShow?.let {
            prams["user"] = it._id
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

        selectedType?.let {
            prams["type"] = it._id
        }

        toPrice?.let {
            prams["maxPrice"] = it

        }

        fromPrice?.let {
            prams["minPrice"] = it

        }

        toRooms?.let {
            prams["maxNumberOfRooms"] = it

        }

        fromRooms?.let {
            prams["minNumberOfRooms"] = it
        }

        toBathRooms?.let {
            prams["maxNumberOfBathroom"] = it
        }

        fromBathRooms?.let {
            prams["minNumberOfBathroom"] = it
        }

        selectedLatLng?.let {
            if (selectedLatLng != null) {
                prams["latitude"] = selectedLatLng!!.latitude
                prams["longitude"] = selectedLatLng!!.longitude
                distance.let {
                    prams["km"] = it
                }
            }
        }


        Log.e("filter", prams.toString())
        val listModel = ArrayList<String>()
        val listSubModel = ArrayList<String>()
        val listYears = ArrayList<String>()
        val listGearbox = ArrayList<String>()
        val listEngineSize = ArrayList<String>()

        val listFuelType = ArrayList<String>()
        val listEngineDriveSystem = ArrayList<String>()
        val listCarColor = ArrayList<String>()

        val listKm = ArrayList<String>()
//
//        selectedModelItem?.let {
//            listModel.add(it._id)
//        }
//        selectedSubModelItem?.let {
//            listSubModel.add(it._id)
//        }
        selectedModelItem?.forEach { listModel.add(it._id) }
        selectedSubModelItem?.forEach { listSubModel.add(it._id) }
        selectedYear?.forEach { listYears.add(it._id) }
        selectedGearbox?.forEach { listGearbox.add(it._id) }
        selectedEngineSize?.forEach { listEngineSize.add(it._id) }

        selectedFuelType?.forEach { listFuelType.add(it._id) }
        selectedEngineDriveSystem?.forEach { listEngineDriveSystem.add(it._id) }
        selectedCarColor?.forEach { listCarColor.add(it._id) }

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

        if (listFuelType.isNotEmpty()) prams["fuelType"] = listFuelType.joinToString(separator = ",")

        if (listEngineDriveSystem.isNotEmpty()) prams["pushFatisType"] = listEngineDriveSystem.joinToString(separator = ",")

        if (listCarColor.isNotEmpty()) prams["color"] = listCarColor.joinToString(separator = ",")

        if (listKm.isNotEmpty()) prams["kms"] = listKm.joinToString(separator = ",")
//            prams["kms"] = listKm

        selectedSort = 0

        prams["km"] = 10


        listLocation?.let {
            isLoading.postValue(true)
            getLocationLatLng(it, object : RequestListener<ArrayList<ArrayList<Double>>> {
                override fun onSuccess(data: ArrayList<ArrayList<Double>>) {
                    Log.e("test_params", "Address:" + data)
                    Log.e("test_params", data.joinToString(separator = ","))
                    prams["locations"] = data.toString()
                    Log.e("test_params", prams.toString())
                    isLoading.postValue(false)

                    dataSourceFactory.changePrams(prams, AdsDataSource.FILTER)

                }

                override fun onFail(message: String?, code: Int) {
                    isLoading.postValue(false)
                }

            })
        } ?: dataSourceFactory.changePrams(prams, AdsDataSource.FILTER)


    }

    fun getLocationLatLng(list: ArrayList<PlaceAutocomplete>, mRequestListener: RequestListener<ArrayList<ArrayList<Double>>>) {
        val listLocationToSend = ArrayList<ArrayList<Double>>()


        for (i in 0 until list.size) {
            val item = list.get(i)
            if (TextUtils.isEmpty(item.placeId)) {
                listLocationToSend.add(arrayListOf(item.latLng.latitude, item.latLng.longitude))
                if (listLocationToSend.size == list.size) {
                    mRequestListener.onSuccess(listLocationToSend)
                }

            }
            GoogleNetworkShared.getAsp().general.getPlaceById(Constants.MAP_API_KEY, item.placeId, "name,geometry", object : RequestListener<PlaceResult> {
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

    fun getLayoutState(): LiveData<CustomFrame.FrameState> = Transformations.switchMap<AdsDataSource, CustomFrame.FrameState>(dataSourceFactory.dataSourceLiveData, AdsDataSource::layoutState)

    fun getFooterState(): LiveData<Boolean> = Transformations.switchMap<AdsDataSource, Boolean>(dataSourceFactory.dataSourceLiveData, AdsDataSource::footerState)

    companion object {
        const val latest = 0
        const val closest = 1
        const val lowest_price = 2
        const val higher_price = 3
        const val model_newest = 4
        const val minimum_mileage = 5
    }
}