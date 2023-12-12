package com.aelzohry.topsaleqatar.ui.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentSearchBinding
import com.aelzohry.topsaleqatar.model.*
import com.aelzohry.topsaleqatar.repository.googleApi.model.PlaceAutocomplete
import com.aelzohry.topsaleqatar.repository.remote.AdsDataSource
import com.aelzohry.topsaleqatar.ui.adapters.AdsAdapter
import com.aelzohry.topsaleqatar.ui.ads.SortDialogFragment
import com.aelzohry.topsaleqatar.ui.ads.filter.FilterBottomSheet
import com.aelzohry.topsaleqatar.ui.ads.filter.FilterListener
import com.aelzohry.topsaleqatar.ui.new_ad.categorisDialog.view.CategoriesDialogFragment
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt

class SearchFragment : BaseActivity<FragmentSearchBinding, SearchViewModel>(), FilterListener {

    private lateinit var adapter: AdsAdapter
    private lateinit var filterBottomSheet: FilterBottomSheet
    override fun getLayoutID(): Int = R.layout.fragment_search

    override fun getViewModel(): SearchViewModel = ViewModelProvider(
        this,
        ViewModelFactory(intent?.getStringExtra(TXT_ARG))
    )[SearchViewModel::class.java]


    override fun setupUI() {

//        var itemWidth = 0
//        screenSizeInDp.apply {
//            // screen width in dp
//            val scale = resources.displayMetrics.density
//            val dpAsPixels = (12.0f * scale + 0.5f).toInt()
//            itemWidth = (x - dpAsPixels) / 2
//        }

        var itemWidth = 0
        screenSizeInDp.apply {
            // screen width in dp
            val itemWidthInDp = (x - 12) / 2
            itemWidth = Utils.pxFromDp(applicationContext, itemWidthInDp.toFloat()).toInt()
        }

        adapter = AdsAdapter(itemWidth,vm)
        binding.rv.adapter = adapter
        (binding.rv.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == AdsAdapter.AD_TYPE) 1 else 2
                }
            }

        filterBottomSheet = FilterBottomSheet.newInstance(
            vm.selectedCat,
            vm.selectedSubCat,
            vm.selectedType,
            vm.selectedCarMake,
            vm.selectedModelLocal,
            vm.selectedSubModelLocal,
            vm.selectedYear,
            vm.selectedGearbox,
            vm.selectedEngineSize,
            vm.selectedFuelType,
            vm.selectedEngineDriveSystem,
            vm.selectedCarColor,
            vm.selectedKm,
            vm.selectedRegion,
            vm.selectedCity,
            true,
            vm.fromPrice,
            vm.toPrice,
            vm.fromRooms,
            vm.toRooms,
            vm.fromBathRooms,
            vm.toBathRooms,
            vm.selectedLatLng,
            vm.distance,
            vm.listLocation
        )
    }

    private fun setupGridView() {

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == AdsAdapter.AD_TYPE) 1 else 2
                }
            }

        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = adapter

    }

    private fun setupListView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = adapter
    }

    override fun onClickedListener() {


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
            if (filterBottomSheet.isVisible)
                filterBottomSheet.dismiss()
            else
                onBackPressed()
        }

        binding.btnFilter.setOnClickListener {
            if (!filterBottomSheet.isVisible) {
                filterBottomSheet = FilterBottomSheet.newInstance(
                    vm.selectedCat,
                    vm.selectedSubCat,
                    vm.selectedType,
                    vm.selectedCarMake,
                    vm.selectedModelLocal,
                    vm.selectedSubModelLocal,
                    vm.selectedYear,
                    vm.selectedGearbox,
                    vm.selectedEngineSize,
                    vm.selectedFuelType,
                    vm.selectedEngineDriveSystem,
                    vm.selectedCarColor,
                    vm.selectedKm,
                    vm.selectedRegion,
                    vm.selectedCity,
                    loadCategory = true,
                    vm.fromPrice,
                    vm.toPrice,
                    vm.fromRooms,
                    vm.toRooms,
                    vm.fromBathRooms,
                    vm.toBathRooms,
                    vm.selectedLatLng,
                    vm.distance,
                    vm.listLocation
                )
                filterBottomSheet.show(supportFragmentManager, "")
            }
        }

        binding.btnSort.setOnClickListener {
//            setupSortList(it)
            openSortDialog()
        }
    }


    private fun openSortDialog() {
        val dialogFragment: SortDialogFragment = SortDialogFragment.newInstance(vm.selectedSort)

        dialogFragment.setListener(object : SortDialogFragment.Listener {
            override fun onItemSelected(itemSelected: Int) {

                when (itemSelected) {
                    R.id.rb_latest -> vm.onSortClickedListener(
                        AdsDataSource.SORT_LATEST,
                        null
                    )
                    R.id.rb_closest -> {
                        requestCurrentLocation()
                    }
                    R.id.rb_lowest_price -> vm.onSortClickedListener(
                        AdsDataSource.SORT_LOWEST_PRICE,
                        null
                    )
                    R.id.rb_higher_price -> vm.onSortClickedListener(
                        AdsDataSource.SORT_HIGHER_PRICE,
                        null
                    )
                }

                vm.selectedSort = itemSelected
                dialogFragment.dismiss()
            }
        })


        dialogFragment.show(
            supportFragmentManager,
            CategoriesDialogFragment::class.java.getSimpleName()
        )
    }


    private fun setupSortList(it: View) {
        val popupMenu = PopupMenu(this, it)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.sort_ads_menu, menu)
        popupMenu.menu.findItem(R.id.model_newest).isVisible =
            vm.selectedCat?.categoryClass == CategoryClass.CARS
        popupMenu.menu.findItem(R.id.minimum_mileage).isVisible =
            vm.selectedCat?.categoryClass == CategoryClass.CARS
        popupMenu.show()
        popupMenu.menu?.getItem(vm.selectedSort)?.isChecked = true

//        popupMenu.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.latest -> vm.onSortClickedListener(AdsDataSource.SORT_LATEST) //الاحدث
//                R.id.closest -> vm.onSortClickedListener(AdsDataSource.SORT_CLOSEST) // الاقرب
//                R.id.lowest_price -> vm.onSortClickedListener(AdsDataSource.SORT_LOWEST_PRICE)
//                R.id.higher_price -> vm.onSortClickedListener(AdsDataSource.SORT_HIGHER_PRICE)
//                R.id.model_newest -> vm.onSortClickedListener(AdsDataSource.SORT_MODEL)
//                R.id.minimum_mileage -> vm.onSortClickedListener(AdsDataSource.SORT_MINIMUM_MILEAGE)
//            }
//            it.isChecked = true
//            vm.selectedSort = it.order
//            return@setOnMenuItemClickListener true
//        }
    }

    private lateinit var currentLocation: GPSTracker
    private fun requestCurrentLocation() {
        currentLocation = GPSTracker(this) { location, err ->
            if (err != null) {
                Toast.makeText(this, this.getString(R.string.location_err), Toast.LENGTH_LONG)
                    .show()
            } else {
                lifecycle.removeObserver(currentLocation)
                vm.onSortClickedListener(AdsDataSource.SORT_CLOSEST, location)
            }

        }
        lifecycle.addObserver(currentLocation)
    }

    override fun observerLiveData() {

        vm.getLayoutState().observe(this, Observer {
            vm.frameState.set(it)
        })

        vm.getFooterState().observe(this, Observer {
            adapter.setState(it)
        })

        vm.adsRes.observe(this, Observer {
            adapter.submitList(it as PagedList<AdBanner>)
        })

    }

    companion object {
        private const val TXT_ARG = "TXT_ARG"

        @JvmStatic
        fun newInstance(context: Context, txt: String) {
            context.startActivity(
                Intent(context, SearchFragment::class.java)
                    .putExtra(TXT_ARG, txt)
            )
        }
    }

    override fun onFilterClickedApplyListener(
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
        fromPrice: String?, toPrice: String?,
        fromRooms: String?, toRooms: String?,
        fromBathRooms: String?, toBathRooms: String?,
        selectedLatLng: LatLng?,
        distance: Int,

        locationList: ArrayList<PlaceAutocomplete>

        ) {

        vm.selectedCat = category
        vm.selectedSubCat = selectedSubCat
        vm.selectedType = selectedType
        vm.selectedCarMake = selectedCarMake
        vm.selectedModelLocal = selectedModelLocal
        vm.selectedSubModelLocal = selectedSubModelLocal
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
        vm.listLocation=locationList
        vm.onFilterCallBack()
    }

    val Activity.displayMetrics: DisplayMetrics
        get() {
            // display metrics is a structure describing general information
            // about a display, such as its size, density, and font scaling
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= 30){
                display?.apply { getRealMetrics(displayMetrics)
                }
            }else{
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