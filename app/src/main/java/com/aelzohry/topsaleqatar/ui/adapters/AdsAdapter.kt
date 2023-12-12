package com.aelzohry.topsaleqatar.ui.adapters

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ViewHolderAdBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderBannerBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderListAdBinding
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.AdBanner
import com.aelzohry.topsaleqatar.model.Banner
import com.aelzohry.topsaleqatar.utils.Binding
import com.aelzohry.topsaleqatar.utils.base.BasePagedAdapter
import com.aelzohry.topsaleqatar.utils.base.BaseViewHolder
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.aelzohry.topsaleqatar.utils.enumClasses.Kilometer
import com.aelzohry.topsaleqatar.utils.extenions.setVisible

/*
* created by : ahmed mustafa
* email : ahmed.mustafa15996@gmail.com
*phone : +201025601465
*/
class AdsAdapter(
    val itemWidth: Int,
    private var vm: BaseViewModel,
) : PagedListAdapter<AdBanner, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<AdBanner>() {

    override fun areItemsTheSame(oldItem: AdBanner, newItem: AdBanner): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: AdBanner, newItem: AdBanner): Boolean = oldItem.id == newItem.id }) {

    private var state = false
    private var isCar = false
    private var showSocialButtons = true

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) if (getItem(position) is Banner) BANNER_TYPE
        else if (vm.isAdViewGrid.get() == true) AD_TYPE else AD_LIST_TYPE
        else LOAD_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && state
    }

    fun setCarCategory(isCar: Boolean) {
        this.isCar = isCar
//        showToast(isCar.toString())

    }

    fun setShowSocialButtons(showSocialButtons: Boolean) {
        this.showSocialButtons = showSocialButtons
//        showToast(isCar.toString())

    }

    fun setState(state: Boolean) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var inflater = LayoutInflater.from(parent.context)

        return if (viewType == AD_TYPE) BaseViewHolder<Ad, ViewHolderAdBinding>(

            DataBindingUtil.inflate(inflater, R.layout.view_holder_ad, parent, false), vm)
        else if (viewType == AD_LIST_TYPE) BaseViewHolder<Ad, ViewHolderListAdBinding>(DataBindingUtil.inflate(inflater, R.layout.view_holder_list_ad, parent, false), vm)
        else if (viewType == BANNER_TYPE) BaseViewHolder<Banner, ViewHolderBannerBinding>(DataBindingUtil.inflate(inflater, R.layout.view_holder_banner, parent, false), vm)
        else BasePagedAdapter.LoadVH(inflater.inflate(R.layout.load_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            AD_TYPE -> {

                val binding = (holder as BaseViewHolder<Ad, ViewHolderAdBinding>).binding
                changeItemWidth(binding)
                getItem(position)?.let {
                    val ads = it as Ad
                    holder.bind(ads)
                    binding.tvKm.setText(Kilometer.getTextByConstant(ads.km))

                    var photoUrl = ""
                    if (TextUtils.isEmpty(ads.thumbImageUrl)) {
                        ads.video?.let {
                            photoUrl = it.thumbUrl
                        }
                    } else {
                        ads.thumbImageUrl?.let {
                            photoUrl = it
                        }
                    }
                    Binding.setImageNoPlaceHolderWithLoader(holder.binding.iv.context, holder.binding.iv, holder.binding.loading, photoUrl)

                }


                binding.llButtons.setVisible(showSocialButtons)



                binding.llCarData.setVisible(isCar)

            }

            AD_LIST_TYPE -> {
                val binding = (holder as BaseViewHolder<Ad, ViewHolderListAdBinding>).binding
                getItem(position)?.let {
                    val ads = it as Ad
                    holder.bind(ads)
                    binding.tvKm.setText(Kilometer.getTextByConstant(ads.km))

                    var photoUrl = ""
                    if (TextUtils.isEmpty(ads.thumbImageUrl)) {
                        ads.video?.let {
                            photoUrl = it.thumbUrl
                        }
                    } else {
                        ads.thumbImageUrl?.let {
                            photoUrl = it
                        }
                    }
                    Binding.setImageNoPlaceHolderWithLoader(holder.binding.iv.context, holder.binding.iv, holder.binding.loading, photoUrl)
                }
                binding.llButtons.setVisible(showSocialButtons)
                binding.llCarData.setVisible(isCar)


            }

            BANNER_TYPE -> {

                getItem(position)?.let {
                    (holder as BaseViewHolder<Banner, ViewHolderBannerBinding>).bind(it as Banner)
                }
            }
        }
    }

    fun changeItemWidth(binding: ViewHolderAdBinding) {
        binding.iv.getLayoutParams().width = itemWidth
        binding.iv.getLayoutParams().height = itemWidth
    }

    companion object {
        const val AD_TYPE = 1
        const val AD_LIST_TYPE = 4
        const val BANNER_TYPE = 2
        const val LOAD_TYPE = 3
    }

}