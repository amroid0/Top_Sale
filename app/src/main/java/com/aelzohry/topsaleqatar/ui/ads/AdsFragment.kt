package com.aelzohry.topsaleqatar.ui.ads

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentAdsBinding
import com.aelzohry.topsaleqatar.helper.Constants.FROM_OTHERS
import com.aelzohry.topsaleqatar.model.AdBanner
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.CategoryClass
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.repository.remote.AdsDataSource
import com.aelzohry.topsaleqatar.ui.adapters.AdsAdapter
import com.aelzohry.topsaleqatar.ui.ads.filter.FilterBottomSheet
import com.aelzohry.topsaleqatar.ui.ads.filter.FilterListener
import com.aelzohry.topsaleqatar.ui.ads.filter.NewFilterBottomSheet
import com.aelzohry.topsaleqatar.ui.categories.CategoriesActivity
import com.aelzohry.topsaleqatar.ui.new_ad.categorisDialog.view.CategoriesDialogFragment
import com.aelzohry.topsaleqatar.ui.selectLocationDialog.view.SelectLocationDialogFragment
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.WrapContentLinearLayoutManager
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog1
import com.aelzohry.topsaleqatar.utils.extenions.setupListDialogs1
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt

class AdsFragment : BaseActivity<FragmentAdsBinding, AdsViewModel>(), FilterListener {

    private var catDialog: Dialog? = null
    private var subCatDialog: Dialog? = null
    private var carStepDialog: Dialog? = null

    private var makesDialog: Dialog? = null
    private var modelDialog: Dialog? = null
    private var subModelDialog: Dialog? = null

    companion object {
        private const val ARG_CATEGORY = "CATEGORY"
        private const val ARG_SUB_CATEGORY = "SUB_CATEGORY"
        private const val ARG_FROM_WHERE = "FROM_WHERE"

        @JvmStatic
        fun newInstance(context: Context, fromWhere: Int, category: Category, subCategory: LocalStanderModel? = null) {

            val i = Intent(context, AdsFragment::class.java).putExtra(ARG_CATEGORY, category)
            if (subCategory != null) i.putExtra(ARG_SUB_CATEGORY, subCategory)
            i.putExtra(ARG_FROM_WHERE, fromWhere)
            context.startActivity(i)
        }
    }

    private lateinit var adsAdapter: AdsAdapter
    private lateinit var filterBottomSheet: FilterBottomSheet
    override fun getLayoutID(): Int = R.layout.fragment_ads

    override fun getViewModel(): AdsViewModel = ViewModelProvider(this, ViewModelFactory(intent?.getParcelableExtra<Category>(ARG_CATEGORY), subCategory = intent?.getParcelableExtra(ARG_SUB_CATEGORY)))[AdsViewModel::class.java]

    override fun setupUI() {

        var itemWidth = 0
        screenSizeInDp.apply {
            // screen width in dp
            val itemWidthInDp = (x - 12) / 2
            itemWidth = Utils.pxFromDp(applicationContext, itemWidthInDp.toFloat()).toInt()
        }

        val fromWhere = intent?.getIntExtra(ARG_FROM_WHERE, FROM_OTHERS)


        adsAdapter = AdsAdapter(itemWidth, vm)
        binding.recyclerView.adapter = adsAdapter
        val mainCategory = intent?.getParcelableExtra<Category>(ARG_CATEGORY)
        adsAdapter.setCarCategory(mainCategory?.categoryClass == CategoryClass.CARS)
        val itemAnimator = binding.recyclerView.getItemAnimator() as SimpleItemAnimator
        itemAnimator.supportsChangeAnimations = false

        (binding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adsAdapter.getItemViewType(position) == AdsAdapter.AD_TYPE) 1 else 2
//                    return adsAdapter.getItemViewType(position)
            }
        }


        binding.recyclerView.layoutManager = WrapContentLinearLayoutManager(this, 2)


        filterBottomSheet = FilterBottomSheet.newInstance(vm.category, vm.selectedSubCat, vm.selectedType, vm.selectedCarMakeItem, vm.selectedModelItem, vm.selectedSubModelItem, vm.selectedYear, vm.selectedGearbox, vm.selectedEngineSize, vm.selectedFuelType, vm.selectedEngineDriveSystem, vm.selectedCarColor, vm.selectedKm, vm.selectedRegion, vm.selectedCity, true, vm.fromPrice, vm.toPrice, vm.fromRooms, vm.toRooms, vm.fromBathRooms, vm.toBathRooms, vm.selectedLatLng, vm.distance, vm.listLocation

        )

        setSearchHint()
        if (vm.category?.categoryClass == CategoryClass.CARS) {
            vm.carCategoryState.set(true)
            vm.aqarCategoryState.set(false)
//            binding.llFastFilters.isVisible = true
//            binding.llCarMake.isVisible = true
//            binding.llAqarType.isVisible = false

        }

        Log.e("test_type", vm.category?.categoryClass.toString())

        if (vm.category?.categoryClass == CategoryClass.LANDS || vm.category?.categoryClass == CategoryClass.PROPERTIES) {
            vm.carCategoryState.set(false)
            vm.aqarCategoryState.set(true)

//            binding.llFastFilters.isVisible = true
//            binding.llCarMake.isVisible = false
//            binding.llAqarType.isVisible = true
        }
    }

    private fun setSearchHint() {
        if (vm.category != null) {
            binding.etSearch.hint = String.format(getString(R.string.search_f), vm.category!!.title.localized)
        }
    }

    private fun setupGridView() {

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adsAdapter.getItemViewType(position) == AdsAdapter.AD_TYPE) 1 else 2
//                    return adsAdapter.getItemViewType(position)
            }
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adsAdapter

    }

    private fun setupListView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adsAdapter
    }

    override fun onClickedListener() {

        binding.imgCat.setOnClickListener {
//            catDialog?.show()
            startActivity(Intent(this, CategoriesActivity::class.java))
        }


        binding.btnToggleView.setOnClickListener {
            val oldValue = vm.isAdViewGrid.get() ?: false

            vm.isAdViewGrid.set(!oldValue)

            if (oldValue) setupListView()
            else setupGridView()

        }
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.onSearchClickedListener(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.btnBack.setOnClickListener {
            if (filterBottomSheet.isVisible) filterBottomSheet.dismiss()
            else onBackPressed()
        }

        binding.btnFilter.setOnClickListener {
            if (!filterBottomSheet.isVisible) {
                val filterBottomSheet = NewFilterBottomSheet.newInstance(vm.category, vm.selectedSubCat, vm.selectedType, vm.selectedCarMakeItem, vm.selectedModelItem, vm.selectedSubModelItem, vm.selectedCarShow, vm.selectedYear, vm.selectedGearbox, vm.selectedEngineSize, vm.selectedFuelType, vm.selectedEngineDriveSystem, vm.selectedCarColor, vm.selectedKm, vm.selectedRegion, vm.selectedCity, true, vm.fromPrice, vm.toPrice, vm.fromRooms, vm.toRooms, vm.fromBathRooms, vm.toBathRooms, vm.selectedLatLng, vm.distance, vm.listLocation,vm.regoinList)
                filterBottomSheet.show(supportFragmentManager, "")
            }
        }

        binding.btnSort.setOnClickListener {
//            setupSortList(it)
            openSortDialog()
        }

        binding.btnLocation.setOnClickListener {
            val dialog: SelectLocationDialogFragment = SelectLocationDialogFragment.newInstance()
            dialog.setListener(object : SelectLocationDialogFragment.LocationListener {
                override fun onLocationSelected(location: LatLng?, distance: Int) {
                    setTextLocation(location, distance)
                }

                override fun onBackClick() {

                }

            })
            supportFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
        }

        binding.tvForSale.setOnClickListener { toggleForSaleUi() }
        binding.tvForRent.setOnClickListener { toggleForRentUi() }

        binding.tvMake.setOnClickListener {
            makesDialog?.show()

//            vm.carMakesRes.value?.let {
//                val list = ArrayList<StanderModel>()
//                it.forEach {
//                    list.add(StanderModel(it._id, it.localized))
//                }
//                setupCarStepDialog(list)
//                carStepDialog?.show()
//
//            }
        }

        binding.tvModel.setOnClickListener {
            modelDialog?.show()
        }

        binding.tvCarSubModel.setOnClickListener {
            subModelDialog?.show()
        }

        binding.llAqarType.setOnClickListener {
            subCatDialog?.show()
        }
    }

    fun toggleForSaleUi() {
        if (vm.selectedType == null) {
            vm.selectedType = vm.forSaleItem
            binding.tvForSale.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.tvForRent.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {

            if (vm.selectedType == vm.forSaleItem) {
                vm.selectedType = null
                binding.tvForSale.setBackgroundResource(R.drawable.custom_border_btn)
                binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.text_color))

                binding.tvForRent.setBackgroundResource(R.drawable.custom_border_btn)
                binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            } else {
                vm.selectedType = vm.forSaleItem
                binding.tvForSale.setBackgroundResource(R.drawable.custom_solid_btn)
                binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.white))

                binding.tvForRent.setBackgroundResource(R.drawable.custom_border_btn)
                binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
        }
        vm.onFilterCallBack()
    }

    fun toggleForRentUi() {

        if (vm.selectedType == null) {
            vm.selectedType = vm.forRentItem
            binding.tvForRent.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.tvForSale.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {

            if (vm.selectedType == vm.forRentItem) {
                vm.selectedType = null
                binding.tvForRent.setBackgroundResource(R.drawable.custom_border_btn)
                binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.text_color))

                binding.tvForSale.setBackgroundResource(R.drawable.custom_border_btn)
                binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            } else {
                vm.selectedType = vm.forRentItem
                binding.tvForRent.setBackgroundResource(R.drawable.custom_solid_btn)
                binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.white))

                binding.tvForSale.setBackgroundResource(R.drawable.custom_border_btn)
                binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
        }
        vm.onFilterCallBack()
    }

    fun toggleForCarMakeUi() {
        if (vm.selectedCarMakeItem == null) {
            binding.tvMake.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvMake.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {

            binding.tvMake.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvMake.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        binding.tvModel.isVisible = vm.selectedCarMakeItem!= null
        binding.tvCarSubModel.isVisible = vm.selectedModelItem != null

    }

    fun toggleForCarModelUi() {
        if (vm.selectedModelItem == null) {
            binding.tvModel.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvModel.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {
            binding.tvModel.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvModel.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.tvCarSubModel.isVisible = vm.selectedModelItem != null

    }

    fun toggleForCarSubModelUi() {
        if (vm.selectedSubModelItem == null) {
            binding.tvCarSubModel.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvCarSubModel.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {
            binding.tvCarSubModel.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvCarSubModel.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

    }

//    private fun setupCarStepDialog(carMakesList:ArrayList<StanderModel>) {
//
//        carStepDialog = Dialog(this)
//        val b = DataBindingUtil.inflate<AdsCatCarAttrBinding>(layoutInflater, R.layout.ads_cat_car_attr, null, false)
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
//
//    private fun setupMakeStep(bind: AdsCatCarAttrBinding,carMakesList:ArrayList<StanderModel>) {
//        vm.tvTitle.set(getString(R.string.car_make))
//        vm.currentStep.set(0)
//        bind.btnBack.setVisible(false)
//        vm.isLoading.postValue(true)
//
//        vm.isLoading.postValue(false)
//        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm,carMakesList) { b, m, i, a ->
//            b.root.setOnClickListener {
//                vm.isLoading.postValue(true)
//
//                vm.selectedCarMakeItem = m
//                vm.onMakeSelectedListener(m) { data ->
//                    vm.isLoading.postValue(false)
//                    setupModelStep(bind, data)
//                }
//            }
//        }
//
//
//    }
//
//    private fun setupModelStep(bind: AdsCatCarAttrBinding, data: List<StanderModel>?) {
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
//    private fun setupSubModelStep(bind: AdsCatCarAttrBinding, data: List<StanderModel>) {
//        vm.tvTitle.set(getString(R.string.car_sub_model))
//        vm.currentStep.set(2)
//        bind.btnBack.setVisible(true)
//        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, data) { b, m, i, a ->
//            b.root.setOnClickListener {
//
//                vm.onSubModelSelectedListener(m)
//                toggleForMakeUi()
//                vm.onFilterCallBack()
//
////                vm.selectedMakeModelCategoryText.set( vm.selectedCarMakeItem?.title + "  ,  " + vm.selectedModelItem?.title + "  ,  " + vm.selectedSubModelItem?.title)
//                carStepDialog?.dismiss()
//
//            }
//        }
//    }

    fun setTextLocation(location: LatLng?, distance: Int) {
        if (location != null) {
            vm.selectedLatLng = location
            vm.distance = distance
        }


        vm.onFilterCallBack()

    }

    private fun openSortDialog() {
        val dialogFragment: SortDialogFragment = SortDialogFragment.newInstance(vm.selectedSort)

        dialogFragment.setListener(object : SortDialogFragment.Listener {
            override fun onItemSelected(itemSelected: Int) {
                when (itemSelected) {
                    R.id.rb_latest -> vm.onSortClickedListener(AdsDataSource.SORT_LATEST, applicationContext, null)
                    R.id.rb_closest -> {
                        requestCurrentLocation()
                    }

                    R.id.rb_lowest_price -> vm.onSortClickedListener(AdsDataSource.SORT_LOWEST_PRICE, applicationContext, null)
                    R.id.rb_higher_price -> vm.onSortClickedListener(AdsDataSource.SORT_HIGHER_PRICE, applicationContext, null)
                }

                vm.selectedSort = itemSelected
                dialogFragment.dismiss()
            }
        })


        dialogFragment.show(supportFragmentManager, CategoriesDialogFragment::class.java.getSimpleName())
    }

    private fun setupSortList(it: View) {
        val popupMenu = PopupMenu(this, it)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.sort_ads_menu, menu)
        popupMenu.menu.findItem(R.id.model_newest).isVisible = vm.category?.categoryClass == CategoryClass.CARS
        popupMenu.menu.findItem(R.id.minimum_mileage).isVisible = vm.category?.categoryClass == CategoryClass.CARS
        popupMenu.show()
        popupMenu.menu?.getItem(vm.selectedSort)?.isChecked = true

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
//                R.id.latest -> vm.onSortClickedListener(AdsDataSource.SORT_LATEST, this) //الاحدث
                R.id.closest -> {
                    requestCurrentLocation()
                }

//                R.id.lowest_price -> vm.onSortClickedListener(AdsDataSource.SORT_LOWEST_PRICE, this)
//                R.id.higher_price -> vm.onSortClickedListener(AdsDataSource.SORT_HIGHER_PRICE, this)
//                R.id.model_newest -> vm.onSortClickedListener(AdsDataSource.SORT_MODEL, this)
//                R.id.minimum_mileage -> vm.onSortClickedListener(
//                    AdsDataSource.SORT_MINIMUM_MILEAGE,
//                    this
//                )
            }
            it.isChecked = true
            vm.selectedSort = it.order
            return@setOnMenuItemClickListener true
        }
    }

    private lateinit var currentLocation: GPSTracker
    private fun requestCurrentLocation() {
        currentLocation = GPSTracker(this) { location, err ->
            if (err != null) {
                Toast.makeText(this, this.getString(R.string.location_err), Toast.LENGTH_LONG).show()
            } else {
                lifecycle.removeObserver(currentLocation)
                vm.onSortClickedListener(AdsDataSource.SORT_CLOSEST, this, location) // الاقرب
            }

        }
        lifecycle.addObserver(currentLocation)
    }

    private fun picLocation() {
        requestCurrentLocation()
    }

    override fun observerLiveData() {

        vm.bannersRes.observe(this) {

        }

        vm.getLayoutState().observe(this) {
            vm.frameState.set(it)
            vm.swipeRefresh.set(it == CustomFrame.FrameState.PROGRESS)
        }

        vm.getFooterState().observe(this) {
            adsAdapter.setState(it)
        }

        vm.adsRes.observe(this) {
            adsAdapter.submitList(it as PagedList<AdBanner>)
        }


        vm.catsRes.observe(this) {
            val list = ArrayList<LocalStanderModel>()
            it.forEach {
                list.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)))
            }
            catDialog = Dialog(this)
            setupDialog(catDialog, list, { modelLocal: LocalStanderModel, i ->
                vm.category = it[i]
                vm.onFilterCallBack()
                setSearchHint()

            })
        }


        vm.carMakesRes.observe(this) {
            makesDialog = Dialog(this)
            setupDialog1(makesDialog, it) { modelLocal: StanderModel ->
                vm.selectedCarMakeItem = modelLocal
                binding.tvModel.isVisible = vm.selectedCarMakeItem != null




                vm.selectedCarMakeText.set(modelLocal.title)
                vm.loadCarModel(modelLocal._id)
                vm.selectedModelItem = null
                vm.selectedSubModelItem = null
                vm.selectedModelText.set("")
                vm.selectedSubModelText.set("")
                vm.carCatMakeState.set(true)
                vm.onFilterCallBack()
                toggleForCarMakeUi()
            }
        }

        vm.modelRes.observe(this) {

            modelDialog = Dialog(this)
            setupListDialogs1(modelDialog, it) { model ->
                vm.selectedModelItem = model
                binding.tvCarSubModel.isVisible = vm.selectedModelItem != null
                vm.selectedSubModelItem = null
                vm.onFilterCallBack()
                toggleForCarModelUi()
                vm.refreshModel()

            }
        }

        vm.carSubModelRes.observe(this) {

            subModelDialog = Dialog(this)
            setupListDialogs1(subModelDialog, it) { model ->
                getViewModel().selectedSubModelItem = model
                vm.onFilterCallBack()
                toggleForCarSubModelUi()
                vm.refreshSubModel()
            }
        }


        vm.subCatsRes.observe(this) {
            subCatDialog = Dialog(this)
            setupDialog(subCatDialog, it) { modelLocal: LocalStanderModel ->
                getViewModel().selectedSubCat = modelLocal
                vm.onFilterCallBack()
//                getViewModel().selectSubCatText.set(modelLocal.title.localized)
            }
        }
    }

    override fun onFilterClickedApplyListener(category: Category?, selectedSubCat: LocalStanderModel?, selectedType: LocalStanderModel?, selectedCarMake: StanderModel?, selectedModelLocal: ArrayList<StanderModel>?, selectedSubModelLocal: ArrayList<StanderModel>?, selectedCarShow: User?, selectedYear: ArrayList<StanderModel>?, selectedGearbox: ArrayList<StanderModel>?, selectedEngineSize: ArrayList<StanderModel>?,

        selectedFuelType: ArrayList<StanderModel>?, selectedEngineDriveSystem: ArrayList<StanderModel>?, selectedCarColor: ArrayList<StanderModel>?,

        selectedKm: ArrayList<StanderModel>?, selectedRegion: LocalStanderModel?, selectedCity: LocalStanderModel?, fromPrice: String?, toPrice: String?, fromRooms: String?, toRooms: String?, fromBathRooms: String?, toBathRooms: String?, selectedLatLng: LatLng?, distance: Int, locationList: ArrayList<PlaceAutocomplete>) {

        vm.category = category
        vm.selectedSubCat = selectedSubCat
        vm.selectedType = selectedType
        vm.selectedCarMakeItem = selectedCarMake
        vm.selectedModelItem = selectedModelLocal
        vm.selectedCarShow = selectedCarShow

        vm.selectedSubModelItem = selectedSubModelLocal
        vm.selectedYear = selectedYear
        vm.selectedGearbox = selectedGearbox
        vm.selectedEngineSize = selectedEngineSize

        vm.selectedFuelType = selectedFuelType
        vm.selectedEngineDriveSystem = selectedEngineDriveSystem
        vm.selectedCarColor = selectedCarColor

        vm.selectedKm = selectedKm
        vm.selectedRegion = selectedRegion
        vm.selectedCity = selectedCity
        vm.fromPrice = fromPrice
        vm.toPrice = toPrice
        vm.fromRooms = fromRooms
        vm.toRooms = toRooms
        vm.fromBathRooms = fromBathRooms
        vm.toBathRooms = toBathRooms
        vm.selectedLatLng = selectedLatLng
        vm.distance = distance
        vm.listLocation = locationList


        setSearchHint()
        vm.onFilterCallBack()

        if (selectedType?._id.equals(vm.forRentItem?._id)) {
            binding.tvForSale.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            binding.tvForRent.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.white))


        } else if (selectedType?._id.equals(vm.forSaleItem?._id)) {
            binding.tvForSale.setBackgroundResource(R.drawable.custom_solid_btn)
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.tvForRent.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {
            binding.tvForSale.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvForSale.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            binding.tvForRent.setBackgroundResource(R.drawable.custom_border_btn)
            binding.tvForRent.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        }

        toggleForCarMakeUi()
        toggleForCarModelUi()
        toggleForCarSubModelUi()

    }

    val Activity.displayMetrics: DisplayMetrics
        get() {
            // display metrics is a structure describing general information
            // about a display, such as its size, density, and font scaling
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= 30) {
                display?.apply {
                    getRealMetrics(displayMetrics)
                }
            } else {
                // getMetrics() method was deprecated in api level 30
                windowManager.defaultDisplay.getMetrics(displayMetrics)
            }

            return displayMetrics
        }

    val Activity.screenSizeInDp: Point
        get() {
            val point = Point()
            displayMetrics.apply {
                // screen width in dp
                point.x = (widthPixels / density).roundToInt()

                // screen height in dp
                point.y = (heightPixels / density).roundToInt()
            }

            return point
        }
}
