package com.aelzohry.topsaleqatar.ui.ad_details

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentAdDetailsBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderAdPhotoBinding
import com.aelzohry.topsaleqatar.databinding.ViewHolderCommentBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.ago
import com.aelzohry.topsaleqatar.helper.hideKeyboard
import com.aelzohry.topsaleqatar.helper.toDate
import com.aelzohry.topsaleqatar.model.*
import com.aelzohry.topsaleqatar.ui.ad_details.adapter.AdsDetailsAdapter
import com.aelzohry.topsaleqatar.ui.selectLocationDialog.view.AdsLocationDialogFragment
import com.aelzohry.topsaleqatar.utils.WorkaroundMapFragment
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import com.aelzohry.topsaleqatar.utils.enumClasses.*
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.aelzohry.topsaleqatar.utils.imageSlider.ImageFullScreenActivity
import com.aelzohry.topsaleqatar.utils.imageSlider.ImageSlider
import com.aelzohry.topsaleqatar.utils.imageSlider.adapter.PagerImageAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ligl.android.widget.iosdialog.IOSSheetDialog
import com.squareup.picasso.Picasso
import gun0912.tedimagepicker.util.ToastUtil
import java.util.*

class AdDetailsActivity : BaseActivity<FragmentAdDetailsBinding, AdDetailsViewModel>() {

    var currentImageBitmap: Bitmap? = null

    private lateinit var imageAdapter: PagerImageAdapter
    private var currentPage = 0
    private val llDots: LinearLayoutCompat? = null

    companion object {
        private const val ARG_AD = "AD"
        private const val ARG_AD_ID = "AD_ID"
        private const val ARG_CAME_FROM_CHAT = "CAME_FROM_CHAT"

        @JvmStatic
        fun newInstance(context: Context, ad: Ad?, cameFromChat: Boolean = false, isClear: Boolean = false, adId: String? = "") {

            val i = Intent(context, AdDetailsActivity::class.java).putExtra(ARG_AD_ID, if (adId?.isEmpty() == true) ad?._id else adId).putExtra(ARG_AD, ad).putExtra(ARG_CAME_FROM_CHAT, cameFromChat)

            if (isClear) i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getLayoutID(): Int = R.layout.fragment_ad_details

    override fun getViewModel(): AdDetailsViewModel {

        val ad: Ad? = intent.getParcelableExtra(ARG_AD)
        val adId = intent.getStringExtra(ARG_AD_ID) ?: ""
        return ViewModelProvider(this, ViewModelFactory(ad, adId))[AdDetailsViewModel::class.java]
    }

    override fun setupUI() {
        vm.cameFromChat = intent.getBooleanExtra(ARG_CAME_FROM_CHAT, false)
        initToolbar(getString(R.string.ad_details))
//        vm.adRes.postValue(vm.ad)


    }

    override fun onClickedListener() {
//        binding.ivReport.setOnClickListener { report() }
        binding.reportButton.setOnClickListener { report() }

        binding.ivShare.setOnClickListener {
            vm.initDeepLink(this, true)
        }
        binding.shareButton.setOnClickListener {
            vm.shareImage(currentImageBitmap, this)
        }

        binding.tvCall.setOnClickListener { openDialog() }

        binding.commentEditText.setOnClickListener {
            scrollToView(binding.scroll, binding.llComment)
        }

        binding.commentEditText.setOnFocusChangeListener { view, b ->
            if (b) scrollToView(binding.scroll, binding.llComment)
        }

        binding.userView.setOnClickListener {
            vm.onUserProfileClickedListener(it, activityLauncher)
        }

        binding.userAdsAll.setOnClickListener {
            vm.onUserProfileClickedListener(it, activityLauncher)
        }

    }

    fun scrollToView(nestedScrollView: NestedScrollView, viewToScrollTo: View) {

//        val scrollTo: Int = (viewToScrollTo.getParent().getParent().getParent() as View).top + viewToScrollTo.getTop()
        val scrollTo: Int = (viewToScrollTo.getParent() as View).top + viewToScrollTo.getTop()
        nestedScrollView.smoothScrollTo(0, scrollTo)

//        val vTop: Int = viewToScrollTo.getTop()
//        val vBottom: Int = viewToScrollTo.getBottom()
//        val vHeight: Int = viewToScrollTo.getBottom()- viewToScrollTo.getTop()
//        val sHeight = nestedScrollView.bottom
//        nestedScrollView.smoothScrollTo(0, vTop)

//        nestedScrollView.postDelayed( {
//            nestedScrollView.fullScroll(View.FOCUS_DOWN)
//        },50)
    }

    override fun observerLiveData() {

        vm.adRes.observe(this) {
            setAdData(it)
        }


        vm.userAdsRes.observe(this) {
            binding.userAdsRecyclerView.adapter = RelatedAdsAdapter(it) {
                vm.onAdClickedListener(binding.root, it)
            }
        }

        vm.relatedAdsRes.observe(this) {
            binding.relatedAdsRecyclerView.adapter = RelatedAdsAdapter(it) {
                vm.onAdClickedListener(binding.root, it)
            }
        }

        vm.commentsRes.observe(this) { recentComments ->
            recentComments?.let {
                it.comments?.let {
                    binding.commentsContainerView.setVisible(it.isNotEmpty())
                    binding.moreCommentsView.setVisible(recentComments.loadMore)
                    binding.commentsRecyclerView.adapter = BaseAdapter<Comment, ViewHolderCommentBinding>(R.layout.view_holder_comment, vm, it) { bind, model, position, adapter ->
                        val username = model.user?._name ?: ""

                        bind.usernameTextView.text = username + if (vm.ad?.user?._id == model.user?._id) " [${getString(R.string.author)}]" else ""
                        bind.dateTextView.text = model.createdAt.toDate()?.ago() ?: ""
                        bind.actionsButton.setOnClickListener {
                            setupActionComment(it, model, position, adapter)
                        }

                    }
                }
            }

        }
    }

    private fun openImageInfFullScreen() {
        val photos = vm.ad?.photos ?: return
        if (photos.isEmpty()) return

//        StfalconImageViewer.Builder(this, photos) { view, image ->
//            Picasso.get().load(image.orgUrl).into(view)
//        }.withStartPosition(vm.selectedPhoto.get() ?: 0).show()

        val list: ArrayList<ImageSlider> = ArrayList()
        for (photoItem in photos) {
            list.add(ImageSlider(photoItem.orgUrl))
        }
        startActivity(ImageFullScreenActivity.newInstance(this, list, vm.selectedPhoto.get() ?: 0))
    }

    private fun setAdData(ad: Ad?) {
        ad ?: return

        Log.e("test_load", "hereeee")
        binding.llData.visibility = VISIBLE
        binding.pendingApprovalTextView.setVisible(ad.status != AdStatus.ACTIVE)
        binding.pendingApprovalTextView.text = ad.status.getTitle(this)
        binding.titleTextView.text = ad.title
        binding.llCommentsView.setVisible(ad.iaAllowComments == true)

        val list: ArrayList<ImageSlider> = ArrayList()
        ad.video?.let {
            if (it.isDefault) {
                list.add(ImageSlider(it.thumb, it.thumb, it.orgUrl, false))
            }
        }

        ad.adPhoto.forEach { item ->
            Log.e("test_image", item.orgUrl)
            Log.e("test_image", item.thumbUrl)
            list.add(ImageSlider(item.thumb, item.thumb600, item.thumb600, true))
        }

        ad.video?.let {
            if (!it.isDefault) {
                list.add(ImageSlider(it.thumb, it.thumb, it.orgUrl, false))
            }
        }
        initImageSlider(list)
        binding.imagesRecyclerView.setVisible(list.isNotEmpty())

//            ..mcrm
//        Picasso.get().load(ad.photos.firstOrNull()?.thumb).placeholder(R.mipmap.logo)
//            .into(object : com.squareup.picasso.Target {
//                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                    currentImageBitmap = bitmap;
//                    currentImageView.setImageBitmap(bitmap)
//                }
//
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
//
//                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
//            })

//        Picasso.get().load(ad.photos.firstOrNull()?.orgUrl).placeholder(R.mipmap.logo).into(currentImageView)
        binding.fixedImageView.setVisible(ad.isFixed == true)
        binding.dateTextView.text = ad.publishedAt?.toDate()?.ago() ?: ""
        binding.priceTextView.text = ad.price.toString()
        binding.viewsCountTextView.text = ad.viewsCount.toString()

        if (TextUtils.isEmpty(ad.details)) {
            binding.llDesc.visibility = View.GONE
        } else {
            binding.llDesc.visibility = View.VISIBLE
            binding.detailsTextView.text = ad.details
        }

        vm.viewCount.set(ad.viewsCount)
        vm.favState.set(ad.isFavourite)
        vm.followState.set(ad.user?.isFollowing)
        vm.likeCount.set(ad.likesCount)
        vm.commentsCount.set(ad.commentsCount)
        vm.likeState.set(ad.isLiked)
        vm.fixed.set(ad.isFixed)
        // gallery

        if (ad.location == null) {
            binding.locationView.visibility = View.GONE
            binding.llMapView.visibility = View.GONE
            binding.locationTextView.text = ""
        } else {
            ad.location.let {
                if (it.coordinates[0] != 0.0 || it.coordinates[1] != 0.0) {
                    binding.locationView.visibility = View.VISIBLE
                    binding.llMapView.visibility = View.VISIBLE
                    binding.locationTextView.text = it.address ?: ""
                    setupLocation(it)


                } else {
                    binding.locationView.visibility = View.GONE
                    binding.llMapView.visibility = View.GONE
                    binding.locationTextView.text = ""
                }

            }
        }

        val imagesPreviewList = arrayListOf<Photo>()

        ad.video?.let {
            if (it.isDefault){
            imagesPreviewList.add(it)
        }
        }

        ad.adPhoto.let {
            imagesPreviewList.addAll(it)
        }

        ad.video?.let {
            if (!it.isDefault) {
                imagesPreviewList.add(it)
            }
        }

        binding.imagesRecyclerView.adapter = BaseAdapter<Photo, ViewHolderAdPhotoBinding>(R.layout.view_holder_ad_photo, vm, imagesPreviewList) { vhBind, model, position, adapter ->
            vhBind.imageView.setOnClickListener {
                binding.viewPager.setCurrentItem(position, true)
                vm.onImageClickedListener(model, position)
            }
        }

        val adsItemList: ArrayList<AdsItem> = ArrayList()

        adsItemList.add(AdsItem(getString(R.string.price), String.format(Locale.ENGLISH, "%.0f", ad.price) + " " + getString(R.string.currency)))


        if (ad.category != null) {
            adsItemList.add(AdsItem(getString(R.string.category), ad.category.title.localized))
        }

        if (ad.subcategory != null) {
            adsItemList.add(AdsItem(getString(R.string.subcategory), ad.subcategory.title.localized))
        }

        if (ad.type != null) {
            adsItemList.add(AdsItem(getString(R.string.type), ad.type.title.localized))
        }

        if (ad.region != null) {
            adsItemList.add(AdsItem(getString(R.string.region), ad.region.title.localized))
        }

        if (ad.numberOfRooms != null && ad.numberOfRooms != 0) {
//            adsItemList.add(AdsItem(getString(R.string.rooms_numbers), ad.numberOfRooms.toString()))
            adsItemList.add(AdsItem(getString(R.string.rooms_numbers), RoomSize.getTextByConstant(ad.numberOfRooms.toString())))
        }

        if (ad.numberOfBathroom != null && ad.numberOfBathroom != 0) {
            adsItemList.add(AdsItem(getString(R.string.bath_room_number), RoomSize.getTextByConstant(ad.numberOfBathroom.toString())))
        }

        if (ad.space != null && ad.space != 0) {
            adsItemList.add(AdsItem(getString(R.string.space_hint), ad.space.toString()))
        }

        if (ad.carMake != null) {
            adsItemList.add(AdsItem(getString(R.string.car_make), ad.carMake.localized))
        }

        if (ad.carModel != null) {
            adsItemList.add(AdsItem(getString(R.string.car_model), ad.carModel.localized))
        }
        if (ad.carSubModel != null) {
            adsItemList.add(AdsItem(getString(R.string.car_sub_model), ad.carSubModel.localized))
        }

        if (!TextUtils.isEmpty(ad.carYear)) {
            adsItemList.add(AdsItem(getString(R.string.car_year), ad.carYear))
        }

        if (!TextUtils.isEmpty(ad.gearBox)) {
            adsItemList.add(AdsItem(getString(R.string.motion_vector), MotionVector.getTextByConstant(ad.gearBox)))
        }

        if (!TextUtils.isEmpty(ad.engineCapacity)) {
            adsItemList.add(AdsItem(getString(R.string.engine_size), EngineSize.getTextByConstant(ad.engineCapacity)))
        }


        if (!TextUtils.isEmpty(ad.fuelType)) {
            adsItemList.add(AdsItem(getString(R.string.fuel_type), FuelType.getTextByConstant(ad.fuelType)))
        }

        if (!TextUtils.isEmpty(ad.pushFatisType)) {
            adsItemList.add(AdsItem(getString(R.string.engine_drive_system), EngineDriveSystem.getTextByConstant(ad.pushFatisType)))
        }

        if (!TextUtils.isEmpty(ad.color)) {
            adsItemList.add(AdsItem(getString(R.string.car_color), CarColor.getTextByConstant(ad.color)))
        }

        if (!TextUtils.isEmpty(ad.km)) {
            adsItemList.add(AdsItem(getString(R.string.car_km), Kilometer.getTextByConstant(ad.km)))
        }

        if (ad.location != null) {
            ad.location.let {
                if (it.coordinates[0] != 0.0 || it.coordinates[1] != 0.0) {
                    adsItemList.add(AdsItem(getString(R.string.location), it.address))
                }
            }
        }

        val adsItemAdapter = AdsDetailsAdapter(adsItemList, this)
        binding.rvDetails.adapter = adsItemAdapter

        // category & type
//        categoryTextView.text = ad.category?.title?.localized ?: ""

//        subcategoryView.setVisible(ad.subcategory != null)// = if () View.GONE else View.VISIBLE
//        subcategoryTextView.text = ad.subcategory?.title?.localized ?: ""

//        typeView.visibility = if (ad.type == null) View.GONE else View.VISIBLE
//        typeTextView.text = ad.type?.title?.localized ?: ""

//        regionView.visibility = if (ad.region == null) View.GONE else View.VISIBLE
//        regionTextView.text = ad.region?.title?.localized ?: ""

//        roomsView.visibility = if (ad.numberOfRooms == null || ad.numberOfRooms == 0) View.GONE else View.VISIBLE
//        roomsTextView.text = ad.numberOfRooms.toString() ?: ""

//        bathRoomView.visibility = if (ad.numberOfBathroom == null || ad.numberOfBathroom == 0) View.GONE else View.VISIBLE
//        bathRoomTextView.text = ad.numberOfBathroom.toString() ?: ""

//        spaceView.visibility = if (ad.space == null || ad.space == 0) View.GONE else View.VISIBLE
//        spaceTextView.text = ad.space.toString() ?: ""

//        carMakeView.visibility = if (ad.carMake == null) View.GONE else View.VISIBLE
//        carMakeTextView.text = ad.carMake?.title ?: ""

//        carModelView.visibility = if (ad.carModel == null) View.GONE else View.VISIBLE
//        carModelTextView.text = ad.carModel?.title ?: ""

//        carYearView.visibility = if (ad.carYear?.trim().isNullOrEmpty()) View.GONE else View.VISIBLE
//        carYearTextView.text = ad.carYear ?: ""

//        carMotionView.visibility = if (ad.gearBox?.trim().isNullOrEmpty()) View.GONE else View.VISIBLE
//        carEngineView.visibility = if (ad.engineCapacity?.trim().isNullOrEmpty()) View.GONE else View.VISIBLE
//        carKmView.visibility = if (ad.km?.trim().isNullOrEmpty()) View.GONE else View.VISIBLE

//        carMotionTextView.text = MotionVector.getTextByConstant(ad.gearBox)
//        carEngineTextView.text = ad.engineCapacity ?: ""
//        carKmTextView.text = Kilometer.getTextByConstant(ad.km)

        // user
        binding.usernameTextView.text = ad.user?._name
        binding.mobileTextView.text = ad.user?.mobile
        ad.user?.avatarUrl?.let {
            Picasso.get().load(it).placeholder(R.drawable.avatar).into(binding.avatarImageView)
        }
//        emailButton.visibility = if (ad.user?.email?.trim().isNullOrEmpty()) View.GONE else View.VISIBLE

    }

    @SuppressLint("NotifyDataSetChanged")
    fun initImageSlider(list: ArrayList<ImageSlider>) {
        if (list.isEmpty()) return
        imageAdapter = PagerImageAdapter(this, list)
        imageAdapter.notifyDataSetChanged()
        binding.viewPager.setOffscreenPageLimit(list.size)
        initViewPagerWithTimer(list)
    }

    fun initViewPagerWithTimer(list: ArrayList<ImageSlider>) {
//        Collections.reverse(list);
        currentPage = 0
        binding.viewPager.adapter = imageAdapter
        binding.viewPager.currentItem = currentPage
        //        viewPager.setOffscreenPageLimit(currentPage);
        addDots(list.size)
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                addDots(list.size)
            }
        })

//        val handler = Handler()
//        val update = Runnable {
//            if (currentPage == list.size) {
//                currentPage = 0
//            }
//            viewPager.setCurrentItem(currentPage++, true)
//        }
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(update)
//            }
//        }, 5000, 5000)
    }

    private fun addDots(listFeaturesSize: Int) {
        binding.llDots.removeAllViews()
        for (i in 0 until listFeaturesSize) {
            val ll = getLayoutInflater().inflate(R.layout.item_dot, null) as LinearLayout
            val ivDot = ll.findViewById<ImageView>(R.id.img_dot)
            //            if (ToolUtils.isArabicLanguage()) {
//                if (i == featureBeans.size() - 1) {
//                    ivDot.setImageDrawable(getResources().getDrawable(R.drawable.ic_selected_dot));
//                } else {
//                    ivDot.setImageDrawable(getResources().getDrawable(R.drawable.ic_un_selected_dot));
//                }
//            } else {
            if (i == currentPage) {
                ivDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_selected_dot))
            } else {
                ivDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_un_selected_dot))
            }
            //            }
            binding.llDots.addView(ll)
        }
    }

    private fun setupLocation(location: Location) {
        (supportFragmentManager.findFragmentById(R.id.view_map) as SupportMapFragment?)?.let {

            it.getMapAsync { mMap ->
                // For dropping a marker at a point on the Map
                val latLng = LatLng(location.coordinates[1], location.coordinates[0])
                mMap.addMarker(MarkerOptions().position(latLng).title(location.address).snippet(location.address))

//                mMap.addPolygon(MapHelper.createPolygonWithCircle(this, sydney, 2))
//                MapHelper.drawCircle(this, mMap, sydney, 300.0)
//                MapHelper.drawCircle(this, mMap, latLng, 300.0)
                // For zooming automatically to the location of the marker
                val cameraPosition = CameraPosition.Builder().target(latLng).zoom(14f).build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                mMap.setOnMapClickListener {
                    val dialog: AdsLocationDialogFragment = AdsLocationDialogFragment.newInstance(location.coordinates[1], location.coordinates[0])
                    dialog.setListener(object : AdsLocationDialogFragment.LocationListener {
                        override fun onLocationSelected(location: LatLng?, distance: Int) {
                        }

                    })
                    supportFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
                }

                mMap.getUiSettings().setScrollGesturesEnabled(false)
                mMap.getUiSettings().isZoomGesturesEnabled = false
                mMap.getUiSettings().isScrollGesturesEnabledDuringRotateOrZoom = false
                mMap.getUiSettings().isMapToolbarEnabled = true

                binding.llMapView.setOnClickListener {

                }
            }
        }


    }

    private val EARTH_RADIUS = 6371
    private fun createOuterBounds(): List<LatLng?>? {
        val delta = 0.01f
        return object : ArrayList<LatLng?>() {
            init {
                add(LatLng((90 - delta).toDouble(), (-180 + delta).toDouble()))
                add(LatLng(0.0, (-180 + delta).toDouble()))
                add(LatLng((-90 + delta).toDouble(), (-180 + delta).toDouble()))
                add(LatLng((-90 + delta).toDouble(), 0.0))
                add(LatLng((-90 + delta).toDouble(), (180 - delta).toDouble()))
                add(LatLng(0.0, (180 - delta).toDouble()))
                add(LatLng((90 - delta).toDouble(), (180 - delta).toDouble()))
                add(LatLng((90 - delta).toDouble(), 0.0))
                add(LatLng((90 - delta).toDouble(), (-180 + delta).toDouble()))
            }
        }
    }

    private fun createHole(center: LatLng, radius: Int): Iterable<LatLng>? {
        val points = 50 // number of corners of inscribed polygon
        val radiusLatitude = Math.toDegrees((radius / EARTH_RADIUS as Float).toDouble())
        val radiusLongitude = radiusLatitude / Math.cos(Math.toRadians(center.latitude))
        val result: MutableList<LatLng> = ArrayList(points)
        val anglePerCircleRegion = 2 * Math.PI / points
        for (i in 0 until points) {
            val theta = i * anglePerCircleRegion
            val latitude = center.latitude + radiusLatitude * Math.sin(theta)
            val longitude = center.longitude + radiusLongitude * Math.cos(theta)
            result.add(LatLng(latitude, longitude))
        }
        return result
    }

    private fun report() {
        if (!vm.isLogin) {
            vm.snackLogin.postValue(true)
            return
        }

        val dialog = AlertDialog.Builder(this)
        val editText = EditText(this)
        editText.hint = getString(R.string.suitable_reason)
        dialog.setTitle(getString(R.string.report))
        dialog.setView(editText)

        dialog.setPositiveButton(getString(R.string.send)) { _, _ ->
            val text = editText.text.toString()
            if (text.isEmpty()) return@setPositiveButton
            editText.hideKeyboard()
            vm.onReportAdClickedListener(text)
        }

        dialog.setNegativeButton(getString(R.string.cancel)) { _, _ ->

        }

        dialog.show()
    }

    private fun setupActionComment(v: View, comment: Comment, position: Int, adapter: BaseAdapter<Comment, ViewHolderCommentBinding>): PopupMenu {

        val popupMenu = PopupMenu(v.context, v)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.comments_menu, menu)
        menu.findItem(R.id.edit).isVisible = comment.isCommentOwner
        menu.findItem(R.id.delete).isVisible = comment.isAdOwner || comment.isCommentOwner
        menu.findItem(R.id.email).isVisible = (comment.user?.email?.isNotEmpty() ?: false) && !(comment.isAdOwner || comment.isCommentOwner)
        menu.findItem(R.id.profile).isVisible = true
        menu.findItem(R.id.block).isVisible = true
        menu.findItem(R.id.call).isVisible = true
        menu.findItem(R.id.chat).isVisible = comment.isAdOwner

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.delete -> vm.onDeleteCommentClickedListener(comment, v) { adapter.removeItem(position) }

                R.id.edit -> editCommentDialog(comment, v) { text ->
                    comment.text = text
                    binding.commentsRecyclerView.adapter?.notifyDataSetChanged()
                }

                R.id.block -> vm.onBlocCommentClickedListener(comment, v) {
                    finish()
                }

                R.id.profile -> vm.onProfileCommentClickedListener(comment, v) { }

                R.id.call -> vm.onCallCommentClickedListener(comment, v) { }
                R.id.chat -> vm.onChatCommentClickedListener(comment, v) { }
                R.id.email -> vm.onEmailCommentClickedListener(comment, v) { }
            }
            true
        }

        popupMenu.show()
        return popupMenu
    }

    private fun editCommentDialog(comment: Comment, v: View, result: (newComment: String) -> Unit) {
        val dialog = AlertDialog.Builder(this)
        val editText = EditText(this)
        editText.hint = getString(R.string.comment)
        editText.setText(comment.text)
        dialog.setTitle(getString(R.string.edit_comment))
        dialog.setView(editText)

        dialog.setPositiveButton(getString(R.string.edit)) { _, _ ->
            val text = editText.text.toString()
            if (text.isEmpty()) return@setPositiveButton
            vm.onEditCommentClickedListener(comment, text, v) { result(text) }
        }

        dialog.setNegativeButton(getString(R.string.cancel)) { _, _ ->

        }

        dialog.show()

    }

    fun openDialog() {
        val list: java.util.ArrayList<IOSSheetDialog.SheetItem> = java.util.ArrayList<IOSSheetDialog.SheetItem>()
        val adItem: Ad? = vm.ad
        val userAdsOwner: User?
        if (adItem != null) {
            userAdsOwner = adItem.user
            if (userAdsOwner == null) return
        } else {
            return
        }
        if (!TextUtils.isEmpty(userAdsOwner.mobile)) {
            list.add(IOSSheetDialog.SheetItem(getString(R.string.call_item), Color.parseColor("#000000")))
            list.add(IOSSheetDialog.SheetItem(getString(R.string.msg_item), Color.parseColor("#000000")))
            list.add(IOSSheetDialog.SheetItem(getString(R.string.whatsapp_item), Color.parseColor("#000000")))
            list.add(IOSSheetDialog.SheetItem(getString(R.string.copy_item), Color.parseColor("#000000")))
            list.add(IOSSheetDialog.SheetItem(getString(R.string.chat_item), Color.parseColor("#000000")))
        }

        if (!TextUtils.isEmpty(userAdsOwner.email)) {
            list.add(IOSSheetDialog.SheetItem(getString(R.string.email_item), Color.parseColor("#000000")))
        }

        val dialog = IOSSheetDialog.Builder(this).setCancelText(getString(R.string.cancel)).setData(list) { dialogInterface: DialogInterface?, i: Int ->
            if (TextUtils.isEmpty(userAdsOwner.mobile)) {
                when (i) {
                    0 -> {
                        sendEmail(userAdsOwner.email)
                    }
                }
            } else {
                when (i) {
                    0 -> call(userAdsOwner.mobile)

                    1 -> {
                        sendTextMsg(userAdsOwner.mobile)
                    }

                    2 -> {
                        openWhatsapp(userAdsOwner.mobile)
                    }

                    3 -> {
                        copyNumber(userAdsOwner.mobile)
                    }

                    4 -> {
                        openChat(userAdsOwner.mobile)
                    }

                    5 -> {
                        sendEmail(userAdsOwner.email)
                    }

                }
            }

        }.show()

        val params: ViewGroup.LayoutParams = dialog.window!!.attributes
        // Assign window properties to fill the parent
        // Assign window properties to fill the parent
        params.width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        params.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as WindowManager.LayoutParams

    }

    fun call(mobile: String) {
        if (TextUtils.isEmpty(mobile)) return
        Helper.callPhone(mobile, this)
    }

    fun sendTextMsg(mobile: String?) {
        if (TextUtils.isEmpty(mobile)) return
        Helper.sendSMS(mobile, this)

    }

    fun sendEmail(email: String?) {
        if (TextUtils.isEmpty(email)) return
        Helper.sendEmail(email, this)
    }

    fun openChat(mobile: String) {
        vm.onChatClickedListener(binding.callButton)
    }

    fun openWhatsapp(mobile: String) {
        if (TextUtils.isEmpty(mobile)) return
        val url = "https://wa.me/${mobile}"
        Helper.openUrl(url, this)
    }

    fun copyNumber(mobile: String) {
        if (TextUtils.isEmpty(mobile)) return
        Helper.copyNumber(mobile, this)

    }



}
