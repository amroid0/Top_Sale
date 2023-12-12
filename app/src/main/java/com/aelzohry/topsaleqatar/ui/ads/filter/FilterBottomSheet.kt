package com.aelzohry.topsaleqatar.ui.ads.filter

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FilterBottomSheetBinding
import com.aelzohry.topsaleqatar.databinding.ItemLocationBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.ui.ads.AdsFragment
import com.aelzohry.topsaleqatar.ui.ads.ViewModelFactory
import com.aelzohry.topsaleqatar.ui.autoCompleteSearchLocation.view.SelectMultipleLocationFragment
import com.aelzohry.topsaleqatar.ui.mapAndSearchList.MapAndSearchListFragment
import com.aelzohry.topsaleqatar.ui.search.SearchFragment
import com.aelzohry.topsaleqatar.utils.base.BaseBottomSheet
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog1
import com.aelzohry.topsaleqatar.utils.extenions.setupListDialogs
import com.aelzohry.topsaleqatar.utils.extenions.setupListDialogs1
import com.google.android.gms.maps.model.LatLng
import org.apmem.tools.layouts.FlowLayout

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class FilterBottomSheet : BaseBottomSheet<FilterBottomSheetBinding, FilterViewModel>() {

    private var catDialog: Dialog? = null
    private var subCatDialog: Dialog? = null
    private var typeDialog: Dialog? = null
    private var makesDialog: Dialog? = null
    private var modelDialog: Dialog? = null
    private var subModelDialog: Dialog? = null
    private var citiesAdLocationDialog: Dialog? = null

    private var yearDialog: Dialog? = null
    private var gearboxDialog: Dialog? = null
    private var engineSizeDialog: Dialog? = null
    private var kmDialog: Dialog? = null

    private var fuelTypeDialog: Dialog? = null
    private var engineDriveSystemDialog: Dialog? = null
    private var carColorDialog: Dialog? = null

    private var regionDialog: Dialog? = null
    private lateinit var thumbView: View

    override fun getLayoutID(): Int = R.layout.filter_bottom_sheet

    override fun getViewModel(): FilterViewModel = ViewModelProvider(this, ViewModelFactory(arguments?.getParcelable(ARG_CATEGORY), loadCategory = arguments?.getBoolean(LOAD_CATEGORY, true) ?: true))[FilterViewModel::class.java]

    override fun setupUI() {

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
        vm.selectedCarMake = arguments?.getParcelable(ARG_SELECTED_CAR_MAKE)
        vm.selectedRegion = arguments?.getParcelable(ARG_SELECTED_REGION)
        vm.selectedCityAdLocation = arguments?.getParcelable(ARG_SELECTED_CITY)
        vm.selectedModelLocal = arguments?.getParcelable(ARG_SELECTED_MODEL)
        vm.selectedSubModelLocal = arguments?.getParcelable(ARG_SELECTED_SUB_MODEL)
        vm.selectedYear = arguments?.getParcelableArrayList(ARG_SELECTED_YEAR)

        vm.selectedGearbox = arguments?.getParcelableArrayList(ARG_SELECTED_GEARBOX)
        vm.selectedEngineSize = arguments?.getParcelableArrayList(ARG_SELECTED_ENGINE_SIZE)
        vm.selectedKm = arguments?.getParcelableArrayList(ARG_SELECTED_KM)

        vm.selectedFuelType = arguments?.getParcelableArrayList(ARG_SELECTED_FULE_TYPE)
        vm.selectedEngineDriveSystem = arguments?.getParcelableArrayList(ARG_SELECTED_ENGINE_DRIVE_SYSTEM)
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



        vm.refreshTitles()
        initSeekBar()

    }

    fun initSeekBar() {
        thumbView = LayoutInflater.from(context).inflate(R.layout.custom_thumb_seek_bar, null, false)

//        binding.seekBar.thumb = getThumb(0)
//        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                seekBar.thumb = getThumb(progress)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//
//            }
//        })

    }

    override fun onClickedListener() {

       // binding.llMainCat.setOnClickListener { catDialog?.show() }

        binding.llMake.setOnClickListener {
            makesDialog?.show()
        }

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
            if (requireActivity() is AdsFragment) (requireActivity() as AdsFragment).onFilterClickedApplyListener(vm.selectedCat, vm.selectedSubCat, vm.selectedType,
                vm.selectedCarMake, vm.selectedModelLocal, vm.selectedSubModelLocal,null,
                vm.selectedYear, vm.selectedGearbox, vm.selectedEngineSize,

                vm.selectedFuelType, vm.selectedEngineDriveSystem, vm.selectedCarColor,

                vm.selectedKm, vm.selectedRegion, vm.selectedCityAdLocation, vm.etFromPrice.get(), vm.etToPrice.get(), vm.etFromRooms.get(), vm.etToRooms.get(), vm.etFromBathRooms.get(), vm.etToBathRooms.get(), vm.selectedLatLng, vm.distance, getItems())

            if (requireActivity() is SearchFragment) (requireActivity() as SearchFragment).onFilterClickedApplyListener(vm.selectedCat, vm.selectedSubCat, vm.selectedType,
                vm.selectedCarMake, vm.selectedModelLocal, vm.selectedSubModelLocal, null,vm.selectedYear, vm.selectedGearbox, vm.selectedEngineSize,

                vm.selectedFuelType, vm.selectedEngineDriveSystem, vm.selectedCarColor,

                vm.selectedKm, vm.selectedRegion, vm.selectedCityAdLocation, vm.etFromPrice.get(), vm.etToPrice.get(), vm.etFromRooms.get(), vm.etToRooms.get(), vm.etFromBathRooms.get(), vm.etToBathRooms.get(), vm.selectedLatLng, vm.distance, getItems())
            dismiss()
        }

        binding.btnApply.setOnClickListener {
            if (requireActivity() is AdsFragment) (requireActivity() as AdsFragment).onFilterClickedApplyListener(vm.selectedCat, vm.selectedSubCat, vm.selectedType,
                vm.selectedCarMake, vm.selectedModelLocal, vm.selectedSubModelLocal,null, vm.selectedYear, vm.selectedGearbox, vm.selectedEngineSize,

                vm.selectedFuelType, vm.selectedEngineDriveSystem, vm.selectedCarColor,

                vm.selectedKm, vm.selectedRegion, vm.selectedCityAdLocation, vm.etFromPrice.get(), vm.etToPrice.get(), vm.etFromRooms.get(), vm.etToRooms.get(), vm.etFromBathRooms.get(), vm.etToBathRooms.get(), vm.selectedLatLng, vm.distance, getItems())

            if (requireActivity() is SearchFragment) (requireActivity() as SearchFragment).onFilterClickedApplyListener(vm.selectedCat, vm.selectedSubCat, vm.selectedType, vm.selectedCarMake, vm.selectedModelLocal, vm.selectedSubModelLocal,null, vm.selectedYear, vm.selectedGearbox, vm.selectedEngineSize,

                vm.selectedFuelType, vm.selectedEngineDriveSystem, vm.selectedCarColor,

                vm.selectedKm, vm.selectedRegion, vm.selectedCityAdLocation, vm.etFromPrice.get(), vm.etToPrice.get(), vm.etFromRooms.get(), vm.etToRooms.get(), vm.etFromBathRooms.get(), vm.etToBathRooms.get(), vm.selectedLatLng, vm.distance, getItems())
            dismiss()
        }

        binding.backBtn.setOnClickListener {
            dismiss()
        }

    }

    fun setTextLocation(location: LatLng?, distance: Int) {
        if (location != null) {
            vm.selectedLatLng = location
            vm.distance = distance
            if (context != null) {
                vm.selectLocationText.set(String.format(getString(R.string.distance_f), distance.toString(), Helper.getAddress(location, requireContext())))
            } else {
                vm.selectLocationText.set(String.format(getString(R.string.distance_f), distance.toString(), getString(R.string.unknown_address)))
            }

        }
    }

    override fun observerLiveData() {

        vm.catsRes.observe(this) {

        }

        vm.subCatsRes.observe(this) {
            subCatDialog = Dialog(requireContext())
            setupDialog(subCatDialog, it) { modelLocal: LocalStanderModel ->
                getViewModel().selectedSubCat = modelLocal
                getViewModel().selectSubCatText.set(modelLocal.title.localized)
            }
        }

        vm.typeRes.observe(this) {
            typeDialog = Dialog(requireContext())
            setupDialog(typeDialog, it) { modelLocal: LocalStanderModel ->
                getViewModel().selectedType = modelLocal
                getViewModel().selectedTypeText.set(modelLocal.title.localized)
//                5f70d8a6ddd69f66a0387c69 // for rent
            }
        }

        vm.regionRes.observe(this) {
            regionDialog = Dialog(requireContext())
            setupDialog(regionDialog, it) { modelLocal: LocalStanderModel ->
                getViewModel().selectedRegion = modelLocal
                getViewModel().selectedRegionText.set(modelLocal.title.localized)
            }
        }

        vm.carMakesRes.observe(this) {
            makesDialog = Dialog(requireContext())
            setupDialog1(makesDialog, it) { modelLocal: StanderModel ->
                getViewModel().selectedCarMake = modelLocal
                getViewModel().selectedCarMakeText.set(modelLocal.title)
                getViewModel().loadCarModel(modelLocal._id)
                getViewModel().selectedModelLocal = null
                getViewModel().selectedSubModelLocal = null
                getViewModel().selectedModelText.set("")
                getViewModel().selectedSubModelText.set("")
                vm.carCatMakeState.set(true)
            }
        }

        /* multi*/
        vm.yearRes.observe(this) {
            yearDialog = Dialog(requireContext())
            setupListDialogs(yearDialog, it) { model ->
                getViewModel().selectedYear = model
                vm.refreshYears()
            }
        }

        vm.gearboxRes.observe(this, Observer {
            gearboxDialog = Dialog(requireContext())
            setupListDialogs(gearboxDialog, it) { model ->
                getViewModel().selectedGearbox = model
                vm.refreshGearbox()

            }
        })

        vm.engineSizeRes.observe(this, Observer {
            engineSizeDialog = Dialog(requireContext())
            setupListDialogs(engineSizeDialog, it) { model ->
                getViewModel().selectedEngineSize = model
                vm.refreshEngineSize()

            }
        })

        vm.kmRes.observe(this) {
            kmDialog = Dialog(requireContext())
            setupListDialogs(kmDialog, it) { model ->
                getViewModel().selectedKm = model
                vm.refreshKm()

            }
        }


        vm.fuelTypeRes.observe(this) {
            fuelTypeDialog = Dialog(requireContext())
            setupListDialogs(fuelTypeDialog, it) { model ->
                getViewModel().selectedFuelType = model
                vm.refreshFuelType()
            }
        }


        vm.carEngineDriveSystemRes.observe(this) {
            engineDriveSystemDialog = Dialog(requireContext())
            setupListDialogs(engineDriveSystemDialog, it) { model ->
                getViewModel().selectedEngineDriveSystem = model
                vm.refreshEngineDriveSystem()
            }
        }

        vm.carColorRes.observe(this) {
            carColorDialog = Dialog(requireContext())
            setupListDialogs(carColorDialog, it) { model ->
                getViewModel().selectedCarColor = model
                vm.refreshCarColor()
            }
        }

        /* multi*/
        vm.modelRes.observe(this, Observer {
            modelDialog = Dialog(requireContext())
            setupListDialogs1(modelDialog, it) { model ->
//                getViewModel().selectedModelLocal = model
//                getViewModel().selectedSubModelLocal = null
//                vm.refreshModel()

            }
        })


        vm.carSubModelRes.observe(this, Observer {
            subModelDialog = Dialog(requireContext())
            setupListDialogs1(subModelDialog, it) { model ->
//                getViewModel().selectedSubModelLocal = model
//                vm.refreshSubModel()
            }
        })


        vm.citiesRes.observe(this) {
            val list = ArrayList<LocalStanderModel>()
            it.forEach {
                list.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)))
            }
            citiesAdLocationDialog = Dialog(requireContext())
            setupDialog(citiesAdLocationDialog, list, { modelLocal: LocalStanderModel, i ->
                vm.onCityAdLocationSelectedListener(modelLocal)
            })
        }

    }

    override fun isFullHeight(): Boolean = false

    companion object {

        private const val ARG_LAT_LNG = "SELECTED_LAT_LNG"
        private const val ARG_DISTANCE = "DISTANCE"
        private const val ARG_CATEGORY = "ARG_CATEGORY"
        private const val ARG_SELECTED_SUB_CAT = "ARG_SELECTED_SUB_CAT"
        private const val ARG_SELECTED_TYPE = "ARG_SELECTED_TYPE"
        private const val ARG_SELECTED_CAR_MAKE = "ARG_SELECTED_CAR_MAKE"
        private const val ARG_SELECTED_MODEL = "ARG_SELECTED_MODEL"
        private const val ARG_SELECTED_SUB_MODEL = "ARG_SELECTED_SUB_MODEL"
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

        @JvmStatic
        fun newInstance(category: Category?, selectedSubCat: LocalStanderModel?, selectedType: LocalStanderModel?,
            selectedCarMake: StanderModel?, selectedModelLocal: ArrayList<StanderModel>?, selectedSubModelLocal: ArrayList<StanderModel>?,
            selectedYear: ArrayList<StanderModel>?, selectedGearbox: ArrayList<StanderModel>?, selectedEngineSize: ArrayList<StanderModel>?,

            selectedFuelType: ArrayList<StanderModel>?, selectedEngineDriveSystem: ArrayList<StanderModel>?, selectedCarColor: ArrayList<StanderModel>?,

            selectedKm: ArrayList<StanderModel>?, selectedRegion: LocalStanderModel?, selectedCity: LocalStanderModel?, loadCategory: Boolean = true, fromPrice: String?, toPrice: String?, fromRooms: String?, toRooms: String?, fromBathRooms: String?, toBathRooms: String?, selectedLatLng: LatLng?, distance: Int, locationList: ArrayList<PlaceAutocomplete>?

        ) = FilterBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_LAT_LNG, selectedLatLng)
                putInt(ARG_DISTANCE, distance)
                putParcelable(ARG_CATEGORY, category)
                putParcelable(ARG_SELECTED_SUB_CAT, selectedSubCat)
                putParcelable(ARG_SELECTED_TYPE, selectedType)
                putParcelable(ARG_SELECTED_CAR_MAKE, selectedCarMake)
                putParcelableArrayList(ARG_SELECTED_MODEL, selectedModelLocal)
                putParcelableArrayList(ARG_SELECTED_SUB_MODEL, selectedSubModelLocal)
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
        dialogFragment.setListener(object :MapAndSearchListFragment.SearchResult{
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

            if (TextUtils.isEmpty(itemToCheck.placeId)){
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

}