package com.aelzohry.topsaleqatar.ui.ads

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ViewHolderAdBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderBannerBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderListAdBinding
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AdBanner
import com.aelzohry.topsaleqatar.model.Banner
import com.aelzohry.topsaleqatar.utils.Binding
import com.aelzohry.topsaleqatar.utils.Binding.setVisible
import com.aelzohry.topsaleqatar.utils.base.BaseViewHolder
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.extenions.setVisible

class AdsAdapter(val itemWidth: Int, val clickListener: (Ad) -> Unit, private val viewModel: BaseViewModel? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isShow: Boolean = true

    companion object {
        private const val GROUP_SIZE = 30
    }

    enum class ViewType {
        Ad, Banner
    }

    fun showOrHideButtons(isShow: Boolean) {
        this.isShow = isShow
    }

    private var models: ArrayList<AdBanner> = arrayListOf()

    private var ads: ArrayList<Ad> = arrayListOf()
    private var banners: ArrayList<Banner> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val isBanner = viewType == ViewType.Banner.ordinal
        return if (isBanner) {
            BaseViewHolder<Banner, ViewHolderBannerBinding>(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_holder_banner, parent, false), viewModel)
        } else {
            if (viewModel?.isAdViewGrid?.get() == true) BaseViewHolder<Ad, ViewHolderAdBinding>(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_holder_ad, parent, false), viewModel) else BaseViewHolder<Ad, ViewHolderListAdBinding>(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_holder_list_ad, parent, false), viewModel)
        }
//        val resource = if (isBanner) R.layout.view_holder_banner else R.layout.view_holder_ad
//        val view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
//        return if (isBanner) BannerViewHolder(view) else AdsViewHolder(view)
    }

    override fun getItemCount(): Int = models.size

    override fun getItemViewType(position: Int): Int = if (models[position] is Ad) ViewType.Ad.ordinal else ViewType.Banner.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val isBanner = getItemViewType(position) == ViewType.Banner.ordinal
        val item = models[position]
        if (item is Banner) {
            val bannerHolder = holder as BaseViewHolder<Banner, ViewHolderBannerBinding>
            val banner = item
            bannerHolder.bind(banner)

            if (TextUtils.isEmpty(banner.photo)) {
                Binding.setImageNoPlaceHolderWithLoader(bannerHolder.binding.iv.context, bannerHolder.binding.iv, bannerHolder.binding.loading,"")
            } else {
                Binding.setImageNoPlaceHolderWithLoader(bannerHolder.binding.iv.context, bannerHolder.binding.iv, bannerHolder.binding.loading, banner.photo)
            }
        } else {

            val ad = item as Ad

            if (viewModel?.isAdViewGrid?.get() == true) {
                val adHolder = holder as BaseViewHolder<Ad, ViewHolderAdBinding>
                changeItemWidth(adHolder.binding)
                adHolder.bind(ad)

                adHolder.binding.llYear.setVisible(true)
                adHolder.binding.llCarData.setVisible(false)
                adHolder.binding.llButtons.isVisible = isShow
                var photoUrl = ""
                if (TextUtils.isEmpty(ad.thumbImageUrl)) {
                    ad.video?.let {
                        photoUrl = it.thumbUrl
                    }
                } else {
                    ad.thumbImageUrl?.let {
                        photoUrl = it
                    }
                }

                Binding.setImageNoPlaceHolderWithLoader(adHolder.binding.iv.context, adHolder.binding.iv, adHolder.binding.loading, photoUrl)


            } else {
                val adHolder = holder as BaseViewHolder<Ad, ViewHolderListAdBinding>
                adHolder.bind(ad)

                adHolder.binding.llButtons.isVisible = isShow

                var photoUrl = ""
                if (TextUtils.isEmpty(ad.thumbImageUrl)) {
                    ad.video?.let {
                        photoUrl = it.thumbUrl
                    }
                } else {
                    ad.thumbImageUrl?.let {
                        photoUrl = it
                    }
                }

                Binding.setImageNoPlaceHolderWithLoader(adHolder.binding.iv.context, adHolder.binding.iv, adHolder.binding.loading, photoUrl)

            }
        }
//        if (isBanner) {
//
//            val bannerHolder = holder as BaseViewHolder<Banner, ViewHolderBannerBinding>
//            val banner = models[position] as Banner
//            bannerHolder.bind(banner)
//
//        } else {
//
//            val ad = models[position] as Ad
//
//            if (viewModel?.isAdViewGrid?.get() == true) {
//                val adHolder = holder as BaseViewHolder<Ad, ViewHolderAdBinding>
//                // TODO get screen width
//                adHolder.binding.llButtons.isVisible = isShow
//                changeItemWidth(adHolder.binding)
//                ad.thumbImageUrl?.let {
//                    Binding.setImageNoPlaceHolderWithLoader(adHolder.binding.iv.context,adHolder.binding.iv,adHolder.binding.loading,ad.thumbImageUrl)
//                }?:{
//                    Binding.setImageNoPlaceHolderWithLoader(adHolder.binding.iv.context,adHolder.binding.iv,adHolder.binding.loading,"")
//                }
//
//                adHolder.bind(ad)
//            } else {
//                val adHolder = holder as BaseViewHolder<Ad, ViewHolderListAdBinding>
//                adHolder.bind(ad)
//            }
//
//
//        }
    }

    fun changeItemWidth(binding: ViewHolderAdBinding) {
        binding.iv.getLayoutParams().width = itemWidth
        binding.iv.getLayoutParams().height = itemWidth
    }

    fun spanSizeAtPosition(position: Int): Int {
        val isBanner = getItemViewType(position) == ViewType.Banner.ordinal
        return if (isBanner) 3 else 1
    }

    fun clear() {
        ads.clear()
        banners.clear()
        notifyDataSetChanged()
    }

    fun addAds(ads: List<Ad>) {
        this.ads.addAll(ads)
        refresh()
    }

    fun setBanners(banners: ArrayList<Banner>) {
        this.banners = banners
        refresh()
    }

    fun refresh() {
        val models = arrayListOf<AdBanner>()

        if (banners.isEmpty()) {
            models.addAll(ads)
        } else {
            val chunks = ads.chunked(GROUP_SIZE)
            chunks.forEachIndexed { index, list ->
                models.addAll(list)
                if (list.size == GROUP_SIZE) {
                    val banner = banners[index % banners.size]
                    models.add(banner)
                }
            }
        }

        this.models = models
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean = models.isEmpty()

}