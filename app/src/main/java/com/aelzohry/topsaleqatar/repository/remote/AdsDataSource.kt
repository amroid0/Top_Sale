package com.aelzohry.topsaleqatar.repository.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.repository.remote.responses.AdsResponse
import com.aelzohry.topsaleqatar.utils.CustomFrame
import com.aelzohry.topsaleqatar.utils.base.baseModel.BaseListModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class AdsDataSource(
    private val prams: HashMap<String, Any>,
    private val typeRequest: Int = FILTER
) :
    PageKeyedDataSource<Int, Ad>() {

    var footerState = MutableLiveData<Boolean>()
    var layoutState = MutableLiveData<CustomFrame.FrameState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Ad>
    ) {

        layoutState.postValue(CustomFrame.FrameState.PROGRESS)

        when (typeRequest) {
            FILTER -> setupAdsRes(ApiClient.api().getAds(1, prams), callback)
            MY_ADS -> setupAdsRes(ApiClient.api().getMyAds(1), callback)
            USER_ADS -> setupAdsRes(
                ApiClient.api().getUserAds(1, prams["user"].toString()),
                callback
            )
            SORT_LATEST -> {
                prams["sortField"]="publishedAt"
                setupAdsRes(ApiClient.api().getAds(1, prams), callback)
            }
            SORT_CLOSEST ->{
                prams["sortField"]="location"
                prams["sortType"]="true"
                setupAdsRes(ApiClient.api().getAds(1, prams), callback)
            }
            // sort by price
            SORT_HIGHER_PRICE ->{
                prams["sortField"]="price"
                prams["sortType"]="false"
                setupAdsRes(ApiClient.api().getAds(1, prams), callback)
            }
            SORT_LOWEST_PRICE -> {
                prams["sortField"]="price"
                prams["sortType"]="true"
                setupAdsRes(ApiClient.api().getAds(1, prams), callback)
            }

            SORT_MINIMUM_MILEAGE -> setupSortRes(ApiClient.api().getAdsByMiles(1), callback)
            SORT_MODEL -> {
                prams["sortField"]="model"
                setupAdsRes(ApiClient.api().getAds(1, prams), callback)
            }
            else -> setupAdsRes(ApiClient.api().getAds(1, prams), callback)
        }

    }

    private fun setupAdsRes(call: Call<AdsResponse>, callback: LoadInitialCallback<Int, Ad>) {
        call.enqueue(object : Callback<AdsResponse> {

            override fun onResponse(call: Call<AdsResponse>, response: Response<AdsResponse>) {
//                Log.e("ads_res",response.body()?.data?.toString())
                response.body()?.data?.let {
                    if (it.ads.isNullOrEmpty()) {
                        layoutState.postValue(CustomFrame.FrameState.NO_ITEM)
                        return
                    }
                    layoutState.postValue(CustomFrame.FrameState.LAYOUT)
                    callback.onResult(
                        it.ads,
                        null,
                        if (response.body()?.data?.ads?.isNullOrEmpty() == true) null else 1
                    )
                }
            }

            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                layoutState.postValue(CustomFrame.FrameState.ERROR)
            }
        })
    }

    private fun setupSortRes(
        call: Call<BaseListModel<Ad>>,
        callback: LoadInitialCallback<Int, Ad>
    ) {
        call.enqueue(object : Callback<BaseListModel<Ad>> {

            override fun onResponse(
                call: Call<BaseListModel<Ad>>,
                response: Response<BaseListModel<Ad>>
            ) {
//                Log.e("ads_res",response.body()?.data?.toString())
                response.body()?.response?.let {
                    if (it.isNullOrEmpty()) {
                        layoutState.postValue(CustomFrame.FrameState.NO_ITEM)
                        return
                    }
                    layoutState.postValue(CustomFrame.FrameState.LAYOUT)
                    callback.onResult(
                        it,
                        null,
                        if (response.body()?.response?.isNullOrEmpty() == true) null else 1
                    )
                }
            }

            override fun onFailure(call: Call<BaseListModel<Ad>>, t: Throwable) {
                layoutState.postValue(CustomFrame.FrameState.ERROR)
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Ad>) {
        footerState.postValue(true)

         when (typeRequest) {
            FILTER -> setupAd((ApiClient.api().getAds(params.key.plus(1), prams)),params,callback)
            MY_ADS -> setupAd((ApiClient.api().getMyAds(params.key.plus(1))),params,callback)
            USER_ADS -> setupAd(ApiClient.api().getUserAds(params.key.plus(1), prams["user"].toString()),params,callback)
            SORT_LATEST ->
            {
                prams["sortField"]="publishedAt"
                setupAd((ApiClient.api().getAds(params.key.plus(1), prams)),params,callback)
            }
            SORT_CLOSEST ->{
                prams["sortField"]="location"
                setupAd((ApiClient.api().getAds(params.key.plus(1), prams)),params,callback)
            }


             // sort by price
             SORT_HIGHER_PRICE ->{
                 prams["sortField"]="price"
                 prams["sortType"]="false"
                 setupAd((ApiClient.api().getAds(params.key.plus(1), prams)),params,callback)
             }
             SORT_LOWEST_PRICE -> {
                 prams["sortField"]="price"
                 prams["sortType"]="true"
                 setupAd((ApiClient.api().getAds(params.key.plus(1), prams)),params,callback)
             }

            SORT_MINIMUM_MILEAGE ->setupListAd( ApiClient.api().getAdsByMiles(params.key.plus(1)),params,callback)
            SORT_MODEL ->
            {
                prams["sortField"]="model"
                setupAd((ApiClient.api().getAds(params.key.plus(1), prams)),params,callback)
            }
            else ->setupAd( ApiClient.api().getAds(params.key.plus(1), prams),params,callback)
        }

    }

    private fun setupAd(
        call: Call<AdsResponse>,
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Ad>
    ) {
        call.enqueue(object : Callback<AdsResponse> {
            override fun onResponse(call: Call<AdsResponse>, response: Response<AdsResponse>) {
                footerState.postValue(false)

                response.body()?.data?.ads?.let {
                    if (it.isNullOrEmpty())
                        return

                    callback.onResult(
                        it,
                        if (it.isNullOrEmpty()) null else
                            params.key.plus(1)
                    )
                }
            }

            override fun onFailure(call: Call<AdsResponse>, t: Throwable) {
                footerState.postValue(false)
            }
        })
    }

    private fun setupListAd(
        call: Call<BaseListModel<Ad>>,
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Ad>
    ) {
        call.enqueue(object : Callback<BaseListModel<Ad>> {
            override fun onResponse(call: Call<BaseListModel<Ad>>, response: Response<BaseListModel<Ad>>) {
                footerState.postValue(false)

                response.body()?.response?.let {
                    if (it.isNullOrEmpty())
                        return

                    callback.onResult(
                        it,
                        if (it.isNullOrEmpty()) null else
                            params.key.plus(1)
                    )
                }
            }

            override fun onFailure(call: Call<BaseListModel<Ad>>, t: Throwable) {
                footerState.postValue(false)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Ad>) {
    }

    class DataSourceFactory(
        private var prams: HashMap<String, Any>,
        private var typeRequest: Int = FILTER
    ) :
        DataSource.Factory<Int, Ad>() {

        val dataSourceLiveData = MutableLiveData<AdsDataSource>()
        var dataSource: AdsDataSource? = null

        override fun create(): DataSource<Int, Ad> {
            dataSource = AdsDataSource(prams, typeRequest)
            dataSourceLiveData.postValue(dataSource)
            return dataSource!!
        }

        fun changePrams(prams: HashMap<String, Any>, typeRequest: Int = this.typeRequest) {
            Log.e("test_params",prams.toString())
            this.prams = prams
            this.typeRequest = typeRequest
            dataSource?.invalidate()

        }
    }

    companion object {
        const val FILTER = 1
        const val MY_ADS = 2
        const val USER_ADS = 3
        const val SORT_LATEST = 4
        const val SORT_CLOSEST = 5
        const val SORT_LOWEST_PRICE = 6
        const val SORT_HIGHER_PRICE = 7
        const val SORT_MINIMUM_MILEAGE = 8
        const val SORT_MODEL = 9

    }
}