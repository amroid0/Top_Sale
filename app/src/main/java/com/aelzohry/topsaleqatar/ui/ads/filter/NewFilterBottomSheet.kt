package com.aelzohry.topsaleqatar.ui.ads.filter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ItemFilterBinding
import com.aelzohry.topsaleqatar.databinding.ItemLocationBinding
import com.aelzohry.topsaleqatar.databinding.NewFilterBottomSheetBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.ui.ads.AdsFragment
import com.aelzohry.topsaleqatar.ui.ads.ViewModelFactory
import com.aelzohry.topsaleqatar.ui.carShows.view.CarShowsFragment
import com.aelzohry.topsaleqatar.ui.mapAndSearchList.MapAndSearchListFragment
import com.aelzohry.topsaleqatar.ui.search.SearchFragment
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog1
import com.aelzohry.topsaleqatar.utils.extenions.setupListDialogs
import com.aelzohry.topsaleqatar.utils.extenions.setupListDialogs1
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.apmem.tools.layouts.FlowLayout
import kotlin.system.measureTimeMillis


/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class NewFilterBottomSheet : BaseBottomSheet<NewFilterBottomSheetBinding, NewFilterViewModel>() {

  private var catDialog: Dialog? = null
  private var subCatDialog: Dialog? = null
  private var typeDialog: Dialog? = null
  private var makesDialog: Dialog? = null
  private var modelDialog: Dialog? = null
  private var subModelDialog: Dialog? = null
  private var citiesAdLocationDialog: Dialog? = null
  private var carStepDialog: Dialog? = null

  private var yearDialog: Dialog? = null
  private var gearboxDialog: Dialog? = null
  private var engineSizeDialog: Dialog? = null
  private var kmDialog: Dialog? = null

  private var fuelTypeDialog: Dialog? = null
  private var engineDriveSystemDialog: Dialog? = null
  private var carColorDialog: Dialog? = null

  private var regionDialog: Dialog? = null
  private lateinit var thumbView: View

  override fun onExpand(){
    vm.loadData()
  }
  override fun getLayoutID(): Int = R.layout.new_filter_bottom_sheet

  override fun getViewModel(): NewFilterViewModel = ViewModelProvider(
    this,
    ViewModelFactory(
      arguments?.getParcelable(ARG_CATEGORY),
      loadCategory = false,
     regoinList = arguments?.getParcelableArrayList(LIST_REGOIN),
    )
  )[NewFilterViewModel::class.java]

  fun clearAllDummyData() {
    binding.llMainCatData.removeAllViews()
    binding.llSubCatData.removeAllViews()
    binding.llCityData.removeAllViews()
    binding.llTypeData.removeAllViews()
    binding.llRegionData.removeAllViews()
    binding.llYearData.removeAllViews()
    binding.llGearboxData.removeAllViews()
    binding.llEngineSizeData.removeAllViews()
    binding.llKmData.removeAllViews()
    binding.llFueltTypeData.removeAllViews()
    binding.llEngineDriveSystemData.removeAllViews()
    binding.llColorData.removeAllViews()
//        binding.llMakeData.removeAllViews()
  }

  private fun setupFullHeight() {
    val bottomSheetDialog = dialog as BottomSheetDialog
    val behavior = bottomSheetDialog.behavior
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
  }


  fun drawMainCategrios() {
    binding.llMainCatData.removeAllViews()
    val categoryList = Helper.getCategoryList()
    val list = ArrayList<LocalStanderModel>()
    categoryList.forEach {
      list.add(
        LocalStanderModel(
          it._id,
          LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)
        )
      )
      binding.llMainCatData.addView(getViewForMainCategoryField(it, binding.llMainCatData, false))
    }
  }

  override fun setupUI() {
    val  time = measureTimeMillis {
      clearAllDummyData()
    //drawMainCategrios()


    binding.flowLocations.removeAllViews()
    vm.selectedLatLng = arguments?.getParcelable(ARG_LAT_LNG)

    arguments?.let {
      vm.distance = it.getInt(ARG_DISTANCE, 1)
    }

    vm.selectedLatLng.let {
      setTextLocation(it, vm.distance)
    }

    vm.selectedSubCat = arguments?.getParcelable(ARG_SELECTED_SUB_CAT)
    vm.selectedType = arguments?.getParcelable(ARG_SELECTED_TYPE)
    vm.selectedCarMakeItem = arguments?.getParcelable(ARG_SELECTED_CAR_MAKE)
    vm.selectedRegion = arguments?.getParcelable(ARG_SELECTED_REGION)
    vm.selectedCityAdLocation = arguments?.getParcelable(ARG_SELECTED_CITY)
    vm.selectedModelLocal = arguments?.getParcelableArrayList(ARG_SELECTED_MODEL)
    vm.selectedSubModelLocal = arguments?.getParcelableArrayList(ARG_SELECTED_SUB_MODEL)
    vm.selectedCarShow = arguments?.getParcelable(ARG_SELECTED_CAR_SHOW)
    vm.selectedYear = arguments?.getParcelableArrayList(ARG_SELECTED_YEAR)



    vm.selectedGearbox = arguments?.getParcelableArrayList(ARG_SELECTED_GEARBOX)
    vm.selectedEngineSize = arguments?.getParcelableArrayList(ARG_SELECTED_ENGINE_SIZE)
    vm.selectedKm = arguments?.getParcelableArrayList(ARG_SELECTED_KM)

    vm.selectedFuelType = arguments?.getParcelableArrayList(ARG_SELECTED_FULE_TYPE)
    vm.selectedEngineDriveSystem =
      arguments?.getParcelableArrayList(ARG_SELECTED_ENGINE_DRIVE_SYSTEM)
    vm.selectedCarColor = arguments?.getParcelableArrayList(ARG_SELECTED_CAR_COLOR)

    vm.etFromPrice.set(arguments?.getString(ARG_FROM_PRICE))
    vm.etToPrice.set(arguments?.getString(ARG_TO_PRICE))

    vm.etFromRooms.set(arguments?.getString(ARG_FROM_ROOMS))
    vm.etFromRooms.set(arguments?.getString(ARG_TO_ROOMS))

    vm.etFromBathRooms.set(arguments?.getString(ARG_FROM_BATHROOMS))
    vm.etToBathRooms.set(arguments?.getString(ARG_TO_BATH_ROOMS))

    vm.listLocations = arguments?.getParcelableArrayList(LIST_LOCATIONS)

    vm.listLocations?.let {
      addListLocations(it, true)
    }


    vm.selectedCatObservable.observe(this) {
      it?.let {
        it.isLocationRequired?.let { it1 ->
          run {
            binding.llLocation.setVisible(it1)
            binding.llCity.setVisible(!it1)
          }
        }
      }
    }


    //vm.refreshTitles()
    initSeekBar()

    setSavedFilterData()
  }
    Log.d("-bottomsheet-", "setupUI: $time")

  }

  fun initSeekBar() {
    thumbView = LayoutInflater.from(context).inflate(R.layout.custom_thumb_seek_bar, null, false)

  }

  fun setSavedFilterData() {

    vm.selectedCarShow?.let {
      binding.tvCarShowList.setText(it._name)
    }

//        vm.selectedCarMakeItem?.let {
//            vm.selectedMakeModelCategoryText.set( vm.selectedCarMakeItem?.title + "  ,  " + vm.selectedModelItem?.title + "  ,  " + vm.selectedSubModelItem?.title)
//        }

  }

  override fun onClickedListener() {

    binding.llMainCat.setOnClickListener { catDialog?.show() }

    binding.llMake.setOnClickListener {
      makesDialog?.show()
    }

//        binding.llMakeModelCategory.setOnClickListener {
//            vm.carMakesRes.value?.let {
//                val list = ArrayList<StanderModel>()
//                it.forEach {
//                    list.add(StanderModel(it._id, it.localized))
//                }
//                setupCarStepDialog(list)
//                carStepDialog?.show()
//
//            }
//        }

    binding.llCarShow.setOnClickListener { onCarShowClick() }

    binding.llModel.setOnClickListener {
      modelDialog?.show()
    }

    binding.llSubModel.setOnClickListener {
      subModelDialog?.show()
    }

    binding.llRegion.setOnClickListener {
      regionDialog?.show()
    }

    binding.llSubCat.setOnClickListener {
      subCatDialog?.show()
    }

    binding.llType.setOnClickListener {
      typeDialog?.show()
    }

    binding.llYear.setOnClickListener {
      yearDialog?.show()
    }

    binding.llGearbox.setOnClickListener {
      gearboxDialog?.show()
    }

    binding.llEngineSize.setOnClickListener {
      engineSizeDialog?.show()
    }

    binding.llKm.setOnClickListener {
      kmDialog?.show()
    }

    binding.llFuelType.setOnClickListener {
      fuelTypeDialog?.show()
    }

    binding.llEngineDriveSystem.setOnClickListener {
      engineDriveSystemDialog?.show()
    }

    binding.llCarColor.setOnClickListener {
      carColorDialog?.show()
    }

    binding.llLocation.setOnClickListener {
//            val dialog: SelectLocationDialogFragment = SelectLocationDialogFragment.newInstance()
//            dialog.setListener(object : SelectLocationDialogFragment.LocationListener {
//                override fun onLocationSelected(location: LatLng?, distance: Int) {
//                    setTextLocation(location, distance)
//                }
//
//            })
//            childFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
      openAutoCompleteScreen()
    }

    binding.llCity.setOnClickListener { citiesAdLocationDialog?.show() }


    binding.btnAll.setOnClickListener {
      getViewModel().onResetBtnClickedListener()
      binding.flowLocations.removeAllViews()
      if (requireActivity() is AdsFragment) (requireActivity() as AdsFragment).onFilterClickedApplyListener(
        vm.selectedCat,
        vm.selectedSubCat,
        vm.selectedType,
        vm.selectedCarMakeItem,
        vm.selectedModelLocal,
        vm.selectedSubModelLocal,
        vm.selectedCarShow,
        vm.selectedYear,
        vm.selectedGearbox,
        vm.selectedEngineSize,

        vm.selectedFuelType,
        vm.selectedEngineDriveSystem,
        vm.selectedCarColor,

        vm.selectedKm,
        vm.selectedRegion,
        vm.selectedCityAdLocation,
        vm.etFromPrice.get(),
        vm.etToPrice.get(),
        vm.etFromRooms.get(),
        vm.etToRooms.get(),
        vm.etFromBathRooms.get(),
        vm.etToBathRooms.get(),
        vm.selectedLatLng,
        vm.distance,
        getItems()
      )

      if (requireActivity() is SearchFragment) (requireActivity() as SearchFragment).onFilterClickedApplyListener(
        vm.selectedCat,
        vm.selectedSubCat,
        vm.selectedType,
        vm.selectedCarMakeItem,
        vm.selectedModelLocal,
        vm.selectedSubModelLocal,
        vm.selectedCarShow,
        vm.selectedYear,
        vm.selectedGearbox,
        vm.selectedEngineSize,

        vm.selectedFuelType,
        vm.selectedEngineDriveSystem,
        vm.selectedCarColor,

        vm.selectedKm,
        vm.selectedRegion,
        vm.selectedCityAdLocation,
        vm.etFromPrice.get(),
        vm.etToPrice.get(),
        vm.etFromRooms.get(),
        vm.etToRooms.get(),
        vm.etFromBathRooms.get(),
        vm.etToBathRooms.get(),
        vm.selectedLatLng,
        vm.distance,
        getItems()
      )
      dismiss()
    }

    binding.btnApply.setOnClickListener {
      if (requireActivity() is AdsFragment) (requireActivity() as AdsFragment).onFilterClickedApplyListener(
        vm.selectedCat,
        vm.selectedSubCat,
        vm.selectedType,
        vm.selectedCarMakeItem,
        vm.selectedModelLocal,
        vm.selectedSubModelLocal,
        vm.selectedCarShow,
        vm.selectedYear,
        vm.selectedGearbox,
        vm.selectedEngineSize,
        vm.selectedFuelType,
        vm.selectedEngineDriveSystem,
        vm.selectedCarColor,

        vm.selectedKm,
        vm.selectedRegion,
        vm.selectedCityAdLocation,
        vm.etFromPrice.get(),
        vm.etToPrice.get(),
        vm.etFromRooms.get(),
        vm.etToRooms.get(),
        vm.etFromBathRooms.get(),
        vm.etToBathRooms.get(),
        vm.selectedLatLng,
        vm.distance,
        getItems()
      )

      if (requireActivity() is SearchFragment) (requireActivity() as SearchFragment).onFilterClickedApplyListener(
        vm.selectedCat,
        vm.selectedSubCat,
        vm.selectedType,
        vm.selectedCarMakeItem,
        vm.selectedModelLocal,
        vm.selectedSubModelLocal,
        vm.selectedCarShow,
        vm.selectedYear,
        vm.selectedGearbox,
        vm.selectedEngineSize,
        vm.selectedFuelType,
        vm.selectedEngineDriveSystem,
        vm.selectedCarColor,
        vm.selectedKm,
        vm.selectedRegion,
        vm.selectedCityAdLocation,
        vm.etFromPrice.get(),
        vm.etToPrice.get(),
        vm.etFromRooms.get(),
        vm.etToRooms.get(),
        vm.etFromBathRooms.get(),
        vm.etToBathRooms.get(),
        vm.selectedLatLng,
        vm.distance,
        getItems()
      )
      dismiss()
    }

    binding.backBtn.setOnClickListener {
      dismiss()
    }

  }

//    private fun setupCarStepDialog(carMakesList:ArrayList<StanderModel>) {
//
//        carStepDialog = Dialog(requireContext())
//        val b = DataBindingUtil.inflate<FilterCatCarFourAttrBinding>(layoutInflater, R.layout.filter_cat_car_four_attr, null, false)
//        b.setVariable(BR.vm, vm)
//        b.executePendingBindings()
//        carStepDialog?.setContentView(b.root)
//        carStepDialog?.setCancelable(false)
//
//        carStepDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//
//
//        b.btnBack.setOnClickListener {
//            when (vm.currentStep.get()) {
//                1 -> setupMakeStep(b,carMakesList)
//                2 -> setupModelStep(b, vm.carModelRes.value)
//
//                else -> { // Note the block
//                    print("x is neither 1 nor 2")
//                }
//            }
//        }
//
//        setupMakeStep(b,carMakesList)
//
//    }

//    private fun setupMakeStep(bind: FilterCatCarFourAttrBinding,carMakesList:ArrayList<StanderModel>) {
//        vm.tvTitle.set(getString(R.string.car_make))
//        vm.currentStep.set(0)
//        bind.btnBack.setVisible(false)
//        vm.isLoading.postValue(true)
//
//            vm.isLoading.postValue(false)
//            bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm,carMakesList) { b, m, i, a ->
//                b.root.setOnClickListener {
//                    vm.isLoading.postValue(true)
//
//                    vm.selectedCarMakeItem = m
//                    vm.onMakeSelectedListener(m) { data ->
//                        vm.isLoading.postValue(false)
//                        setupModelStep(bind, data)
//                    }
//                }
//            }
//
//
//    }
//
//    private fun setupModelStep(bind: FilterCatCarFourAttrBinding, data: List<StanderModel>?) {
//        vm.tvTitle.set(getString(R.string.car_model))
//        vm.currentStep.set(1)
//        bind.btnBack.setVisible(true)
//        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, data) { b, m, i, a ->
//            b.root.setOnClickListener {
//
//                vm.onModelSelectedListener(m) { data ->
//                    vm.isLoading.postValue(false)
//                    setupSubModelStep(bind, data)
//                }
//            }
//        }
//    }
//
//    private fun setupSubModelStep(bind: FilterCatCarFourAttrBinding, data: List<StanderModel>) {
//        vm.tvTitle.set(getString(R.string.car_sub_model))
//        vm.currentStep.set(2)
//        bind.btnBack.setVisible(true)
//        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, data) { b, m, i, a ->
//            b.root.setOnClickListener {
//
//                vm.onSubModelSelectedListener(m)
//                vm.selectedMakeModelCategoryText.set( vm.selectedCarMakeItem?.title + "  ,  " + vm.selectedModelItem?.title + "  ,  " + vm.selectedSubModelItem?.title)
//                carStepDialog?.dismiss()
//
//            }
//        }
//    }

  fun onCarShowClick() {
    val dialog: CarShowsFragment = CarShowsFragment.newInstance()
    childFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
    dialog.setListener(object : CarShowsFragment.Listener {
      override fun onItemSelected(user: User) {
        if (user.isChecked) {
          vm.selectedCarShow = user
          binding.tvCarShowList.text = user._name
        } else {
          vm.selectedCarShow = null
          binding.tvCarShowList.text = ""
        }

      }

    })
  }

  fun setTextLocation(location: LatLng?, distance: Int) {
    if (location != null) {
      vm.selectedLatLng = location
      vm.distance = distance
      if (context != null) {
        vm.selectLocationText.set(
          String.format(
            getString(R.string.distance_f),
            distance.toString(),
            Helper.getAddress(location, requireContext())
          )
        )
      } else {
        vm.selectLocationText.set(
          String.format(
            getString(R.string.distance_f),
            distance.toString(),
            getString(R.string.unknown_address)
          )
        )
      }

    }
  }

  override fun observerLiveData() {

    //region new code
    vm.catsRes.observe(this) {
      binding.llMainCatData.removeAllViews()
      val list = ArrayList<LocalStanderModel>()
      it.forEach {
        list.add(
          LocalStanderModel(
            it._id,
            LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)
          )
        )
        binding.llMainCatData.addView(getViewForMainCategoryField(it, binding.llMainCatData, false))
      }

      catDialog = Dialog(requireContext())
      setupDialog(catDialog, list, { modelLocal: LocalStanderModel, i ->
        it[i].isLocationRequired?.let { it1 -> binding.llLocation.setVisible(it1) }
        vm.onCatSelectedListener(it[i])
      })
    }


    vm.subCatsRes.observe(this) {
      subCatDialog = Dialog(requireContext())
      setupDialog(subCatDialog, it) { modelLocal: LocalStanderModel ->
        getViewModel().selectedSubCat = modelLocal
        getViewModel().selectSubCatText.set(modelLocal.title.localized)
      }

      binding.llSubCatData.removeAllViews()
      val list = ArrayList<LocalStanderModel>()
      it.forEach {
        list.add(
          LocalStanderModel(
            it._id,
            LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)
          )
        )
        binding.llSubCatData.addView(getViewForSubCategoryField(it, binding.llSubCatData, false))
      }
    }

    vm.typeRes.observe(this) {
      binding.llTypeData.removeAllViews()
      it.forEach {
        binding.llTypeData.addView(getViewForTypField(it, binding.llTypeData, false))
      }

//            typeDialog = Dialog(requireContext())
//            setupDialog(typeDialog, it) { modelLocal: LocalStanderModel ->
//                getViewModel().selectedType = modelLocal
//                getViewModel().selectedTypeText.set(modelLocal.title.localized)
////                5f70d8a6ddd69f66a0387c69 // for rent
//            }
    }


    vm.regionRes.observe(this) {

      binding.llRegionData.removeAllViews()
      it.forEach {
        binding.llRegionData.addView(getViewForRegionField(it, binding.llRegionData, false))
      }


      regionDialog = Dialog(requireContext())
      setupDialog(regionDialog, it) { modelLocal: LocalStanderModel ->
        getViewModel().selectedRegion = modelLocal
//                getViewModel().selectedRegionText.set(modelLocal.title.localized)
      }
    }

    vm.citiesRes.observe(this) {
      binding.llCityData.removeAllViews()
      val list = ArrayList<LocalStanderModel>()
      it.forEach {
        list.add(
          LocalStanderModel(
            it._id,
            LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)
          )
        )
      }


      list.forEach {
        binding.llCityData.addView(getViewForCityField(it, binding.llCityData, false))
      }

      citiesAdLocationDialog = Dialog(requireContext())
      setupDialog(citiesAdLocationDialog, list, { modelLocal: LocalStanderModel, i ->
        vm.onCityAdLocationSelectedListener(modelLocal)
      })
    }



    vm.carMakesRes.observe(this) {
//            binding.llMakeData.removeAllViews()
//            val list = ArrayList<StanderModel>()
//            it.forEach {
//                list.add(StanderModel(it._id, it.localized))
//            }
//
//            setupCarStepDialog(list)
//            list.forEach {
//                binding.llMakeData.addView(getViewForCarMakeField(it, binding.llMakeData))
//            }

      makesDialog = Dialog(requireContext())
      setupDialog1(makesDialog, it) { modelLocal: StanderModel ->
        vm.selectedCarMakeItem = modelLocal
        binding.llModel.isVisible = vm.selectedCarMakeItem != null

        vm.selectedCarMakeText.set(modelLocal.title)
        vm.loadCarModel(modelLocal._id)
        vm.selectedModelLocal = null
        vm.selectedSubModelLocal = null
        vm.selectedModelText.set("")
        vm.selectedSubModelText.set("")
        vm.carCatMakeState.set(true)
      }
    }

    /* multi*/
    vm.yearRes.observe(this) {
      binding.llYearData.removeAllViews()
      it.forEach {
        binding.llYearData.addView(getViewForYearField(it))
      }

      yearDialog = Dialog(requireContext())
      setupListDialogs(yearDialog, it) { model ->
        getViewModel().selectedYear = model
        vm.refreshYears()
      }
    }

    vm.gearboxRes.observe(this) {

      binding.llGearboxData.removeAllViews()
      it.forEach {
        binding.llGearboxData.addView(getViewForGearBoxField(it))
      }


      gearboxDialog = Dialog(requireContext())
      setupListDialogs(gearboxDialog, it) { model ->
        getViewModel().selectedGearbox = model
        vm.refreshGearbox()

      }
    }


    vm.engineSizeRes.observe(this) {
      binding.llEngineSizeData.removeAllViews()
      it.forEach {
        binding.llEngineSizeData.addView(getViewForEngineSizeField(it))
      }

      engineSizeDialog = Dialog(requireContext())
      setupListDialogs(engineSizeDialog, it) { model ->
        getViewModel().selectedEngineSize = model
        vm.refreshEngineSize()

      }
    }

    vm.kmRes.observe(this) {
      binding.llKmData.removeAllViews()
      it.forEach {
        binding.llKmData.addView(getViewForKmField(it))
      }


      kmDialog = Dialog(requireContext())
      setupListDialogs(kmDialog, it) { model ->
        getViewModel().selectedKm = model
        vm.refreshKm()

      }
    }

    vm.fuelTypeRes.observe(this) {
      binding.llFueltTypeData.removeAllViews()
      it.forEach {
        binding.llFueltTypeData.addView(getViewForFuelTypeField(it))
      }


      fuelTypeDialog = Dialog(requireContext())
      setupListDialogs(fuelTypeDialog, it) { model ->
        getViewModel().selectedFuelType = model
        vm.refreshFuelType()
      }
    }

    vm.carEngineDriveSystemRes.observe(this) {

      binding.llEngineDriveSystemData.removeAllViews()
      it.forEach {
        binding.llEngineDriveSystemData.addView(getViewForEngineDriveSystemField(it))
      }


      engineDriveSystemDialog = Dialog(requireContext())
      setupListDialogs(engineDriveSystemDialog, it) { model ->
        getViewModel().selectedEngineDriveSystem = model
        vm.refreshEngineDriveSystem()
      }
    }

    vm.carColorRes.observe(this) {
      binding.llColorData.removeAllViews()
      it.forEach {
        binding.llColorData.addView(getViewForCarColorField(it))
      }


      carColorDialog = Dialog(requireContext())
      setupListDialogs(carColorDialog, it) { model ->
        getViewModel().selectedCarColor = model
        vm.refreshCarColor()
      }
    }

    /* multi*/
    vm.modelRes.observe(this) {

//            binding.llModelData.removeAllViews()
//
//            val list = ArrayList<StanderModel>()
//            it.forEach {
//                list.add(StanderModel(it._id, it.localized))
//            }
//
//
//            list.forEach {
//                binding.llModelData.addView(getViewForCarModelField(it))
//            }

      modelDialog = Dialog(requireContext())
      setupListDialogs1(modelDialog, it) { model ->
        vm.selectedModelLocal = model
        binding.llSubModel.isVisible = vm.selectedModelLocal != null

        vm.selectedSubModelLocal = null
        vm.refreshModel()

      }
    }

    vm.carSubModelRes.observe(this) {

//            binding.llCarCategoryData.removeAllViews()
//
//            val list = ArrayList<StanderModel>()
//            it.forEach {
//                list.add(StanderModel(it._id, it.localized))
//            }
//
//
//            list.forEach {
//                binding.llCarCategoryData.addView(getViewForCarSubModelField(it))
//            }

      subModelDialog = Dialog(requireContext())
      setupListDialogs1(subModelDialog, it) { model ->
        getViewModel().selectedSubModelLocal = model
        vm.refreshSubModel()
      }
    }

    //endregion new code


  }

  override fun isFullHeight(): Boolean = true

  companion object {

    private const val ARG_LAT_LNG = "SELECTED_LAT_LNG"
    private const val ARG_DISTANCE = "DISTANCE"
    private const val ARG_CATEGORY = "ARG_CATEGORY"
    private const val ARG_SELECTED_SUB_CAT = "ARG_SELECTED_SUB_CAT"
    private const val ARG_SELECTED_TYPE = "ARG_SELECTED_TYPE"
    private const val ARG_SELECTED_CAR_MAKE = "ARG_SELECTED_CAR_MAKE"
    private const val ARG_SELECTED_MODEL = "ARG_SELECTED_MODEL"
    private const val ARG_SELECTED_SUB_MODEL = "ARG_SELECTED_SUB_MODEL"
    private const val ARG_SELECTED_CAR_SHOW = "ARG_SELECTED_CAR_SHOW"
    private const val ARG_SELECTED_YEAR = "ARG_SELECTED_YEAR"
    private const val ARG_SELECTED_GEARBOX = "ARG_SELECTED_GEARBOX"
    private const val ARG_SELECTED_ENGINE_SIZE = "ARG_SELECTED_ENGINE_SIZE"
    private const val ARG_SELECTED_KM = "ARG_SELECTED_Km"

    private const val ARG_SELECTED_FULE_TYPE = "ARG_SELECTED_FULE_TYPE"
    private const val ARG_SELECTED_ENGINE_DRIVE_SYSTEM = "ARG_SELECTED_ENGINE_DRIVE_SYSTEM"
    private const val ARG_SELECTED_CAR_COLOR = "ARG_SELECTED_CAR_COLOR"

    private const val ARG_SELECTED_REGION = "ARG_SELECTED_REGION"
    private const val ARG_SELECTED_CITY = "ARG_SELECTED_CITY"
    private const val ARG_FROM_PRICE = "FROM_PRICE"
    private const val ARG_TO_PRICE = "TO_PRICE"
    private const val ARG_FROM_ROOMS = "FROM_ROOMS"
    private const val ARG_TO_ROOMS = "TO_ROOMS"
    private const val ARG_FROM_BATHROOMS = "FROM_BATHROOMS"
    private const val ARG_TO_BATH_ROOMS = "TO_BATH_ROOMS"
    private const val ARG_SORT = "SORT"
    private const val LOAD_CATEGORY = "LOAD_CATEGORY"
    private const val LIST_LOCATIONS = "LIST_LOCATIONS"
    private const val LIST_REGOIN = "LIST_REGOIN"

    @JvmStatic
    fun newInstance(
      category: Category?,
      selectedSubCat: LocalStanderModel?,
      selectedType: LocalStanderModel?,
      selectedCarMake: StanderModel?,
      selectedModelLocal: ArrayList<StanderModel>?,
      selectedSubModelLocal: ArrayList<StanderModel>?,
      selectedCarShow: User?,
      selectedYear: ArrayList<StanderModel>?,
      selectedGearbox: ArrayList<StanderModel>?,
      selectedEngineSize: ArrayList<StanderModel>?,

      selectedFuelType: ArrayList<StanderModel>?,
      selectedEngineDriveSystem: ArrayList<StanderModel>?,
      selectedCarColor: ArrayList<StanderModel>?,

      selectedKm: ArrayList<StanderModel>?,
      selectedRegion: LocalStanderModel?,
      selectedCity: LocalStanderModel?,
      loadCategory: Boolean = true,
      fromPrice: String?,
      toPrice: String?,
      fromRooms: String?,
      toRooms: String?,
      fromBathRooms: String?,
      toBathRooms: String?,
      selectedLatLng: LatLng?,
      distance: Int,
      locationList: ArrayList<PlaceAutocomplete>?,
      regoinList: ArrayList<LocalStanderModel>?

    ) = NewFilterBottomSheet().apply {
      arguments = Bundle().apply {
        putParcelable(ARG_LAT_LNG, selectedLatLng)
        putInt(ARG_DISTANCE, distance)
        putParcelable(ARG_CATEGORY, category)
        putParcelable(ARG_SELECTED_SUB_CAT, selectedSubCat)
        putParcelable(ARG_SELECTED_TYPE, selectedType)
        putParcelable(ARG_SELECTED_CAR_MAKE, selectedCarMake)
        putParcelableArrayList(ARG_SELECTED_MODEL, selectedModelLocal)
        putParcelableArrayList(ARG_SELECTED_SUB_MODEL, selectedSubModelLocal)
        putParcelable(ARG_SELECTED_CAR_SHOW, selectedCarShow)
        putParcelableArrayList(ARG_SELECTED_YEAR, selectedYear)
        putParcelableArrayList(ARG_SELECTED_GEARBOX, selectedGearbox)
        putParcelableArrayList(ARG_SELECTED_ENGINE_SIZE, selectedEngineSize)

        putParcelableArrayList(ARG_SELECTED_FULE_TYPE, selectedFuelType)
        putParcelableArrayList(ARG_SELECTED_ENGINE_DRIVE_SYSTEM, selectedEngineDriveSystem)
        putParcelableArrayList(ARG_SELECTED_CAR_COLOR, selectedCarColor)


        putParcelableArrayList(ARG_SELECTED_KM, selectedKm)
        putParcelable(ARG_SELECTED_REGION, selectedRegion)
        putParcelable(ARG_SELECTED_CITY, selectedCity)
        putString(ARG_FROM_PRICE, fromPrice)
        putString(ARG_TO_PRICE, toPrice)
        putString(ARG_FROM_ROOMS, fromRooms)
        putString(ARG_TO_ROOMS, toRooms)
        putString(ARG_FROM_BATHROOMS, fromBathRooms)
        putString(ARG_TO_BATH_ROOMS, toBathRooms)
        putBoolean(LOAD_CATEGORY, loadCategory)

        putParcelableArrayList(LIST_LOCATIONS, locationList)
        putParcelableArrayList(LIST_REGOIN, regoinList)

      }
    }
  }

  private fun openAutoCompleteScreen() {
//        val fields = Arrays.asList(
//            Place.Field.ID,
//            Place.Field.NAME,
//            Place.Field.ADDRESS,
//            Place.Field.LAT_LNG
//        )
//        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
//        intent.setCountries(Arrays.asList("QA"))
//        activityLauncher.launch(intent.build(requireContext())) { result ->
//            if (result.resultCode === AppCompatActivity.RESULT_OK) {
//                if (result.data != null) {
//                    val place = Autocomplete.getPlaceFromIntent(result.data)
//                    place.let {
//                        addNewLocation(it.name,it.latLng as LatLng)
//                    }
//                }
//
//            }
//        }

//        val dialogFragment: SelectMultipleLocationFragment = SelectMultipleLocationFragment.newInstance()
//        dialogFragment.setListener(object : SelectMultipleLocationFragment.Listener {
//            override fun onItemSelected(list: ArrayList<PlaceAutocomplete>) {
//                addListLocations(list)
//            }
//
//        })
//
//
//        dialogFragment.show(childFragmentManager, SelectMultipleLocationFragment::class.java.getSimpleName())

    val dialogFragment: MapAndSearchListFragment = MapAndSearchListFragment.newInstance()
    dialogFragment.setListener(object : MapAndSearchListFragment.SearchResult {
      override fun passLocations(list: ArrayList<PlaceAutocomplete>) {
        addListLocations(list)
      }

      override fun passLatLng(item: LatLng) {
//                addNewLocation(it.name,it.latLng as LatLng)
      }

    })
//        dialogFragment.setListener(object : SelectMultipleLocationFragment.Listener {
//            override fun onItemSelected(list: ArrayList<PlaceAutocomplete>) {
//                addListLocations(list)
//            }
//
//        })

    dialogFragment.show(childFragmentManager, MapAndSearchListFragment::class.java.getSimpleName())

  }

  private fun addListLocations(list: ArrayList<PlaceAutocomplete>, isNew: Boolean = false) {
    if (isNew) binding.flowLocations.removeAllViews()

    for (item in list) {
      addNewLocation(item)
    }
  }

  private fun addNewLocation(item: PlaceAutocomplete) {
    if (!isItemAdded(item)) {
      binding.flowLocations.addView(getViewForLocationField(item, binding.flowLocations))
      updateLocationViewsUi()
    }
  }

  private fun isItemAdded(itemToCheck: PlaceAutocomplete): Boolean {
    for (i in 0 until binding.flowLocations.childCount) {
      val v: View = binding.flowLocations.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val item = tvText.tag as PlaceAutocomplete

      if (TextUtils.isEmpty(itemToCheck.placeId)) {
        return false
      }
      if (itemToCheck.placeId.equals(item.placeId)) {
        return true
      }

    }
    return false
  }

  private fun getViewForLocationField(item: PlaceAutocomplete, flowFilterFields: FlowLayout): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemLocationBinding = ItemLocationBinding.inflate(vi)
    val view: View = binding.getRoot()
    if (item.nameDetails != null) {
      binding.tvText.text = item.nameDetails.mainText
    } else {
      binding.tvText.text = item.description
    }
    binding.tvText.setTag(item)


    binding.fBackground.setOnClickListener { v ->
      flowFilterFields.removeView(view)
      updateLocationViewsUi()
    }
    return view
  }

  private fun updateLocationViewsUi() {
    binding.flowLocations.setVisible(binding.flowLocations.childCount > 0)
//        binding.tvLocation.setVisible(binding.flowLocations.childCount==0)

  }

  private fun getItems(): ArrayList<PlaceAutocomplete> {
    val list = ArrayList<PlaceAutocomplete>()
    for (i in 0 until binding.flowLocations.childCount) {
      val v: View = binding.flowLocations.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val item = tvText.tag as PlaceAutocomplete
      list.add(item)

    }
    return list
  }

  //region draw views
  private fun getViewForMainCategoryField(
    item: Category,
    llContainer: LinearLayoutCompat,
    isMultipleSelect: Boolean
  ): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title.localized
    binding.tvText.setTag(item)


    vm.selectedCat?.let {
      if (item._id.equals(it._id)) {
        vm.onCatSelectedListener(it)
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      if (isMultipleSelect) {
        val category = binding.tvText.getTag() as Category?
        category?.let {
          if (it.isSelected == true) {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
            it.isSelected = false
          } else {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
            it.isSelected = true
          }
          binding.tvText.setTag(it)
        }
      } else {
        updateCategoryItemSelection(item, llContainer)
      }
    }
    return view
  }

  private fun updateCategoryItemSelection(clickedItem: Category, llContainer: LinearLayoutCompat) {
    val childCount = llContainer.childCount
    for (i in 0 until childCount) {
      val v = llContainer.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val llRoot = v.findViewById<LinearLayoutCompat>(R.id.f_background)
      val category: Category = tvText.tag as Category
      category.isSelected = category._id.equals(clickedItem._id, true)

      if (category.isSelected) {
        vm.onCatSelectedListener(clickedItem)
        llRoot.setBackgroundResource(R.drawable.filter_item_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        llRoot.setBackgroundResource(R.drawable.filter_item_un_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))

      }
    }

  }

  private fun getViewForSubCategoryField(
    item: LocalStanderModel,
    llContainer: LinearLayoutCompat,
    isMultipleSelect: Boolean
  ): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title.localized
    binding.tvText.setTag(item)


    vm.selectedCat?.let {
      if (item._id.equals(it._id)) {
        vm.onCatSelectedListener(it)
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      if (isMultipleSelect) {
        val category = binding.tvText.getTag() as Category?
        category?.let {
          if (it.isSelected == true) {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
            it.isSelected = false
          } else {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
            it.isSelected = true
          }
          binding.tvText.setTag(it)
        }
      } else {
        updateSubCategoryItemSelection(item, llContainer)
      }
    }
    return view
  }

  private fun updateSubCategoryItemSelection(
    clickedItem: LocalStanderModel,
    llContainer: LinearLayoutCompat
  ) {
    val childCount = llContainer.childCount
    for (i in 0 until childCount) {
      val v = llContainer.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val llRoot = v.findViewById<LinearLayoutCompat>(R.id.f_background)
      val category: LocalStanderModel = tvText.tag as LocalStanderModel
      category.isChecked = category._id.equals(clickedItem._id, true)

      if (category.isChecked) {

//                vm.onCatSelectedListener(clickedCategoryItem)
        vm.selectedSubCat = clickedItem

        llRoot.setBackgroundResource(R.drawable.filter_item_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        llRoot.setBackgroundResource(R.drawable.filter_item_un_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))

      }
    }

  }

  private fun getViewForTypField(
    item: LocalStanderModel,
    llContainer: LinearLayoutCompat,
    isMultipleSelect: Boolean
  ): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title.localized
    binding.tvText.setTag(item)


    Log.e("test_type_filter", vm.selectedType?._id + "  " + item._id)

    vm.selectedType?.let {
      if (item._id.equals(it._id)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      if (isMultipleSelect) {
        val taggedItem = binding.tvText.getTag() as LocalStanderModel?
        taggedItem?.let {
          if (it.isChecked == true) {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
            it.isChecked = false
          } else {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
            it.isChecked = true
          }
          binding.tvText.setTag(it)
        }
      } else {
        updateTypeItemSelection(item, llContainer)
      }
    }
    return view
  }

  private fun updateTypeItemSelection(
    clickedItem: LocalStanderModel,
    llContainer: LinearLayoutCompat
  ) {
    val childCount = llContainer.childCount
    for (i in 0 until childCount) {
      val v = llContainer.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val llRoot = v.findViewById<LinearLayoutCompat>(R.id.f_background)
      val taggedItem: LocalStanderModel = tvText.tag as LocalStanderModel
      taggedItem.isChecked = taggedItem._id.equals(clickedItem._id, true)

      if (taggedItem.isChecked) {
        vm.selectedType = clickedItem
        llRoot.setBackgroundResource(R.drawable.filter_item_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        llRoot.setBackgroundResource(R.drawable.filter_item_un_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))
      }
    }

  }

  private fun getViewForRegionField(
    item: LocalStanderModel,
    llContainer: LinearLayoutCompat,
    isMultipleSelect: Boolean
  ): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title.localized
    binding.tvText.setTag(item)


    vm.selectedRegion?.let {
      if (item._id.equals(it._id)) {
        vm.selectedRegion = it
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      if (isMultipleSelect) {
        val taggedItem = binding.tvText.getTag() as LocalStanderModel?
        taggedItem?.let {
          if (it.isChecked == true) {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
            it.isChecked = false
          } else {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
            it.isChecked = true
          }
          binding.tvText.setTag(it)
        }
      } else {
        updateRegionItemSelection(item, llContainer)
      }
    }
    return view
  }

  private fun updateRegionItemSelection(
    clickedItem: LocalStanderModel,
    llContainer: LinearLayoutCompat
  ) {
    val childCount = llContainer.childCount
    for (i in 0 until childCount) {
      val v = llContainer.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val llRoot = v.findViewById<LinearLayoutCompat>(R.id.f_background)
      val taggedItem: LocalStanderModel = tvText.tag as LocalStanderModel
      taggedItem.isChecked = taggedItem._id.equals(clickedItem._id, true)

      if (taggedItem.isChecked) {
        vm.selectedRegion = clickedItem

        llRoot.setBackgroundResource(R.drawable.filter_item_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        llRoot.setBackgroundResource(R.drawable.filter_item_un_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))
      }
    }

  }

  private fun getViewForCityField(
    item: LocalStanderModel,
    llContainer: LinearLayoutCompat,
    isMultipleSelect: Boolean
  ): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title.localized
    binding.tvText.setTag(item)


    vm.selectedCityAdLocation?.let {
      if (item._id.equals(it._id)) {
        vm.onCityAdLocationSelectedListener(it)

        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      if (isMultipleSelect) {
        val taggedItem = binding.tvText.getTag() as LocalStanderModel?
        taggedItem?.let {
          if (it.isChecked == true) {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
            it.isChecked = false
          } else {
            binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
            it.isChecked = true
          }
          binding.tvText.setTag(it)
        }
      } else {
        updateCityItemSelection(item, llContainer)
      }
    }
    return view
  }

  private fun updateCityItemSelection(
    clickedItem: LocalStanderModel,
    llContainer: LinearLayoutCompat
  ) {
    val childCount = llContainer.childCount
    for (i in 0 until childCount) {
      val v = llContainer.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val llRoot = v.findViewById<LinearLayoutCompat>(R.id.f_background)
      val taggedItem: LocalStanderModel = tvText.tag as LocalStanderModel
      taggedItem.isChecked = taggedItem._id.equals(clickedItem._id, true)

      if (taggedItem.isChecked) {
        vm.onCityAdLocationSelectedListener(clickedItem)

        llRoot.setBackgroundResource(R.drawable.filter_item_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        llRoot.setBackgroundResource(R.drawable.filter_item_un_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))
      }
    }

  }

  private fun getViewForCarMakeField(item: StanderModel, llContainer: LinearLayoutCompat): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedCarMakeItem?.let {
      if (item._id.equals(it._id)) {
        vm.selectedCarMakeItem = it

        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      updateCarMakeSelection(item, llContainer)
    }
    return view
  }

  private fun updateCarMakeSelection(clickedItem: StanderModel, llContainer: LinearLayoutCompat) {
    val childCount = llContainer.childCount
    for (i in 0 until childCount) {
      val v = llContainer.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val llRoot = v.findViewById<LinearLayoutCompat>(R.id.f_background)
      val taggedItem: StanderModel = tvText.tag as StanderModel
      taggedItem.isChecked = taggedItem._id.equals(clickedItem._id, true)

      if (taggedItem.isChecked) {
        vm.selectedCarMakeItem = taggedItem
        binding.llModel.isVisible = vm.selectedCarMakeItem != null


        vm.loadCarModel(taggedItem._id)
//                vm.selectedModelItem = null
//                vm.selectedSubModelItem = null

        llRoot.setBackgroundResource(R.drawable.filter_item_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        llRoot.setBackgroundResource(R.drawable.filter_item_un_selected)
        tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))
      }
    }

  }

  private fun getViewForYearField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedYear?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateYearsList()
      }
    }
    return view
  }

  private fun updateYearsList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llYearData.childCount
    for (i in 0 until childCount) {
      val v = binding.llYearData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedYear = list
    vm.refreshYears()

  }

  private fun getViewForGearBoxField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedGearbox?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateGearBoxList()
      }
    }
    return view
  }

  private fun updateGearBoxList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llGearboxData.childCount
    for (i in 0 until childCount) {
      val v = binding.llGearboxData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedGearbox = list
    vm.refreshGearbox()

  }

  private fun getViewForEngineSizeField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedEngineSize?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateEngineSizeList()
      }
    }
    return view
  }

  private fun updateEngineSizeList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llEngineSizeData.childCount
    for (i in 0 until childCount) {
      val v = binding.llEngineSizeData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedEngineSize = list
    vm.refreshEngineSize()

  }

  private fun getViewForKmField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedKm?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateKmList()
      }
    }
    return view
  }

  private fun updateKmList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llKmData.childCount
    for (i in 0 until childCount) {
      val v = binding.llKmData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedKm = list
    vm.refreshKm()

  }

  private fun getViewForFuelTypeField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedFuelType?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateFuelTypeList()
      }
    }
    return view
  }

  private fun updateFuelTypeList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llFueltTypeData.childCount
    for (i in 0 until childCount) {
      val v = binding.llFueltTypeData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedFuelType = list
    vm.refreshFuelType()

  }

  private fun getViewForEngineDriveSystemField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedEngineDriveSystem?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateEngineDriveSystemList()
      }
    }
    return view
  }

  private fun updateEngineDriveSystemList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llEngineDriveSystemData.childCount
    for (i in 0 until childCount) {
      val v = binding.llEngineDriveSystemData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedEngineDriveSystem = list
    vm.refreshEngineDriveSystem()

  }

  private fun getViewForCarColorField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)


    vm.selectedCarColor?.let {
      if (it.contains(item)) {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
        binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
      } else {
        binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
        binding.tvText.setTextColor(
          ContextCompat.getColor(
            requireContext(),
            R.color.text_color_black
          )
        )
      }
    }


    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateCarColorList()
      }
    }
    return view
  }

  private fun updateCarColorList() {
    val list: ArrayList<StanderModel> = ArrayList()
    val childCount = binding.llColorData.childCount
    for (i in 0 until childCount) {
      val v = binding.llColorData.getChildAt(i)
      val tvText = v.findViewById<TextView>(R.id.tv_text)
      val taggedItem = tvText.tag as StanderModel
      if (taggedItem.isChecked) list.add(taggedItem)
    }
    vm.selectedCarColor = list
    vm.refreshCarColor()

  }

  private fun getViewForCarModelField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)

//        vm.selectedModelLocal?.let {
//            if (it.contains(item)) {
//                binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
//                binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
//            } else {
//                binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
//                binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))
//            }
//        }

    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateCarModelList()
      }
    }
    return view
  }

  private fun updateCarModelList() {
    val list: ArrayList<StanderModel> = ArrayList()
//        val childCount = binding.llModelData.childCount
//        for (i in 0 until childCount) {
//            val v = binding.llModelData.getChildAt(i)
//            val tvText = v.findViewById<TextView>(R.id.tv_text)
//            val taggedItem = tvText.tag as StanderModel
//            if (taggedItem.isChecked) list.add(taggedItem)
//        }
//        vm.selectedModelLocal = list
//        binding.llSubModel.isVisible = vm.selectedModelLocal != null

    vm.refreshModel()

  }

  private fun getViewForCarSubModelField(item: StanderModel): View {
    val vi = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ItemFilterBinding = ItemFilterBinding.inflate(vi)
    val view: View = binding.getRoot()
    binding.tvText.text = item.title
    binding.tvText.setTag(item)

//        vm.selectedSubModelLocal?.let {
//            if (it.contains(item)) {
//                binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
//                binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
//            } else {
//                binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
//                binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_black))
//            }
//        }

    binding.fBackground.setOnClickListener { v ->
      val taggedItem = binding.tvText.getTag() as StanderModel?
      taggedItem?.let {
        if (it.isChecked) {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_un_selected)
          binding.tvText.setTextColor(
            ContextCompat.getColor(
              requireContext(),
              R.color.text_color_black
            )
          )
          it.isChecked = false
        } else {
          binding.fBackground.setBackgroundResource(R.drawable.filter_item_selected)
          binding.tvText.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
          it.isChecked = true
        }
        binding.tvText.setTag(it)
        updateCarSubModelList()
      }
    }
    return view
  }

  private fun updateCarSubModelList() {
    val list: ArrayList<StanderModel> = ArrayList()
//        val childCount = binding.llCarCategoryData.childCount
//        for (i in 0 until childCount) {
//            val v = binding.llCarCategoryData.getChildAt(i)
//            val tvText = v.findViewById<TextView>(R.id.tv_text)
//            val taggedItem = tvText.tag as StanderModel
//            if (taggedItem.isChecked) list.add(taggedItem)
//        }
    vm.refreshSubModel()

  }

  //endregion draw views

}