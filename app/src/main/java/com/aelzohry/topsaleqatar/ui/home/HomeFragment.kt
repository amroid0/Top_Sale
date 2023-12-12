package com.aelzohry.topsaleqatar.ui.home

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentHomeBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderHomeBannerBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.TopBanner
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.ui.ads.AdsAdapter
import com.aelzohry.topsaleqatar.ui.messages.MessagesFragment
import com.aelzohry.topsaleqatar.ui.notification.NotificationActivity
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import com.aelzohry.topsaleqatar.utils.base.BaseFragment
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.onesignal.OneSignal
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView.OnItemChangedListener
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() , OnItemChangedListener<RecyclerView.ViewHolder>{

    private var infiniteFreeAdapter: InfiniteScrollAdapter<RecyclerView.ViewHolder>? = null
    private val timerHandlerForFreeAds = Handler()
    private lateinit var timerRunnableForFreeAds: Runnable

    private lateinit var mListener: MessagesFragment.Listener

    override fun getLayoutID(): Int = R.layout.fragment_home

    override fun getViewModel(): HomeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]


    override fun setupUI() {
        setOneSignalListener()
        var itemWidth = 0
        activity?.screenSizeInDp?.apply {
            // screen width in dp
            val scale = resources.displayMetrics.density
            val dpAsPixels = ((16.0f * 3) * scale + 0.5f).toInt()
            itemWidth = (x - dpAsPixels) / 3
        }

        vm.adsAdapter = AdsAdapter(itemWidth, {}, vm)
        binding.adsRecyclerView.adapter = vm.adsAdapter
        vm.adsAdapter.showOrHideButtons(false)
        setupGridView()

    }


    fun setOneSignalListener() {
        OneSignal.setNotificationWillShowInForegroundHandler {
            Log.e("test_notification", "new notification")
            loadNumberOfUnSeenNotification()
        }
    }

    fun loadNumberOfUnSeenNotification() {
        val repository = RemoteRepository()
        Helper.getUnSeenNotificationNumber(repository, object : Helper.Listener {
            override fun passUnSeenNotificationNumber(notificationNumber: Int,messagesNumber: Int) {
                showOrHideBadge(notificationNumber)
                if (::mListener.isInitialized)
                    mListener.onBadgeChange(notificationNumber)
            }
        })
    }


    private fun setupGridView() {
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return vm.adsAdapter.spanSizeAtPosition(position)
            }
        }
        binding.adsRecyclerView.layoutManager = layoutManager
        binding.adsRecyclerView.adapter = vm.adsAdapter

    }

    private fun setupListView() {
        val layoutManager = LinearLayoutManager(context)
        binding.adsRecyclerView.layoutManager = layoutManager
        binding.adsRecyclerView.adapter = vm.adsAdapter
    }

    override fun onClickedListener() {

        binding.ibNotification.setOnClickListener {
            startActivity(NotificationActivity.newInstance(requireContext()))
        }

        binding.llAddAd.setOnClickListener {
            context?.let {
                vm.onAddAdButtonClickedListener(it)
            }
        }

        binding.btnToggleView.setOnClickListener {

            val oldValue = vm.isAdViewGrid.get() ?: false

            vm.isAdViewGrid.set(!oldValue)

            if (oldValue) setupListView()
            else setupGridView()


        }
//        vm.layoutManagerState.observe(this, Observer {
//            binding.adsRecyclerView.layoutManager =
//                if (it) GridLayoutManager(context, 3) else LinearLayoutManager(context)
//        })

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.onSearchClickedListener(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    override fun observerLiveData() {

        vm.topBannersRes.observe(this) { banners ->
            binding.bannersRecyclerView.adapter =
                BaseAdapter<TopBanner, ViewHolderHomeBannerBinding>(
                    R.layout.view_holder_home_banner,
                    vm, banners
                )
        }

        vm.adsRes.observe(this) { ads ->
            vm.adsAdapter.addAds(ads)
        }

        vm.adsBannerRes.observe(this) { banners ->
            vm.adsAdapter.setBanners(banners)
        }

        vm.notificationNumberRes.observe(this) { number ->
            showOrHideBadge(number)
        }
        vm.bannerSliderRes.observe(this){
            showFreeAdsData(it)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.home_menu, menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_search -> {
//                vm.onSearchClickedListener(binding.root)
//                true
//            }
//            R.id.action_categories -> {
//                vm.onCatsButtonsClickedListener(binding.root)
//                true
//            }
//            else -> false
//        }
//    }
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        setHasOptionsMenu(true)
////        repository = RemoteRepository()
////    }
//
////    override fun onDestroy() {
////        homeCall?.cancel()
////        bannersCall?.cancel()
////        super.onDestroy()
////    }
//
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        return inflater.inflate(R.layout.fragment_home, container, false)
////    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
////        bannersRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//
////        bannersAdapter = HomeBannersAdapter(arrayListOf()) {
////            onTopBannerPressed(it)
////        }
////        bannersRecyclerView.adapter = bannersAdapter
//
//    }


//    private fun search() {
//
//
//    }

//    private fun categories() {
//        val categoriesFragment = CategoriesFragment()
//        (activity as MainActivity).pushFragment(categoriesFragment)
//    }

//    private fun refresh() {
////        bannersAdapter.banners.clear()
////        bannersAdapter.notifyDataSetChanged()
//        adsAdapter.clear()
//        isLoading = false
//        loadHome()
//        loadBanners()
//    }

//    private fun loadBanners() {
//        bannersCall = repository.getBanners {
//            it?.let { banners ->
//                this.adsAdapter.setBanners(banners)
//            }
//        }
//    }

//    private fun loadHome() {
//        if (isLoading) return
//        startLoadingHome()
//        homeCall = repository.getHome {
//            it?.data?.let { data ->
//
//                binding.bannersRecyclerView.adapter =
//                    BaseAdapter<TopBanner, ViewHolderHomeBannerBinding>(
//                        R.layout.view_holder_home_banner,
//                        vm, data.banners
//                    )
//                this.bannersAdapter.banners = data.banners
//                this.bannersAdapter.notifyDataSetChanged()
//                this.adsAdapter.addAds(data.ads)
//            }
//            endLoadingHome()
//        }
//    }

//    private fun startLoadingHome() {
//
//    }
//
//    private fun endLoadingHome() {
//        isLoading = false
//        swipeRefreshLayout.isRefreshing = false
//        contentView.visibility = View.VISIBLE
//    }


//    private fun onAdPressed(ad: Ad) {
//        val detailsFragment = AdDetailsFragment.newInstance(ad)
//        (activity as MainActivity).pushFragment(detailsFragment)
//    }

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
//                point.x = (widthPixels / density).roundToInt()
                point.x = widthPixels

                // screen height in dp
//                point.y = (heightPixels / density).roundToInt()
                point.y = heightPixels
            }

            return point
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as MessagesFragment.Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString()
                        + "must implement the SendMessageListener interface"
            )
        }
    }


    private fun showOrHideBadge(number: Int) {
        if (::mListener.isInitialized)
            mListener.onBadgeChange(number)
        binding.tvNotificationBadge.text = number.toString()
        binding.tvNotificationBadge.setVisible(number != 0)
    }

    fun showFreeAdsData(sliderList: ArrayList<TopBanner>) {
        binding.slider.setVisibility(View.VISIBLE)
        binding.slider.setOrientation(DSVOrientation.HORIZONTAL)
        binding.slider.addOnItemChangedListener(this)
        val mAdapterPromotion = SliderDataAdapter(activity, sliderList) { position ->
            infiniteFreeAdapter?.let {
                val item: TopBanner = sliderList.get(it.getRealPosition(position))
                vm.onBannerClickedListener(binding.slider,item)
            }

        }
        infiniteFreeAdapter = InfiniteScrollAdapter.wrap(mAdapterPromotion)
        binding.slider.setAdapter(infiniteFreeAdapter)
        binding.slider.setOverScrollEnabled(true)
        binding.slider.scrollToPosition(0)
        binding.slider.setSlideOnFling(true)
        binding.slider.setSlideOnFlingThreshold(1000)
        binding.slider.setItemTransitionTimeMillis(500)
        val width_px = Math.round(Resources.getSystem().displayMetrics.widthPixels
            /** 0.8f */
            .toFloat())
        binding.slider.addScrollListener { v, i, i1, viewHolder, t1 ->
            timerHandlerForFreeAds.removeCallbacks(timerRunnableForFreeAds)
            timerHandlerForFreeAds.postDelayed(timerRunnableForFreeAds, 5000)
        }
        binding.slider.setItemTransformer(ScaleTransformer.Builder()
            //                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
            //                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
            //                .setMaxScale(1.05f).setMinScale(0.9f)
            .build())
        timerRunnableForFreeAds = Runnable {
            binding.slider.smoothScrollBy(width_px, 0) // 5 is how many pixels you want it to scroll horizontally by
            timerHandlerForFreeAds.postDelayed(timerRunnableForFreeAds, 5000) // 10 is how many milliseconds you want this thread to run
        }
        timerHandlerForFreeAds.postDelayed(timerRunnableForFreeAds, 5000)
    }

    override fun onCurrentItemChanged(viewHolder: RecyclerView.ViewHolder?, adapterPosition: Int) {

    }
}


