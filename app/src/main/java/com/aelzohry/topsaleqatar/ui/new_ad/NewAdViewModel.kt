package com.aelzohry.topsaleqatar.ui.new_ad

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aelzohry.topsaleqatar.model.*
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.CategoryData
import com.aelzohry.topsaleqatar.ui.new_ad.finishAd.FinishActivity
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.customViews.CustomEditText
import java.io.File

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class NewAdViewModel : BaseViewModel() {

    var repository: Repository = RemoteRepository()
    var ad: Ad? = null

    var catsRes = MutableLiveData<List<CategoryData>>()

    var subCatsRes = MutableLiveData<List<LocalStanderModel>>()
    var typeRes = MutableLiveData<List<LocalStanderModel>>()

    var regionRes = MutableLiveData<List<Region>>()
    var citiesRes = MutableLiveData<List<Region>>()

//    var carMakesRes = MutableLiveData<List<StanderModel>>()
    var carModelRes = MutableLiveData<List<StanderModel1>>()
    var carSubModelRes = MutableLiveData<List<StanderModel1>>()
//    var yearRes = MutableLiveData<List<LocalStanderModel>>()


    /*
    * steps */
    val currentStep = ObservableField(0) //
    val tvTitle = ObservableField("")


    /**/

    var selectedCat: Category? = null
    var selectedSubCat: LocalStanderModel? = null
    var selectedType: LocalStanderModel? = null
    var selectedCarMake: StanderModel1? = null
    var selectedModelLocal: StanderModel1? = null
    var selectedSubModelLocal: StanderModel1? = null
    var selectedYear: LocalStanderModel? = null
    var selectedMotion: StanderModel? = null
    var selectedEngine: StanderModel? = null
    var selectedKm: StanderModel? = null
    var selectedRegion: LocalStanderModel? = null
    var selectedCityAdLocation: LocalStanderModel? = null
    var selectedRooms: StanderModel? = null
    var selectedBathRoom: StanderModel? = null
    var selectedEngineSystem: StanderModel? = null
    var selectedFuelType: StanderModel? = null
    var selectedColor: StanderModel? = null
    var selectedCarShow: User? = null

    var selectCatText = ObservableField("")
    var selectSubCatText = ObservableField("")
    var selectedTypeText = ObservableField("")
    var selectCarText = ObservableField("")
    var selectCarMotionAndEnginAnKmText = ObservableField("")
    var selectCarEngineAndColorAndFuelText = ObservableField("")

    var selectPropertiesText = ObservableField("")
    var selectRegionText = ObservableField("")
    var selectedCityAdLocationText = ObservableField("")
    var subCatState = ObservableField(false)
    var typeState = ObservableField(false)
    var carCatState = ObservableField(false)
    var propertiesState = ObservableField(false)
    var regionState = ObservableField(false)
    var etTitle = ObservableField("")
    var etspace = ObservableField("")
    var etKm = ObservableField("")
    var etPrice = ObservableField("")
    var etDesc = ObservableField("")
    var address = ObservableField("")

    var lat: Double? = 0.0
    var lng: Double? = 0.0

//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    fun loadData() {
//
//        repository.getCategories {
//            it?.data?.let { categories ->
//                catsRes.postValue(categories)
//            }
//        }
//        repository.getRegions {
//            it?.data.let { lst ->
//                regionRes.postValue(lst)
//            }
//        }
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadData() {

        frameState.set(CustomFrame.FrameState.PROGRESS)
        repository = RemoteRepository()
        repository.getCategoriesGroupd { response ->
            response?.data?.let { cats ->
                catsRes.postValue(cats.sortedBy { it -> it.order })
            }
            frameState.set(CustomFrame.FrameState.LAYOUT)
        }

        repository.getRegions {
            it?.data.let { lst ->
                regionRes.postValue(lst)
            }
        }

        repository.getCities {
            it?.data.let { lst ->
                citiesRes.postValue(lst)
            }
        }

        loadCategory()
    }


    private fun loadCategory() {
        repository.getCategories { response ->
            response?.data?.let { cats ->
                setCurrentSelectedCategory(cats)
            }
            frameState.set(CustomFrame.FrameState.LAYOUT)
        }
    }

    private fun setCurrentSelectedCategory(list: ArrayList<Category>) {
        for (item in list) {
            if (selectedCat != null && item._id.equals(selectedCat!!._id)) {
                onCatSelectedListener(item, false)
                selectedCat = item
                subCatState.set(item.subcategories?.isNullOrEmpty() == false)
                selectCatText.set(item.title.localized)
            }
        }

        ad?.let {
            onSubCatSelectedListener(it.subcategory)
            onSelectedTypeListener(it.type)
            onRegionSelectedListener(it.region)
        }

    }

    fun onCatSelectedListener(model: Category?, restValue: Boolean) {
        val category = model ?: return
        this.selectedCat = category
        selectCatText.set(category.title.localized)
        this.subCatsRes.postValue(category.subcategories)
        this.typeRes.postValue(category.types)

        if (restValue) {
            selectedSubCat = null
            selectedType = null
            selectedRegion = null
            selectedCityAdLocation = null
            selectedCarMake = null
            selectedMotion = null
            selectedEngine = null
            selectedKm = null
            selectedModelLocal = null
            selectedYear = null
            selectedSubModelLocal = null
            selectedRooms = null
            selectedBathRoom = null


            selectRegionText.set("")
            selectedCityAdLocationText.set("")
            selectSubCatText.set("")
            selectedTypeText.set("")
            selectPropertiesText.set("")
            selectCarText.set("")
            selectCarMotionAndEnginAnKmText.set("")
        }

        subCatState.set(category.subcategories?.isNullOrEmpty() == false)
        typeState.set(category.types?.isNullOrEmpty() == false)
        carCatState.set(category.categoryClass == CategoryClass.CARS)
        if (category.categoryClass == CategoryClass.PROPERTIES) {
            propertiesState.set(true)
            regionState.set(false)
        } else if (category.categoryClass == CategoryClass.LANDS) {
            propertiesState.set(false)
            regionState.set(false)
        } else {
            propertiesState.set(false)
            regionState.set(false)
        }
    }

    fun onSubCatSelectedListener(model: LocalStanderModel?) {
        val modelLocal = model ?: return
        selectSubCatText.set(modelLocal.title.localized)
        selectedSubCat = modelLocal
    }

    fun onSelectedTypeListener(model: LocalStanderModel?) {
        val modelLocal = model ?: return
        selectedTypeText.set(modelLocal.title.localized)
        selectedType = modelLocal
    }

    fun onRegionSelectedListener(modelLocal: LocalStanderModel?) {
        val modelLocal = modelLocal ?: return
        selectedRegion = modelLocal
        selectRegionText.set(modelLocal.title.localized)
    }


    fun onCityAdLocationSelectedListener(modelLocal: LocalStanderModel?) {
        val modelLocal = modelLocal ?: return
        selectedCityAdLocation = modelLocal
        selectedCityAdLocationText.set(modelLocal.title.localized)
    }


    fun onRoomSelectedListener(model: StanderModel) {
        selectedRooms = model
    }

    fun onBathRoomSelectedListener(model: StanderModel) {
        selectedBathRoom = model
    }

    fun onMakeSelectedListener(model: StanderModel1?, data: (List<StanderModel1>) -> Unit?) {
        val modelLocal = model ?: return
        selectedCarMake = modelLocal
//        selectedCarMakeText.set(modelLocal.title)
        selectedModelLocal = null
//        selectedModelText.set("")
        repository.getCarModel(model._id) {
            it?.response?.let { makes ->
                carModelRes.postValue(makes)
                data(makes)
            }
        }
    }

    fun onModelSelectedListener(model: StanderModel1?, data: (List<StanderModel1>) -> Unit?) {
        val list = model ?: return
        selectedModelLocal = list

        repository.getCarSubModel(model._id) {
            it?.response?.let { makes ->
                carSubModelRes.postValue(makes)
                data(makes)
            }
        }
    }

    fun onModelSelectedListener(model: StanderModel1?) {
        val list = model ?: return
        selectedModelLocal = list
    }

    fun onSubModelSelectedListener(model: StanderModel1?) {
        val item = model ?: return
        selectedSubModelLocal = item

//        selectedModelText.set(list?.title)
    }

    fun onSelectedYearsListener(model: LocalStanderModel) {
        val item = model ?: return
        selectedYear = item
//        selectedYearText.set(list.title.localized)
    }


    fun onSelectedMotionListener(model: StanderModel) {
        val item = model ?: return
        selectedMotion = item
//        selectedYearText.set(list.title.localized)
    }


    fun onSelectedEngineSizeListener(model: StanderModel) {
        val item = model ?: return
        selectedEngine = item
//        selectedYearText.set(list.title.localized)
    }



    fun onSelectedKmListener(model: StanderModel) {
        val item = model ?: return
        selectedKm = item
//        selectedYearText.set(list.title.localized)
    }


    fun onSelectedCarEngineSystemListener(model: StanderModel) {
        val item = model
        selectedEngineSystem = item
//        selectedYearText.set(list.title.localized)
    }

    fun onSelectedCarColorListener(model: StanderModel) {
        val item = model
        selectedColor = item
//        selectedYearText.set(list.title.localized)
    }

    fun onSelectedFuelListener(model: StanderModel) {
        val item = model ?: return
        selectedFuelType = item
//        selectedYearText.set(list.title.localized)
    }


    fun onAddNewClickedListener(
      v: View,
      images: List<File>,
      videoPath: File?,
      edDesc: CustomEditText,
      isAllComments: Boolean,
      isDefaultVideo: Boolean
    ) {

        isLoading.postValue(true)
        repository.newAd(
            etTitle.get() ?: "",
            etPrice.get() ?: "",
            edDesc.text ?: "",
            selectedCat?._id ?: "",
            etspace.get() ?: "",
            selectedType?._id,
            selectedSubCat?._id,
            selectedRegion?._id,
            selectedCityAdLocation?._id,
            selectedCarMake?._id,
            selectedModelLocal?._id,
            selectedSubModelLocal?._id,
            selectedYear?._id ?: "",
            selectedMotion?._id,
            selectedEngine?._id,
            selectedKm?._id,
            lat?.toString(),
            lng?.toString(),
            address.get(),
            selectedRooms?._id,
            selectedBathRoom?._id,
            selectedEngineSystem?._id, selectedFuelType?._id, selectedColor?._id,
            images,
            videoPath,
            isAllComments,
            isDefaultVideo,
            ) { success, message, ad ->
            showToast.postValue(message)
            isLoading.postValue(false)
            if (success) {
                ad?.let {
                    FinishActivity.newInstance(v.context, it)
                }
            }
        }
    }

    fun onEditAdClickedListener(
        v: Activity, images: List<File>, deletedPhotos: List<String>?, thumbnailType: String?,
        thumbnailId: String?,edDesc : CustomEditText,isAllComments:Boolean,      isDefaultVideo: Boolean

    ) {
        isLoading.postValue(true)

        repository.editAd(
            ad?._id ?: "",
            etTitle.get() ?: "",
            etPrice.get() ?: "",
            edDesc.text ?: "",
            selectedCat?._id ?: "",
            selectedType?._id,
            selectedSubCat?._id,
            selectedRegion?._id,
            selectedCityAdLocation?._id,
            selectedCarMake?._id,
            selectedModelLocal?._id,
            selectedSubModelLocal?._id,
            selectedYear?._id ?: "",
            selectedMotion?._id,
            selectedEngine?._id,
            selectedKm?._id,
            lat?.toString(),
            lng?.toString(),
            address.get(),
            selectedRooms?._id,
            selectedBathRoom?._id,
            images,
            deletedPhotos,
            thumbnailType,
            thumbnailId,
            isAllComments,isDefaultVideo
        ) { success, message, updatedAd ->
            showToast.postValue(message)
            isLoading.postValue(false)
            if (success) {
                updatedAd?.let {
                    (v as AppCompatActivity).setResult(AppCompatActivity.RESULT_OK)
                    v.finish()
                }
            }
        }
    }


}