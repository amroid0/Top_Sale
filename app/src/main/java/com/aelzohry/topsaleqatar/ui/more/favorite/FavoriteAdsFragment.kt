package com.aelzohry.topsaleqatar.ui.more.favorite

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentFavoriteAdsBinding
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.AdsResponse
import com.aelzohry.topsaleqatar.ui.ads.AdsAdapter
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import retrofit2.Call
import kotlin.math.roundToInt

class FavoriteAdsFragment : BaseActivity<FragmentFavoriteAdsBinding, BaseViewModel>() {

    private lateinit var repository: Repository
    private lateinit var adapter: AdsAdapter
    private var adsCall: Call<AdsResponse>? = null

    override fun getLayoutID(): Int = R.layout.fragment_favorite_ads

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {
        initToolbar(getString(R.string.favorites))
        repository = RemoteRepository()

        binding.refreshLayout.setOnRefreshListener { refresh() }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                // check for scroll down
                {
                    val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !didLoadAllPages) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loadNextPage()
                        }
                    }
                }
            }
        })

        var itemWidth = 0
        screenSizeInDp.apply {
            // screen width in dp
            val itemWidthInDp = (x - 12) / 2
            itemWidth = Utils.pxFromDp(applicationContext, itemWidthInDp.toFloat()).toInt()
        }

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = AdsAdapter(itemWidth, {
            vm.onAdClickedListener(binding.root, it)
        }, vm)
        binding.recyclerView.adapter = adapter
        refresh()
    }

    override fun onClickedListener() {

    }

    override fun observerLiveData() {

    }

    override fun onDestroy() {
        adsCall?.cancel()
        super.onDestroy()
    }

    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var didLoadAllPages: Boolean = false

    private fun refresh() {
        adsCall?.cancel()
        adapter.clear()
        didLoadAllPages = false
        isLoading = false
        currentPage = 1
        loadData(1)
    }

    private fun loadData(page: Int) {
        if (isLoading || didLoadAllPages) return
        startLoading()
        adsCall = repository.getFavoriteAds(page) { adsResponse ->
            adsResponse?.data?.ads?.let {
                if (it.isEmpty()) {
                    didLoadAllPages = true
                } else {
                    this.adapter.addAds(it)
                    currentPage = page
                }
            }
            endLoading()
        }
    }

    private fun loadNextPage() {
        loadData(currentPage + 1)
    }

    private fun startLoading() {
        isLoading = true
        binding.refreshLayout.isRefreshing = true
        binding.noDataTextView.visibility = View.GONE
    }

    private fun endLoading() {
        isLoading = false
        binding.refreshLayout.isRefreshing = false
        checkEmptyData()
    }

    private fun checkEmptyData() {
        binding.noDataTextView.visibility = if (adapter.isEmpty()) View.VISIBLE else View.GONE
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
