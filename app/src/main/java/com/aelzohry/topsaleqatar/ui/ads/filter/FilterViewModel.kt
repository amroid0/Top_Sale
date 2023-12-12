package com.aelzohry.topsaleqatar.ui.ads.filter

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.model.*
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.enumClasses.*
import com.google.android.gms.maps.model.LatLng

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class FilterViewModel(private val category: Category?, val loadCategory: Boolean) :
    BaseViewModel() {

    private var repository: Repository = RemoteRepository()

    var catsRes = MutableLiveData<List<Category>>()
    var subCatsRes = MutableLiveData<List<LocalStanderModel>>()
    var typeRes = MutableLiveData<List<LocalStanderModel>>()
    var carMakesRes = MutableLiveData<List<StanderModel1>>()
    var regionRes = MutableLiveData<List<LocalStanderModel>>()
    var modelRes = MutableLiveData<List<StanderModel1>>()
    var yearRes = MutableLiveData<List<StanderModel>>()

    var carSubModelRes = MutableLiveData<List<StanderModel1>>()


    var gearboxRes = MutableLiveData<List<StanderModel>>()
    var engineSizeRes = MutableLiveData<List<StanderModel>>()
    var kmRes = MutableLiveData<List<StanderModel>>()

    var fuelTypeRes = MutableLiveData<List<StanderModel>>()
    var carEngineDriveSystemRes = MutableLiveData<List<StanderModel>>()
    var carColorRes = MutableLiveData<List<StanderModel>>()


    var selectedCat: Category? = category
    var selectedSubCat: LocalStanderModel? = null
    var selectedType: LocalStanderModel? = null
    var selectedCarMake: StanderModel? = null
    var selectedModelLocal: ArrayList<StanderModel>? = null
    var selectedSubModelLocal: ArrayList<StanderModel>? = null
    var selectedYear: ArrayList<StanderModel>? = null
    var selectedRegion: LocalStanderModel? = null

    var selectedGearbox: ArrayList<StanderModel>? = null
    var selectedEngineSize: ArrayList<StanderModel>? = null
    var selectedKm: ArrayList<StanderModel>? = null


    var selectedFuelType: ArrayList<StanderModel>? = null
    var selectedEngineDriveSystem: ArrayList<StanderModel>? = null
    var selectedCarColor: ArrayList<StanderModel>? = null

    var selectedLatLng: LatLng? = null
    var distance: Int = 1

    var listLocations: ArrayList<PlaceAutocomplete>? = ArrayList()


    var selectedCatObservable = MutableLiveData<Category>()
    var selectCatText = ObservableField("")
    var selectSubCatText = ObservableField("")
    var selectCityText = ObservableField("")
    var selectedTypeText = ObservableField("")
    var selectedCarMakeText = ObservableField("")
    var selectedModelText = ObservableField("")
    var selectedSubModelText = ObservableField("")
    var selectedYearText = ObservableField("")
    var selectedGearboxText = ObservableField("")
    var selectedEngineSizeText = ObservableField("")
    var selectedKmText = ObservableField("")
    var selectedRegionText = ObservableField("")

    var selectedFuelText = ObservableField("")
    var selectedCarEngineDriveSystemText = ObservableField("")
    var selectedCarColorText = ObservableField("")

    var selectLocationText = ObservableField("")

    var selectedCityAdLocation: LocalStanderModel? = null

    var subCatState = ObservableField(category?.subcategories?.isNullOrEmpty() == false)
    var typeState = ObservableField(category?.types?.isNullOrEmpty() == false)
    var carCatState = ObservableField(category?.categoryClass == CategoryClass.CARS)
    var carCatMakeState = ObservableField(false)
    var regionState = ObservableField(category?.categoryClass == CategoryClass.PROPERTIES)

    var etFromPrice = ObservableField("")
    var etToPrice = ObservableField("")
    var etFromRooms = ObservableField("")
    var etToRooms = ObservableField("")
    var etFromBathRooms = ObservableField("")
    var etToBathRooms = ObservableField("")


    var citiesRes = MutableLiveData<List<Region>>()


    fun onResetBtnClickedListener() {
        selectedSubCat = null
        selectedType = null
        selectedCarMake = null
        selectedModelLocal = null
        selectedSubModelLocal = null
        selectedYear = null
        selectedGearbox = null
        selectedEngineSize = null

        selectedFuelType = null
        selectedEngineDriveSystem = null
        selectedCarColor = null

        selectedKm = null
        selectedRegion = null
        selectedCityAdLocation = null
        selectedLatLng = null
        distance = 1

        selectLocationText.set("")
        selectSubCatText.set("")
        selectedTypeText.set("")
        selectedCarMakeText.set("")
        selectedModelText.set("")
        selectedSubModelText.set("")
        selectedYearText.set("")
        selectedGearboxText.set("")
        selectedEngineSizeText.set("")

        selectedFuelText.set("")
        selectedCarEngineDriveSystemText.set("")
        selectedCarColorText.set("")

        selectedKmText.set("")
        selectedRegionText.set("")

        listLocations = ArrayList()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {


        if (selectedCat != null)
            onCatSelectedListener(selectedCat)

        if (loadCategory) {
            loadCategory()
        } else {
            loadSubCats()
            loadType()
        }

        loadRegions()
        loadCarYears()
        loadCarMakes()
        loadCarGearbox()
        loadCarEngineSize()
        loadCarKm()
        loadFuelType()
        loadEngineDriveSystemType()
        loadCarColor()

        repository.getCities {
            it?.data.let { lst ->
                citiesRes.postValue(lst)
            }
        }


    }


    fun onCityAdLocationSelectedListener(modelLocal: LocalStanderModel?) {
        val modelLocal = modelLocal ?: return
        selectedCityAdLocation = modelLocal
        selectCityText.set(modelLocal.title.localized)
    }

    fun onCatSelectedListener(model: Category?) {
        val category = model ?: return
        this.selectedCat = category
        this.selectedCatObservable.postValue(selectedCat)
        selectCatText.set(category.title.localized)


        if (!category.subcategories.isNullOrEmpty())
            this.subCatsRes.postValue(category.subcategories)

        if (!category.types.isNullOrEmpty())
            this.typeRes.postValue(category.types)



        selectedSubCat = null
        selectedType = null

        selectSubCatText.set("")
        selectedTypeText.set("")

        subCatState.set(category.subcategories?.isNullOrEmpty() == false)
        typeState.set(category.types?.isNullOrEmpty() == false)
        carCatState.set(category.categoryClass == CategoryClass.CARS)
        carCatMakeState.set(false)
        regionState.set(category.categoryClass == CategoryClass.PROPERTIES)

    }

    private fun loadCategory() {
        repository.getCategories {
            it?.data?.let { categories ->
                catsRes.postValue(categories)

                setCurrentSelectedCategory(categories)

            }
        }
    }


    private fun setCurrentSelectedCategory(list: ArrayList<Category>) {
        for (item in list) {
            if (selectedCat != null && item._id.equals(selectedCat!!._id)) {
                onCatSelectedListener(item)
                selectedCatObservable.postValue(item)
                selectedCat = item
                subCatState.set(item.subcategories?.isNullOrEmpty() == false)
                selectCatText.set(item.title.localized)
            }
        }
    }

    private fun loadCarMakes() {
        repository.getCarMake {
            it?.response?.let { makes ->
                carMakesRes.postValue(makes)
                if (makes.isNotEmpty())
                    loadCarModel(makes[0]._id)
            }
        }
    }

    private fun loadRegions() {
        repository.getRegion {
            it?.response?.let { regions ->
                regionRes.postValue(regions)
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

    fun loadCarSubModel(listModel:String){
        repository.getCarSubModel(listModel) {
            it?.response?.let { makes ->
                carSubModelRes.postValue(makes)
            }
        }
    }

    private fun loadCarYears() {
        val list = ArrayList<StanderModel>()
        for (i in 2022 downTo 1800) {
            list.add(
                StanderModel(
                    i.toString(), i.toString()
                )
            )
        }
        yearRes.postValue(list)

    }


    private fun loadCarGearbox() {
        val list = MotionVector.getList()
        gearboxRes.postValue(list)
    }

    private fun loadCarEngineSize() {
        val list = EngineSize.getList()
        engineSizeRes.postValue(list)
    }

    private fun loadCarKm() {
        val list = Kilometer.getList()
        kmRes.postValue(list)
    }


    private fun loadFuelType() {
        val list = FuelType.getList()
        fuelTypeRes.postValue(list)
    }

    private fun loadEngineDriveSystemType() {
        val list = EngineDriveSystem.getList()
        carEngineDriveSystemRes.postValue(list)
    }

    private fun loadCarColor() {
        val list = CarColor.getList()
        carColorRes.postValue(list)
    }

    private fun loadSubCats() {
        val list = ArrayList<LocalStanderModel>()
        category?.subcategories?.forEach {
            list.add(LocalStanderModel(it._id, it.title))
        }
        subCatsRes.postValue(list)
    }

    private fun loadType() {

        val list = ArrayList<LocalStanderModel>()
        category?.types?.forEach {
            list.add(LocalStanderModel(it._id, it.title))
        }
        typeRes.postValue(list)
    }

    fun refreshTitles() {
        selectSubCatText.set(selectedSubCat?.title?.localized)
        selectedTypeText.set(selectedType?.title?.localized)
        selectedCarMakeText.set(selectedCarMake?.title)
        selectedRegionText.set(selectedRegion?.title?.localized)
        selectCityText.set(selectedCityAdLocation?.title?.localized)


        refreshModel()
        refreshSubModel()
        refreshYears()
        refreshEngineSize()
        refreshGearbox()
        refreshKm()
        refreshFuelType()
        refreshEngineDriveSystem()
        refreshCarColor()

    }

    fun refreshModel() {
        val listModel = ArrayList<String>()
//        selectedModelLocal?.forEach {
//            listModel.add(it.title)
//        }
        val modelTitle = listModel.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedModelText.set(modelTitle)


        val listModelId = ArrayList<String>()
//        selectedModelLocal?.forEach {
//            listModelId.add(it._id)
//        }


        val string = java.lang.String.join(",", listModelId)


        loadCarSubModel(string)

    }


    fun refreshSubModel() {
        val listModel = ArrayList<String>()
//        selectedSubModelLocal?.forEach {
//            listModel.add(it.title)
//        }
        val modelTitle = listModel.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedSubModelText.set(modelTitle)

    }

    fun refreshYears() {
        val list = ArrayList<String>()
        selectedYear?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedYearText.set(title)
    }


    fun refreshGearbox() {
        val list = ArrayList<String>()
        selectedGearbox?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedGearboxText.set(title)
    }


    fun refreshEngineSize() {
        val list = ArrayList<String>()
        selectedEngineSize?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedEngineSizeText.set(title)
    }


    fun refreshKm() {
        val list = ArrayList<String>()
        selectedKm?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedKmText.set(title)
    }

    fun refreshFuelType() {
        val list = ArrayList<String>()
        selectedFuelType?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedFuelText.set(title)
    }

    fun refreshEngineDriveSystem() {
        val list = ArrayList<String>()
        selectedEngineDriveSystem?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedCarEngineDriveSystemText.set(title)
    }

    fun refreshCarColor() {
        val list = ArrayList<String>()
        selectedCarColor?.forEach {
            list.add(it.title)
        }
        val title = list.toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", " - ")
        selectedCarColorText.set(title)
    }
}