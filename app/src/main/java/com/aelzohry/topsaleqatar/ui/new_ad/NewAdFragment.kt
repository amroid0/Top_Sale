package com.aelzohry.topsaleqatar.ui.new_ad

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aelzohry.topsaleqatar.App.Companion.context
import com.aelzohry.topsaleqatar.BR
import com.aelzohry.topsaleqatar.BuildConfig
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.AqarAttrBinding
import com.aelzohry.topsaleqatar.databinding.CatCarAttrBinding
import com.aelzohry.topsaleqatar.databinding.CatCarFourAttrBinding
import com.aelzohry.topsaleqatar.databinding.FragmentNewAdBinding
import com.aelzohry.topsaleqatar.databinding.ListItem2Binding
import com.aelzohry.topsaleqatar.databinding.ListItem3Binding
import com.aelzohry.topsaleqatar.databinding.ListItemBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.model.Ad
import com.aelzohry.topsaleqatar.model.Category
import com.aelzohry.topsaleqatar.model.CategoryClass
import com.aelzohry.topsaleqatar.model.LocalStanderModel
import com.aelzohry.topsaleqatar.model.Photo
import com.aelzohry.topsaleqatar.model.StanderModel
import com.aelzohry.topsaleqatar.model.StanderModel1
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.ui.carShows.view.CarShowsFragment
import com.aelzohry.topsaleqatar.ui.new_ad.adLocation.AdLocationActivity
import com.aelzohry.topsaleqatar.ui.new_ad.categorisDialog.view.CategoriesDialogFragment
import com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.view.CustomPhotosActivity
import com.aelzohry.topsaleqatar.utils.GPSTracker
import com.aelzohry.topsaleqatar.utils.ImageCompress
import com.aelzohry.topsaleqatar.utils.ItemsDialog.DialogItem
import com.aelzohry.topsaleqatar.utils.ItemsDialog.ItemsDialogFragment
import com.aelzohry.topsaleqatar.utils.Localization
import com.aelzohry.topsaleqatar.utils.MyFileUtils
import com.aelzohry.topsaleqatar.utils.MyFileUtils.getPath
import com.aelzohry.topsaleqatar.utils.URIPathHelper
import com.aelzohry.topsaleqatar.utils.Utils
import com.aelzohry.topsaleqatar.utils.ValidationUtils
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseAdapter
import com.aelzohry.topsaleqatar.utils.base.RequestListener
import com.aelzohry.topsaleqatar.utils.enumClasses.CarColor
import com.aelzohry.topsaleqatar.utils.enumClasses.EngineDriveSystem
import com.aelzohry.topsaleqatar.utils.enumClasses.EngineSize
import com.aelzohry.topsaleqatar.utils.enumClasses.FuelType
import com.aelzohry.topsaleqatar.utils.enumClasses.Kilometer
import com.aelzohry.topsaleqatar.utils.enumClasses.MotionVector
import com.aelzohry.topsaleqatar.utils.enumClasses.RoomSize
import com.aelzohry.topsaleqatar.utils.extenions.setVisible
import com.aelzohry.topsaleqatar.utils.extenions.setupDialog
import com.aelzohry.topsaleqatar.utils.extenions.showToast
import com.aelzohry.topsaleqatar.utils.extenions.snackBar
import com.aelzohry.topsaleqatar.utils.imageSlider.ImageFullScreenActivity
import com.aelzohry.topsaleqatar.utils.imageSlider.ImageSlider
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.IpCons.RC_IMAGE_PICKER
import com.esafirm.imagepicker.model.Image
import com.kaopiz.kprogresshud.KProgressHUD
import com.otaliastudios.transcoder.Transcoder
import com.otaliastudios.transcoder.TranscoderListener
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.FISHBUN_REQUEST_CODE
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.MimeType
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.Calendar
import java.util.Objects
import java.util.UUID


class NewAdFragment : BaseActivity<FragmentNewAdBinding, NewAdViewModel>(), ImageCompress.onFinishedCompressListListener {

    private lateinit var imageCompress: ImageCompress
    private lateinit var filePathImageCamera: File

    private var deletedPhotos: ArrayList<String> = arrayListOf()
    private lateinit var imagesAdapter: ImagesAdapter
    private var catDialog: Dialog? = null
    private var regionDialog: Dialog? = null
    private var subCatDialog: Dialog? = null
    private var citiesAdLocationDialog: Dialog? = null
    private var typeDialog: Dialog? = null
    private var carStepDialog: Dialog? = null
    private var carEngineColorFuelDialog: Dialog? = null
    private var aqarStepDialog: Dialog? = null

    private var currentStep = 1
    var hud : KProgressHUD? =null
    override fun getLayoutID(): Int = R.layout.fragment_new_ad

    private val images = arrayListOf<Image>()

    override fun getViewModel(): NewAdViewModel = ViewModelProvider(this)[NewAdViewModel::class.java]

    override fun setupUI() {
        imageCompress = ImageCompress(this)
           hud  = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
            .setLabel("optimizing video...")
            .setMaxProgress(100)

        intent?.let {
            // coming from edit mode
            vm.ad = it.getParcelableExtra(ARG_AD)
        }

        imagesAdapter = ImagesAdapter(arrayListOf(), 0, { it, view ->
            // delete or make default
            didTapImage(it, view)
        }) {
            // add new
            // show alert for choosing camera or gallery
            //pickImages()
        }
        binding.imagesRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.imagesRecyclerView.setHasFixedSize(true)
        binding.imagesRecyclerView.adapter = imagesAdapter

        if (vm.ad != null) {
            // coming from edit mode
            binding.publishButton.setText(R.string.save)
            editAd()
        }
        initToolbar(getString(if (vm.ad == null) R.string.new_ad else R.string.edit_ad))

        // hide region in case we choose building , so we should select rooms and bathrooms
//        propertiesButtonRegion.visibility = View.GONE
//        propertiesButton.visibility = View.GONE

        setCurrentUi()


    }

    private fun setCurrentUi() {
        Localization.setLanguage(this, Helper.languageCode)
        Utils.hideKeyboard(this)

        when (currentStep) {
            1 -> {
                binding.llStepOne.visibility = View.VISIBLE
                binding.llStepTwo.visibility = View.GONE
                binding.llStepThree.visibility = View.GONE

                binding.viewOne.setBackgroundResource(R.drawable.progress_view_selected)
                binding.viewTwo.setBackgroundResource(R.drawable.progress_view_un_selected)
                binding.viewThree.setBackgroundResource(R.drawable.progress_view_un_selected)

                binding.tvViewOne.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                binding.tvViewTwo.setTextColor(ContextCompat.getColor(this, R.color.grayBorderColor))
                binding.tvViewThree.setTextColor(ContextCompat.getColor(this, R.color.grayBorderColor))

                binding.publishButton.text = getString(R.string.next)
            }

            2 -> {
                binding.llStepOne.visibility = View.GONE
                binding.llStepTwo.visibility = View.VISIBLE
                binding.llStepThree.visibility = View.GONE

                binding.viewOne.setBackgroundResource(R.drawable.progress_view_un_selected)
                binding.viewTwo.setBackgroundResource(R.drawable.progress_view_selected)
                binding.viewThree.setBackgroundResource(R.drawable.progress_view_un_selected)

                binding.tvViewOne.setTextColor(ContextCompat.getColor(this, R.color.grayBorderColor))
                binding.tvViewTwo.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                binding.tvViewThree.setTextColor(ContextCompat.getColor(this, R.color.grayBorderColor))
                binding.publishButton.text = getString(R.string.next)
            }

            3 -> {
                binding.llStepOne.visibility = View.GONE
                binding.llStepTwo.visibility = View.GONE
                binding.llStepThree.visibility = View.VISIBLE

                binding.viewOne.setBackgroundResource(R.drawable.progress_view_un_selected)
                binding.viewTwo.setBackgroundResource(R.drawable.progress_view_un_selected)
                binding.viewThree.setBackgroundResource(R.drawable.progress_view_selected)

                binding.tvViewOne.setTextColor(ContextCompat.getColor(this, R.color.grayBorderColor))
                binding.tvViewTwo.setTextColor(ContextCompat.getColor(this, R.color.grayBorderColor))

                binding.locationButton.setVisible(vm.selectedCat?.isLocationRequired == true)
                binding.tvViewThree.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                binding.publishButton.text = getString(R.string.publish)

            }
        }
    }

    override fun onClickedListener() {
        binding.categoryButton.setOnClickListener {
//            catDialog?.show()
            openCategoriesDialog()
        }
        binding.subcategoryButton.setOnClickListener { subCatDialog?.show() }
        binding.cityLocation.setOnClickListener { citiesAdLocationDialog?.show() }
        binding.typeButton.setOnClickListener { typeDialog?.show() }
        binding.imageViewGallery.setOnClickListener {
            pickImages()
        }
        binding.imageViewCamera.setOnClickListener {
            pickImagesFromCamera()
        }

        binding.propertiesButton.setOnClickListener {
            setupAqarStepDialog()
            aqarStepDialog?.show()
        }
        binding.propertiesButtonRegion.setOnClickListener {
            regionDialog?.show()
        }
        binding.carAttrButton.setOnClickListener {
            setupCarStepDialog()
            carStepDialog?.show()
        }

        binding.edKm.setOnClickListener {
            setupCarMotionVectorAttribute()
            carStepDialog?.show()
        }

        binding.publishButton.setOnClickListener {
            if (currentStep == 1) {
                if (validateFirstStep()) {
                    currentStep = 2
                    setCurrentUi()
                }
            } else if (currentStep == 2) {
                if (validateSecondStep()) {
                    currentStep = 3
                    setCurrentUi()
                }
            } else if (currentStep == 3) {
                if (validateThirdStep()) {
                    publish()
                }
            }
        }
        binding.locationButton.setOnClickListener {
            picLocation()
        }

        binding.edEngineDriveFuelColor.setOnClickListener {
            setupCarEngineAndColorAndFuel()
            carEngineColorFuelDialog?.show()
        }


        binding.llAllowComments.setOnClickListener {
            binding.toggleAllowComments.performClick()
        }

        /*binding.edCarShow.setOnClickListener {
            val dialog: CarShowsFragment = CarShowsFragment.newInstance()
            supportFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
            dialog.setListener(object : CarShowsFragment.Listener {
                override fun onItemSelected(user: User) {
                    if (user.isChecked) {
                        vm.selectedCarShow = user
                        binding.edCarShow.text = user._name
                    } else {
                        vm.selectedCarShow = null
                        binding.edCarShow.text = ""
                    }

                }

            })
        }*/

    }

    private fun checkIfAddedVideo(): Boolean {
        imagesAdapter.images.forEach {
            if (!it.isImage) {
                showToast(getString(R.string.only_one_video))
                return true
            }
        }
        return false
    }

    private var currentLocation: GPSTracker? = null

    private fun requestCurrentLocation() {
        startActivityForResult(Intent(this, AdLocationActivity::class.java), PLACE_PICKER_REQUEST)

//        currentLocation = GPSTracker(this) { location, err ->
//            if (err != null) {
//                Toast.makeText(
//                    this, this.getString(R.string.location_err),
//                    Toast.LENGTH_LONG
//                ).show()
//            } else {
//                if (currentLocation != null) {
//                    lifecycle.removeObserver(currentLocation!!)
//                }
//                startActivityForResult(
//                    Intent(this, AdLocationActivity::class.java),
//                    PLACE_PICKER_REQUEST
//                )
//            }
//
//        }
//        lifecycle.addObserver(currentLocation!!)
    }

    private fun picLocation() {
        requestCurrentLocation()
    }

    override fun observerLiveData() {

        vm.catsRes.observe(this) {

//            val list = ArrayList<LocalStanderModel>()
//            it.forEach {
//                list.add(
//                    LocalStanderModel(
//                        it._id,
//                        LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)
//                    )
//                )
//            }
            catDialog = Dialog(this)

//            setupDialog(catDialog, list, { modelLocal: LocalStanderModel, i ->
//                // when select category
//                // check for category is equal to lands to show field of space
//                if (it[i].type == "lands") {
//                    // show the text box of space
//                    space_EditText.visibility = View.VISIBLE
//                } else {
//                    // hide the text box of space
//                    space_EditText.visibility = View.GONE
//                }
//                vm.onCatSelectedListener(it[i])
//            })
        }
        vm.regionRes.observe(this) {
            Log.e("test_data", "region data" + it.size + "")
            val list = ArrayList<LocalStanderModel>()
            it.forEach {
                list.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)))
            }
            regionDialog = Dialog(this)
            setupDialog(regionDialog, list, { modelLocal: LocalStanderModel, i ->
                vm.onRegionSelectedListener(modelLocal)
            })
        }

        vm.subCatsRes.observe(this) {
            if (it != null) {
                subCatDialog = Dialog(this)
                setupDialog(subCatDialog, it) { modelLocal: LocalStanderModel ->
                    vm.onSubCatSelectedListener(modelLocal)
                }
            }

        }


        vm.citiesRes.observe(this) {
            val list = ArrayList<LocalStanderModel>()
            it.forEach {
                list.add(LocalStanderModel(it._id, LocalStanderModel.LocalizedModel(it.title.ar, it.title.en)))
            }
            citiesAdLocationDialog = Dialog(this)
            setupDialog(citiesAdLocationDialog, list, { modelLocal: LocalStanderModel, i ->
                vm.onCityAdLocationSelectedListener(modelLocal)
            })
        }


        vm.typeRes.observe(this) {
            if (it != null) {
                typeDialog = Dialog(this)
                setupDialog(typeDialog, it) { modelLocal: LocalStanderModel ->
                    vm.onSelectedTypeListener(modelLocal)
                }
            }

        }

    }

    private fun openCategoriesDialog() {

        val listData = ArrayList(vm.catsRes.value)
        val dialogFragment: CategoriesDialogFragment = CategoriesDialogFragment.newInstance(listData)

        dialogFragment.setListener(object : CategoriesDialogFragment.Listener {
            override fun onItemSelected(itemSelected: Category) {
//                restAllEditText()
                // when select category
                // check for category is equal to lands to show field of space
                Log.e("test_type", itemSelected.type.toString())
                if (itemSelected.type == "lands" || itemSelected.type == "properties") {
                    // show the text box of space
                    binding.spaceEditText.visibility = View.VISIBLE
                } else {
                    // hide the text box of space
                    binding.spaceEditText.visibility = View.GONE
                }
                vm.onCatSelectedListener(itemSelected, true)
                dialogFragment.dismiss()
            }
        })


        dialogFragment.show(supportFragmentManager, CategoriesDialogFragment::class.java.getSimpleName())
    }

    private fun restAllEditText() {

        vm.selectSubCatText.set(null)
        vm.selectedTypeText.set(null)
        vm.selectRegionText.set(null)
        vm.selectedCityAdLocationText.set(null)
        vm.selectPropertiesText.set(null)
        vm.etspace.set(null)
        vm.selectCarText.set(null)
        vm.etKm.set(null)
        vm.etDesc.set(null)
    }

    private fun setupCarMotionVectorAttribute() {

        carStepDialog = Dialog(this)
        val b = DataBindingUtil.inflate<CatCarAttrBinding>(layoutInflater, R.layout.cat_car_attr, null, false)
        b.setVariable(BR.vm, vm)
        b.executePendingBindings()
        b.tvTitle.text = getString(R.string.motion_engine_km)
        b.btnBack.setVisible(false)
        carStepDialog?.setCancelable(false)
        carStepDialog?.setContentView(b.root)

        carStepDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)



        b.btnBack.setOnClickListener {
            when (vm.currentStep.get()) {
                1 -> setMotionVectorStep(b)
                2 -> setupEngineStep(b)

                else -> { // Note the block
                    print("x is neither 1 nor 2")
                }
            }
        }

        setMotionVectorStep(b)
    }

    private fun setupCarEngineAndColorAndFuel() {

        carEngineColorFuelDialog = Dialog(this)
        val b = DataBindingUtil.inflate<CatCarAttrBinding>(layoutInflater, R.layout.cat_car_attr, null, false)
        b.setVariable(BR.vm, vm)
        b.executePendingBindings()
        b.tvTitle.text = getString(R.string.engine_color_fuel)
        b.btnBack.setVisible(false)
        carEngineColorFuelDialog?.setCancelable(false)
        carEngineColorFuelDialog?.setContentView(b.root)

        carEngineColorFuelDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)



        b.btnBack.setOnClickListener {
            when (vm.currentStep.get()) {
                1 -> setEngineSystemStep(b)
                2 -> setupFuelTypeStep(b)

                else -> { // Note the block
                    print("x is neither 1 nor 2")
                }
            }
        }

        setEngineSystemStep(b)
    }

    private fun setEngineSystemStep(b: CatCarAttrBinding) {

        b.btnBack.setVisible(false)
        val list = EngineDriveSystem.getList()
        vm.currentStep.set(0)

        vm.tvTitle.set(getString(R.string.engine_drive_system))
        vm.selectCarEngineAndColorAndFuelText.set("")

        b.rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        b.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { bind, model, postion, adapter ->
            bind.root.setOnClickListener {
                vm.selectCarEngineAndColorAndFuelText.set(model.title)
                model.isChecked = true
                vm.onSelectedCarEngineSystemListener(model)
                setupFuelTypeStep(b)
            }
        }
    }

    private fun setupFuelTypeStep(bind: CatCarAttrBinding) {

        bind.btnBack.setVisible(true)

        val list = FuelType.getList()

        vm.tvTitle.set(getString(R.string.fuel_type))
        vm.currentStep.set(1)


        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { bindItem, model, postion, adapter ->
            bindItem.root.setOnClickListener {
                val oldText = vm.selectCarEngineAndColorAndFuelText.get() ?: ""
                vm.selectCarEngineAndColorAndFuelText.set(oldText + "  ,  " + model.title)
                model.isChecked = true
                vm.onSelectedFuelListener(model)
                setupCarColorStep(bind)

            }
        }

    }

    private fun setupCarColorStep(bind: CatCarAttrBinding) {

        bind.btnBack.setVisible(true)

        val list = CarColor.getList()


        vm.tvTitle.set(getString(R.string.car_color))
        vm.currentStep.set(2)


        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { bindItem, model, postion, adapter ->
            bindItem.root.setOnClickListener {
                val oldText = vm.selectCarEngineAndColorAndFuelText.get() ?: ""
                vm.selectCarEngineAndColorAndFuelText.set(oldText + "  ,  " + model.title)
                model.isChecked = true
                vm.onSelectedCarColorListener(model)
                carEngineColorFuelDialog?.dismiss()
            }
        }

    }

    private fun setMotionVectorStep(b: CatCarAttrBinding) {

        b.btnBack.setVisible(false)
        val list = MotionVector.getList()
        vm.currentStep.set(0)

        vm.tvTitle.set(getString(R.string.motion_vector))
        vm.selectCarMotionAndEnginAnKmText.set("")

        b.rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        b.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { bind, model, postion, adapter ->
            bind.root.setOnClickListener {
                vm.selectCarMotionAndEnginAnKmText.set(model.title)
                model.isChecked = true
                vm.onSelectedMotionListener(model)
                setupEngineStep(b)
            }
        }
    }

    private fun setupEngineStep(bind: CatCarAttrBinding) {

        bind.btnBack.setVisible(true)

        val engineSize = EngineSize.getList()


        vm.tvTitle.set(getString(R.string.engine_size))
        vm.currentStep.set(1)


        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, engineSize) { bindItem, model, postion, adapter ->
            bindItem.root.setOnClickListener {
                val oldText = vm.selectCarMotionAndEnginAnKmText.get() ?: ""
                vm.selectCarMotionAndEnginAnKmText.set(oldText + "  ,  " + model.title)
                model.isChecked = true
                vm.onSelectedEngineSizeListener(model)
                setupKmStep(bind)
            }
        }

    }

    private fun setupKmStep(bind: CatCarAttrBinding) {

        bind.btnBack.setVisible(true)

        val list = Kilometer.getList()

        vm.tvTitle.set(getString(R.string.km_hint))
        vm.currentStep.set(2)


        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { bind, model, postion, adapter ->
            bind.root.setOnClickListener {
                val oldText = vm.selectCarMotionAndEnginAnKmText.get() ?: ""
                vm.selectCarMotionAndEnginAnKmText.set(oldText + "  ,  " + model.title)
                model.isChecked = true
                vm.onSelectedKmListener(model)
                carStepDialog?.dismiss()

            }
        }

    }

    // cars
    private fun setupCarStepDialog() {

        carStepDialog = Dialog(this)
        val b = DataBindingUtil.inflate<CatCarFourAttrBinding>(layoutInflater, R.layout.cat_car_four_attr, null, false)
        b.setVariable(BR.vm, vm)
        b.executePendingBindings()
        carStepDialog?.setContentView(b.root)
        carStepDialog?.setCancelable(false)

        carStepDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        b.btnBack.setOnClickListener {
            when (vm.currentStep.get()) {
                1 -> setupYearStep(b)
                2 -> setupMakeStep(b)
                3 -> setupModelStep(b, vm.carModelRes.value)

                else -> { // Note the block
                    print("x is neither 1 nor 2")
                }
            }
        }

        setupYearStep(b)
    }

    private fun setupYearStep(dialogBinding: CatCarFourAttrBinding) {

        val calender = Calendar.getInstance()
        val currentYear = calender.get(Calendar.YEAR)
        val years = ArrayList<LocalStanderModel>()
        for (i in currentYear downTo 1800) {
            years.add(LocalStanderModel(i.toString(), LocalStanderModel.LocalizedModel(i.toString(), i.toString())))
        }

        vm.currentStep.set(0)
        vm.tvTitle.set(getString(R.string.car_year))
        vm.selectCarText.set("")
        dialogBinding.btnBack.setVisible(false)

        dialogBinding.rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        dialogBinding.rv.adapter = BaseAdapter<LocalStanderModel, ListItemBinding>(R.layout.list_item, vm, years) { bind, model, postion, adapter ->
            bind.root.setOnClickListener {
//                vm.selectCarText.set(model.title.localized)
                model.isChecked = true
                vm.onSelectedYearsListener(model)
                setupMakeStep(dialogBinding)
            }
        }

    }

    private fun setupMakeStep(bind: CatCarFourAttrBinding) {
        vm.tvTitle.set(getString(R.string.car_make))
        vm.currentStep.set(1)
        bind.btnBack.setVisible(true)
        vm.isLoading.postValue(true)
        vm.repository.getCarMake {

            vm.isLoading.postValue(false)
            bind.rv.adapter = BaseAdapter<StanderModel1, ListItem3Binding>(R.layout.list_item3, vm, it?.response) { b, m, i, a ->
                b.root.setOnClickListener {
                    vm.isLoading.postValue(true)
//                        val oldText = vm.selectCarText.get() ?: ""
//                        vm.selectCarText.set(oldText + "  ,  " + m.title)

                    vm.onMakeSelectedListener(m) { data ->
                        vm.isLoading.postValue(false)
                        setupModelStep(bind, data)

                    }
                }
            }
        }

    }

    private fun setupModelStep(bind: CatCarFourAttrBinding, data: List<StanderModel1>?) {
        vm.tvTitle.set(getString(R.string.car_model))
        vm.currentStep.set(2)
        bind.btnBack.setVisible(true)
        bind.rv.adapter = BaseAdapter<StanderModel1, ListItem3Binding>(R.layout.list_item3, vm, data) { b, m, i, a ->
            b.root.setOnClickListener {

                vm.onModelSelectedListener(m) { data ->
                    vm.isLoading.postValue(false)
                    setupSubModelStep(bind, data)
                }

//                    vm.onModelSelectedListener(m)
//                    vm.selectCarText.set(vm.selectedYear?.title?.localized + "  ,  " + vm.selectedCarMake?.title + "  ,  " + vm.selectedModelLocal?.title)
//                    carStepDialog?.dismiss()
            }
        }
    }

    private fun setupSubModelStep(bind: CatCarFourAttrBinding, data: List<StanderModel1>) {
        vm.tvTitle.set(getString(R.string.car_sub_model))
        vm.currentStep.set(3)
        bind.btnBack.setVisible(true)
        bind.rv.adapter = BaseAdapter<StanderModel1, ListItem3Binding>(R.layout.list_item3, vm, data) { b, m, i, a ->
            b.root.setOnClickListener {
//                    showToast(m.title)

                vm.onSubModelSelectedListener(m)
                vm.selectCarText.set(vm.selectedYear?.title?.localized + "  ,  " + vm.selectedCarMake?.localized + "  ,  " + vm.selectedModelLocal?.localized + "  ,  " + vm.selectedSubModelLocal?.localized)
                carStepDialog?.dismiss()

            }
        }
    }

    //aqar
    private fun setupAqarStepDialog() {

        aqarStepDialog = Dialog(this)
        val b = DataBindingUtil.inflate<AqarAttrBinding>(layoutInflater, R.layout.aqar_attr, null, false)
        b.setVariable(BR.vm, vm)
        b.executePendingBindings()
        aqarStepDialog?.setContentView(b.root)

        aqarStepDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

//        vm.currentStep.set(0)
//        vm.tvTitle.set(getString(R.string.region))

        b.tvTitle.setText(R.string.region_number_of_rooms_number_of_bathrooms)
        b.rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        setupRoomsStep(b)

//        vm.isLoading.postValue(true)
//        vm.repository.getRegion {
//
//            vm.isLoading.postValue(false)
//            b.rv.adapter = BaseAdapter<LocalStanderModel, ListItemBinding>(
//                R.layout.list_item,
//                vm,
//                it?.response
//            ) { bind, model, postion, adapter ->
//                bind.root.setOnClickListener {
//                    vm.selectPropertiesText.set(model.title.localized)
//                    vm.onRegionSelectedListener(model)
//                    setupRoomsStep(b)
//                }
//            }
//
//        }
    }

    private fun setupRoomsStep(bind: AqarAttrBinding) {
        vm.currentStep.set(0)
        vm.tvTitle.set(getString(R.string.rooms_numbers))

        val list = RoomSize.getList()

        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { b, m, i, a ->
            b.root.setOnClickListener {
                val oldText = vm.selectPropertiesText.get() ?: ""
                vm.selectPropertiesText.set(m.title)
                vm.onRoomSelectedListener(m)
                setupBathRoomsStep(bind)
            }
        }

    }

    private fun setupBathRoomsStep(bind: AqarAttrBinding) {
        vm.tvTitle.set(getString(R.string.bath_room_number))
        vm.currentStep.set(1)

        val list = RoomSize.getList()


        bind.rv.adapter = BaseAdapter<StanderModel, ListItem2Binding>(R.layout.list_item2, vm, list) { b, m, i, a ->
            b.root.setOnClickListener {
                val oldText = vm.selectPropertiesText.get() ?: ""
                vm.selectPropertiesText.set(oldText + "  ,  " + m.title)
                vm.onBathRoomSelectedListener(m)
                aqarStepDialog?.dismiss()
            }
        }
    }

    private fun editAd() {
        val ad = vm.ad ?: return

        val listOfVideoAndImages = arrayListOf<NewImage>()
        ad.video?.let {
            listOfVideoAndImages.add(NewImage(it, null, false))
        }

        val images = ArrayList(ad.photos?.map { NewImage(it, null, true) } ?: arrayListOf())
        listOfVideoAndImages.addAll(images)
        imagesAdapter.images = listOfVideoAndImages
        imagesDidChange()

        vm.onCatSelectedListener(ad.category, false)
        vm.onSubCatSelectedListener(ad.subcategory)
        vm.onSelectedTypeListener(ad.type)
        vm.onRegionSelectedListener(ad.region)

        val roomSize = RoomSize.getObjectByConstant(ad.numberOfRooms.toString())
        if (roomSize != null) {
            vm.onRoomSelectedListener(roomSize)
            vm.selectedRooms = roomSize
        }

        val bathRoomSize = RoomSize.getObjectByConstant(ad.numberOfBathroom.toString())
        if (bathRoomSize != null) {
            vm.onBathRoomSelectedListener(bathRoomSize)
            vm.selectedBathRoom = bathRoomSize

        }

        if (roomSize != null && bathRoomSize != null) vm.selectPropertiesText.set(roomSize.title + "  ,  " + bathRoomSize.title)


        ad.location?.let {
            vm.lat = it.coordinates[1]
            vm.lng = it.coordinates[0]
            vm.address.set(it.address)
        }





        if (ad.carYear != null && ad.carMake != null && ad.carModel != null && ad.carSubModel != null) {

            vm.onMakeSelectedListener(ad.carMake) {}
            vm.onModelSelectedListener(ad.carModel)
            vm.onSubModelSelectedListener(ad.carSubModel)
            vm.onSelectedYearsListener(LocalStanderModel(ad.carYear ?: "", LocalStanderModel.LocalizedModel(ad.carYear ?: "", ad.carYear ?: "")))


            vm.selectCarText.set(ad.carYear + "  ,  " + ad.carMake.localized + "  ,  " + ad.carModel.localized + "  ,  " + ad.carSubModel.localized)

        }

        if (bathRoomSize != null) {
            vm.onBathRoomSelectedListener(bathRoomSize)
            vm.selectedBathRoom = bathRoomSize
        }

        val engineDriveSystem = EngineDriveSystem.getObjectByConstant(ad.pushFatisType.toString())
        val fuelType = FuelType.getObjectByConstant(ad.fuelType.toString())
        val carColor = CarColor.getObjectByConstant(ad.color.toString())


        if (fuelType != null && engineDriveSystem != null && carColor != null) {
            vm.onSelectedFuelListener(fuelType)
            vm.onSelectedCarEngineSystemListener(engineDriveSystem)
            vm.onSelectedCarColorListener(carColor)

            vm.selectCarMotionAndEnginAnKmText.set(engineDriveSystem.title + "  ,  " + fuelType.title + "  ,  " + carColor.title)
        }

        val km = Kilometer.getObjectByConstant(ad.km.toString())
        val engineSize = EngineSize.getObjectByConstant(ad.engineCapacity.toString())
        val motionVector = MotionVector.getObjectByConstant(ad.gearBox.toString())



        if (km != null && engineSize != null && motionVector != null) {
            vm.onSelectedKmListener(km)
            vm.onSelectedEngineSizeListener(engineSize)
            vm.onSelectedMotionListener(motionVector)

            vm.selectCarMotionAndEnginAnKmText.set(motionVector.title + "  ,  " + engineSize.title + "  ,  " + km.title)
        }







        vm.etDesc.set(ad.details)
        vm.etTitle.set(ad.title)
        vm.etPrice.set(ad.price.toString())
        // TODO edit ads
//        vm.onSelectedKmListener(
//            LocalStanderModel(
//                ad.km ?: "",
//                LocalStanderModel.LocalizedModel(ad.carYear ?: "", ad.carYear ?: "")
//            )
//        )

        vm.etKm.set(ad.km)
    }

    private fun pickImages() {
        proceedPickImages()
    }

    private fun pickImagesFromCamera() {
        requestPermissions(object : RequestListener<Boolean> {
            override fun onResult(result: Boolean) {
                if (result) onPickImageFromCamera()
            }
        })
    }

    fun requestPermissions(listener: RequestListener<Boolean>) {
        val permissions: Array<String>
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        permissionLauncher.launch(permissions) { result ->

            var isAllGranted = true
            for (item: MutableMap.MutableEntry<String, Boolean> in result.entries) {
                if (!item.value) {
                    isAllGranted = false
                    break
                }
            }
            if (isAllGranted) {
                listener.onResult(true)
            } else {
                listener.onResult(false)
                Helper.showSettingsDialog(this@NewAdFragment, getString(R.string.storage))
            }
        }
    }

    private fun proceedPickImages() {
//        onPickImage()
//        pickImageUsingLibrary()
//        openPickerDialog()

        requestPermissions(object : RequestListener<Boolean> {
            override fun onResult(result: Boolean) {
                if (result) openPickerDialog()
            }
        })
    }

    private fun openPickerDialog() {
        val list: java.util.ArrayList<DialogItem> = java.util.ArrayList<DialogItem>()
        list.add(DialogItem(1, getString(R.string.choose_image_from_gallery)))
        list.add(DialogItem(2, getString(R.string.choose_video_from_gallery)))
        list.add(DialogItem(4, getString(R.string.pic_video)))
        list.add(DialogItem(3, getString(R.string.pic_image)))
        list.add(DialogItem(5, getString(R.string.choose_custom_photo)))
        val dialog: ItemsDialogFragment = ItemsDialogFragment.newInstance(list)
        supportFragmentManager.beginTransaction().add(dialog, "DialogMessage").commitAllowingStateLoss()
        dialog.setListener { dialogItem ->
            if (dialogItem.getId() == 1) {
                chooseImageFromGallery()
            } else if (dialogItem.getId() == 2) {
                if (!checkIfAddedVideo()) chooseVideoFromGallery()
            } else if (dialogItem.getId() == 3) {
                picImageCamera()
            } else if (dialogItem.getId() == 4) {
                if (!checkIfAddedVideo()) recordVideoCamera()
            } else if (dialogItem.getId() == 5) {
                activityLauncher.launch(CustomPhotosActivity.newInstance(this)) {
                    if (it.resultCode == RESULT_OK) {
                        it.data?.let { intent ->
                            val path = intent.extras?.getString("path")
                            if (path != null) {
                                didPickImages(listOf(path))
                            }
                        }
                    }
                }
            }
        }
    }

    fun chooseImageFromGallery() {
        FishBun.with(this).setImageAdapter(GlideAdapter()).setIsUseDetailView(false).setMaxCount(15).setMinCount(1).setPickerSpanCount(4).setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#000000"), false).setActionBarTitleColor(Color.parseColor("#000000"))

            .setAlbumSpanCount(1, 8).setButtonInAlbumActivity(true).setCamera(false).setReachLimitAutomaticClose(true)

//            .setAllDoneButtonDrawable(R.drawable.ic_add)
            .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_24dp))
//            .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_back_white))
//            .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
            .setAllViewTitle(getString(R.string.all)).setActionBarTitle(getString(R.string.choose_gallery)).textOnImagesSelectionLimitReached(getString(R.string.limit_reached)).textOnNothingSelected(getString(R.string.nothing_selected)).setSelectCircleStrokeColor(Color.BLACK).setIsUseAllDoneButton(false).isStartInAllView(false).exceptMimeType(listOf(MimeType.GIF)).startAlbum()

    }

    fun chooseVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        activityLauncher.launch(intent) { result ->
            if (result != null && result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    val videoUri = intent.data
                    if (videoUri == null) {
                        return@let
                    }
                    optimiozeVideo(videoUri)
                }
            }

        }
    }

    private fun optimiozeVideo(videoUri: Uri) {
        val mimeType: String = contentResolver.getType(videoUri).toString()
        val file = saveVideoToAppScopeStorage(this, videoUri, mimeType)
        file?.let {
            hud?.show()
            Transcoder.into(file.absolutePath).addDataSource(context, videoUri)
                .setListener(object : TranscoderListener {
                    override fun onTranscodeProgress(progress: Double) {
                        hud?.setProgress((progress * 100).toInt())

                    }

                    override fun onTranscodeCompleted(successCode: Int) {
                        hud?.dismiss()
                        didPickImages1(listOf(NewImage(null, it.path, false)))
                    }

                    override fun onTranscodeCanceled() {
                        hud?.dismiss()
                    }

                    override fun onTranscodeFailed(exception: Throwable) {
                        hud?.dismiss()
                        didPickImages1(listOf(NewImage(null, it.path, false)))
                    }

                }).transcode()
        }
    }

    fun picImageCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            filePathImageCamera = MyFileUtils.getNewImageFile(this)
            val photoURI: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                photoURI = FileProvider.getUriForFile(this, "qa.topsale.provider", filePathImageCamera)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }
            activityLauncher.launch(takePictureIntent) { result ->
                if (result != null && result.resultCode == RESULT_OK) {
                    val data = result.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val photos: MutableList<String> = java.util.ArrayList()
                        photos.add(filePathImageCamera.getAbsolutePath())
                        imageCompress.compressImages(true, photos, this)
                    } else {
                        val photos: MutableList<String?> = java.util.ArrayList()
                        if (data != null) {
                            if (data.dataString != null) {
                                photos.add(data.dataString)
                                imageCompress.compressImages(true, photos, this)
                            } else {
                                val bundle = data.extras
                                val photo = bundle!!["data"] as Bitmap?
                                val tempUri: Uri = imageCompress.getImageUri(photo)
                                val imagePath: String = imageCompress.getRealPathFromURI(tempUri)
                                if (imagePath != null) {
                                    photos.add(imagePath)
                                    imageCompress.compressImages(true, photos, this)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun recordVideoCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        try {
            activityLauncher.launch(takePictureIntent) {
                if (it.resultCode == RESULT_OK) {
                    it.data?.let { intent ->
                        val videoUri = intent.data
                        if (videoUri == null) {
                            return@let
                        }

                        optimiozeVideo(videoUri)

                        //Save file to upload on server
//                        val file = saveVideoToAppScopeStorage(this, videoUri, mimeType)

                    }
                }

            }
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    fun getVideoThumbnail(context: Context, videoUri: Uri, imageWidth: Int, imageHeight: Int): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT < 27) {
            val picturePath = getPath(context, videoUri)
            Log.i("TAG", "picturePath $picturePath")
            if (picturePath == null) {
                val mMMR = MediaMetadataRetriever()
                mMMR.setDataSource(context, videoUri)
                bitmap = mMMR.frameAtTime
            } else {
                bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Images.Thumbnails.MINI_KIND)
            }
        } else {
            val mMMR = MediaMetadataRetriever()
            mMMR.setDataSource(context, videoUri)
            bitmap = mMMR.getScaledFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC, imageWidth, imageHeight)
        }

        return bitmap
    }

    fun saveVideoToAppScopeStorage(context: Context, videoUri: Uri?, mimeType: String?): File? {
        if (videoUri == null || mimeType == null) {
            return null
        }

        val fileName = "capturedVideo.${mimeType.substring(mimeType.indexOf("/") + 1)}"

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), fileName)
        file.deleteOnExit()
        file.createNewFile()
        return file
    }

    override fun onError(error: String) {

    }

    val REQUEST_IMAGE_CAPTURE_VIDEO = 100
    private fun dispatchTakeVideoIntent() {

    }

    override fun onSuccessMainImageCompress(photos: MutableList<String>) {
        val filePath = photos[0]
        didPickImages(listOf(filePath))
    }

    override fun onSuccessImageCompress(photos: MutableList<String>) {
    }

    fun onPickImage() {
//        val chooseImageIntent: Intent? = ImagePicker.getPickImageIntent(this,getString(R.string.select_photos))
//        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        getResult.launch(intent)


    }

    // Receiver
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {

            val items = arrayListOf<String>()

            if (it.data?.clipData != null) {
                val clipData = it.data?.clipData ?: return@registerForActivityResult
                val count = clipData.itemCount

                // check clipData.itemCount
                if ((imagesAdapter.images.size + count) > imagesAdapter.MAX_IMAGES) {
                    vm.showToast.postValue(getString(R.string.max_images_alert))
                    return@registerForActivityResult
                }

                for (i in 0 until count) {
                    clipData.getItemAt(i).uri?.let {
                        Log.i("NewAdFragment", it.toString())

                        MyFileUtils.getPath(this, it).let { path ->
                            items.add(path)
                        }
                    }
                }

                if (items.isNotEmpty())

                    didPickImages(items)

            } else if (it.data?.data != null) {
                it.data?.data?.let {
                    MyFileUtils.getPath(this, it).let { path ->
                        items.add(path)
                    }

                    if (items.isNotEmpty()) didPickImages(items)
                }
            }


        }
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    private lateinit var filePhoto: File
    fun onPickImageFromCamera() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filePhoto = getPhotoFile(FILE_NAME)
        val providerFile =

            FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", filePhoto)

        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        startActivityForResult(takePhotoIntent, CAMERA_IMAGE_ID)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Localization.setLanguage(this, Helper.languageCode)

        if (resultCode != RESULT_OK) return

        if (requestCode == CAMERA_IMAGE_ID) {
            val path = filePhoto.absolutePath
            path.let {
                didPickImages(listOf(it))
            }
        }


        if (requestCode == SELECT_PICTURES) {
            if (resultCode == Activity.RESULT_OK) {
                if (data?.clipData != null) {
                    val clipData = data.clipData ?: return
                    val count = clipData.itemCount
                    val items = arrayListOf<String>()

                    // check clipData.itemCount
                    if ((imagesAdapter.images.size + count) > imagesAdapter.MAX_IMAGES) {
                        vm.showToast.postValue(getString(R.string.max_images_alert))
                        return
                    }

                    for (i in 0 until count) {
                        clipData.getItemAt(i).uri?.let {
                            Log.i("NewAdFragment", it.toString())

                            URIPathHelper().getPath(this, it)?.let { path -> items.add(path) }
                        }
                    }

                    if (items.isNotEmpty())

                        didPickImages(items)

                } else if (data?.data != null) {
                    data.data?.let {
                        Helper.getImagePath(it, this)?.let { path ->
                            didPickImages(arrayListOf(path))
                        }
                    }
                }
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {

            vm.lat = data?.getDoubleExtra("lat", 0.0)
            vm.lng = data?.getDoubleExtra("lng", 0.0)
            vm.address.set(data?.getStringExtra("address"))
        }

        if (resultCode == Activity.RESULT_OK && requestCode == RC_IMAGE_PICKER && data != null) {
            val imagesPath = arrayListOf<String>()
            imagesPath.clear()

            val result = ImagePicker.getImages(data)
            if (result!!.isNotEmpty()) {
                for (i in 0 until result.size) {
                    val path = result.get(i).path
                    MyFileUtils.getPath(this, Uri.parse(path)).let {
                        imagesPath.add(it)
                    }
                }
            }

            if (imagesPath.isNotEmpty()) didPickImages(imagesPath)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == FISHBUN_REQUEST_CODE && data != null) {
            val imagesPath = arrayListOf<String>()
            imagesPath.clear()

            val result = data.getParcelableArrayListExtra<Uri>(INTENT_PATH)


            if (result!!.isNotEmpty()) {
                for (i in 0 until result.size) {
                    val path = result.get(i)

                    //                onSuccessMainImageCompress(photos);
//                    imageCompress.compressImages(true, photos, this)
                    MyFileUtils.getPath(this, path).let {
                        Log.e("test_1", it)
                        imagesPath.add(it)

                    }
                    MyFileUtils.compressImage(this, path).let {
                        Log.e("test_2", it)

                    }
                }
            }

            if (imagesPath.isNotEmpty()) didPickImages(imagesPath)

        }

        if (requestCode == 12345 && data != null) {
            val path = data.extras?.getString("path")
            if (path != null) {
                val imgName = UUID.randomUUID().toString()
//                val path =saveImageToGallery(path, imgName).toString()
                didPickImages(listOf(path))
            }


        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun didPickImages(images: List<String>) {
        val newImages = images.map { NewImage(null, it, true) }
        imagesAdapter.images.addAll(newImages)
        imagesDidChange()

    }

    private fun didPickImages1(images: List<NewImage>) {
        imagesAdapter.images.addAll(images)
        imagesDidChange()
    }

    private fun imagesDidChange() {
        imagesAdapter.notifyDataSetChanged()
        if (imagesAdapter.images.isEmpty()) {
            binding.defaultImageView.setVisible(false)
            binding.tvImageClick.setVisible(false)
            binding.defaultImageView.setImageResource(R.mipmap.logo)
        } else {
            val image = imagesAdapter.images[imagesAdapter.defaultImageIndex]
            binding.defaultImageView.setVisible(true)
            binding.tvImageClick.setVisible(true)
            if (image.bitmap != null) {
                binding.defaultImageView.setImageBitmap(image.bitmap)
            } else {
                Picasso.get().load(image.photo?.thumb).placeholder(R.mipmap.logo).into(binding.defaultImageView)
            }
        }
    }

    private fun didTapImage(position: Int, v: View) {
        val image = imagesAdapter.images.getOrNull(position) ?: return
        val popupMenu = PopupMenu(this, v)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.ad_details_menu, menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.make_default -> {
                    imagesAdapter.defaultImageIndex = position
                    imagesDidChange()
                }

                R.id.delete -> {
                    image.photo?.let {
                        it._id?.let { it1 -> deletedPhotos.add(it1) }
                    }
                    if (imagesAdapter.defaultImageIndex == position) {
                        if (imagesAdapter.defaultImageIndex > 0) {
                            imagesAdapter.defaultImageIndex -= 1
                        }
                    } else if (imagesAdapter.defaultImageIndex > position) {
                        imagesAdapter.defaultImageIndex -= 1
                    }
                    imagesAdapter.images.removeAt(position)
                    imagesDidChange()
                }

                R.id.show_image -> {
                    val list: ArrayList<ImageSlider> = ArrayList()

                    if (image.localPath != null) {
                        Log.e("test_image", imagesAdapter.images.get(position).localPath + "")
                        val item = imagesAdapter.images.get(position)
                        list.add(ImageSlider(item.localPath, item.localPath, item.localPath, item.isImage))
                    } else {
                        Log.e("test_image", imagesAdapter.images.get(position).photo?.orgUrl + "")

                        list.add(ImageSlider(imagesAdapter.images.get(position).photo?.thumbUrl, imagesAdapter.images.get(position).photo?.mediumThumbUrl, imagesAdapter.images.get(position).photo?.orgUrl, imagesAdapter.images.get(position).isImage))
                    }
                    startActivity(ImageFullScreenActivity.newInstance(this, list, 0))
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()

//        val dialog = AlertDialog.Builder(context)
//            .setView(R.layout.dialog_image_actions)
//            .create()
//
//        dialog.show()
//
//        dialog.makeDefaultButton.setOnClickListener {
//            imagesAdapter.defaultImageIndex = position
//            imagesDidChange()
//            dialog.dismiss()
//        }
//
//        dialog.deleteButton.setOnClickListener {
//            image.photo?.let {
//                deletedPhotos.add(it._id)
//            }
//            if (imagesAdapter.defaultImageIndex == position) {
//                if (imagesAdapter.defaultImageIndex > 0) {
//                    imagesAdapter.defaultImageIndex -= 1
//                }
//            } else if (imagesAdapter.defaultImageIndex > position) {
//                imagesAdapter.defaultImageIndex -= 1
//            }
//            imagesAdapter.images.removeAt(position)
//            imagesDidChange()
//            dialog.dismiss()
//        }
    }

    private fun validateFirstStep(): Boolean {
        binding.edTitle.setError("")

        if (imagesAdapter.images.isEmpty()) {
            snackBar(R.string.select_photos, R.string.select) {
                openPickerDialog()
            }
            return false
        }

        if (TextUtils.isEmpty(binding.edTitle.text)) {
            binding.edTitle.setError(getString(R.string.enter_ad_name))
//            vm.showToast.postValue(R.string.enter_ad_name)
            return false
        }

        vm.etTitle.set(binding.edTitle.text)
//        if (withoutPhotos) {
//            currentStep = 2
//            setCurrentUi()
//        }
        return true

    }

    private fun validateSecondStep(): Boolean {
        binding.categoryButton.setError("")
//        binding.ilCategoryButton.isErrorEnabled = false
        binding.categoryButton.setError("")
        binding.subcategoryButton.setError("")
        binding.typeButton.setError("")
        binding.carAttrButton.setError("")
        binding.edKm.setError("")
        binding.propertiesButton.setError("")

        binding.edEngineDriveFuelColor.setError("")



        if (vm.selectedCat == null) {
            binding.categoryButton.setError(getString(R.string.select_category))
//            snackBar(R.string.select_category, R.string.select) { openCategoriesDialog() }
            return false

        }

        if (vm.selectedCat?.subcategories?.isNotEmpty() == true && vm.selectedSubCat == null) {
            binding.subcategoryButton.setError(getString(R.string.select_sub_category))
//            snackBar(R.string.select_sub_category, R.string.select) { subCatDialog?.show() }
            return false

        }

        if (vm.selectedCat?.types?.isNotEmpty() == true && vm.selectedType == null) {
            binding.typeButton.setError(getString(R.string.select_type))
//            snackBar(R.string.select_type, R.string.select) { typeDialog?.show() }
            return false

        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedYear == null) {
            binding.carAttrButton.setError(getString(R.string.select_year))
//            snackBar(R.string.select_year, R.string.select) { carStepDialog?.show() }
            return false

        }


        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedCarMake == null) {
            binding.carAttrButton.setError(getString(R.string.select_make))
//            snackBar(R.string.select_make, R.string.select) { setupCarStepDialog()
//                carStepDialog?.show()
//            }
            return false

        }


        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedModelLocal == null) {
            binding.carAttrButton.setError(getString(R.string.select_model))
//            snackBar(R.string.select_model, R.string.select) { carStepDialog?.show() }
            return false

        }

//        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.etKm.get()?.isEmpty() == true) {
//            binding.edKm.setError(getString(R.string.km_error))
////            vm.showToast.postValue(R.string.km_error)
//            return false
//        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedMotion == null) {
            binding.edKm.setError(getString(R.string.motion_vector_error))
            return false
        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedEngine == null) {
            binding.edKm.setError(getString(R.string.engine_size_error))
            return false
        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedKm == null) {
            binding.edKm.setError(getString(R.string.km_error))
            return false
        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedEngineSystem == null) {
            binding.edEngineDriveFuelColor.setError(getString(R.string.engine_drive_system_error))
            return false
        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedFuelType == null) {
            binding.edEngineDriveFuelColor.setError(getString(R.string.fuel_type_error))
            return false
        }

        if (vm.selectedCat?.categoryClass == CategoryClass.CARS && vm.selectedColor == null) {
            binding.edEngineDriveFuelColor.setError(getString(R.string.car_color_error))
            return false
        }

//        if (vm.selectedCat?.categoryClass == CategoryClass.LANDS && vm.selectedRegion == null) {
//            snackBar(R.string.select_region, R.string.select) { aqarStepDialog?.show() }
//            return false
//
//        }

//        if (vm.selectedCat?.categoryClass == CategoryClass.LANDS && vm.etspace.get()
//                ?.isEmpty() == true
//        ) {
//            vm.showToast.postValue(R.string.area_error)
//            return false
//        }

        if (vm.selectedCat?.categoryClass == CategoryClass.PROPERTIES && vm.selectedRooms == null) {
            binding.propertiesButton.setError(getString(R.string.room_error))
//            vm.showToast.postValue(R.string.room_error)
            return false
        }

        if (vm.selectedCat?.categoryClass == CategoryClass.PROPERTIES && vm.selectedBathRoom == null) {
            binding.propertiesButton.setError(getString(R.string.bath_room_error))
//            vm.showToast.postValue(R.string.bath_room_error)
            return false
        }

        return true
    }

    private fun validateThirdStep(): Boolean {
        binding.edPrice.setError("")


        if (ValidationUtils.isEmptyEditText(binding.edPrice.editText)) {
            binding.edPrice.setError(getString(R.string.enter_price))
//            vm.showToast.postValue(R.string.enter_price)
            return false
        }

        if (binding.edPrice.text.toDouble() == 0.0) {
            binding.edPrice.setError(getString(R.string.ads_price_error))
//            vm.showToast.postValue(R.string.ads_price_error)
            return false
        }

        vm.etPrice.set(binding.edPrice.text)

        if (vm.selectedCat?.isLocationRequired != null && vm.selectedCat?.isLocationRequired!!) {
            if (vm.lat == 0.0 || vm.lng == 0.0) {
                binding.locationButton.setError(getString(R.string.select_ad_location))
//            vm.showToast.postValue(R.string.select_ad_location)
                return false
            }
        }



        return true
    }

    private fun publish(withoutPhotos: Boolean = false) {
        var videoPath: File? = null
//        val images = ArrayList(imagesAdapter.images.mapNotNull {
//            it.resizedLocalFile
//        })
        val images = arrayListOf<File>()
        imagesAdapter.images.forEach {
            if (it.isImage) {
                it.resizedLocalFile?.let { it1 -> images.add(it1) }
            } else {
                videoPath = it.resizedLocalFile
            }
        }

        val thumbnailIndex = imagesAdapter.defaultImageIndex
        if (thumbnailIndex > 0 && thumbnailIndex < images.size) {
            try {
                val thumbnail = images[thumbnailIndex]
                images.removeAt(thumbnailIndex)
                images.add(0, thumbnail)
            } catch (ex: IndexOutOfBoundsException) {

            }
        }




        vm.ad?.let {
            editBtn()
        } ?: vm.onAddNewClickedListener(binding.root, images, videoPath, binding.detailsTextView, binding.toggleAllowComments.isChecked,!imagesAdapter.images[imagesAdapter.defaultImageIndex].isImage)
    }

    private fun editBtn() {
        val newImages = ArrayList(imagesAdapter.images.mapNotNull { it.resizedLocalFile })
        val thumbnailIndex = imagesAdapter.defaultImageIndex
        val thumbnailImage = imagesAdapter.images.elementAtOrNull(thumbnailIndex)
        var thumbnailType: String? = null
        var thumbnailId: String? = null
        thumbnailImage?.resizedLocalFile?.let {
            thumbnailType = "new"
            newImages.remove(it)
            newImages.add(0, it)
        }
        thumbnailImage?.photo?.let {
            thumbnailType = "old"
            thumbnailId = it._id
        }

        vm.onEditAdClickedListener(this, newImages, deletedPhotos, thumbnailType, thumbnailId, binding.detailsTextView, binding.toggleAllowComments.isChecked,!imagesAdapter.images[imagesAdapter.defaultImageIndex].isImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (currentLocation != null) {
            lifecycle.removeObserver(currentLocation!!)
        }
    }

    companion object {
        private const val FILE_NAME = "photo.jpg"
        const val SELECT_PICTURES = 1001
        const val PICK_IMAGE_ID = 234
        const val CAMERA_IMAGE_ID = 235
        public const val PLACE_PICKER_REQUEST = 5434
        const val ARG_AD = "AD"

        fun newAd(context: Context): Intent {
            return Intent(context, NewAdFragment::class.java)
        }

        fun editAd(context: Context, ad: Ad): Intent {
            return Intent(context, NewAdFragment::class.java).putExtra(ARG_AD, ad)
        }
    }

    override fun onBackPressed() {
        if (currentStep == 1) {
            super.onBackPressed()
        } else if (currentStep == 2) {
            currentStep = 1
            setCurrentUi()
        } else if (currentStep == 3) {
            currentStep = 2
            setCurrentUi()
        }
    }


}

fun saveImageToGallery(bitmap: Bitmap, imgName: String): Uri? {
    val fos: OutputStream
    try {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver: ContentResolver = context.getContentResolver()
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + imgName + ".jpeg")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "topsale/ads")
        val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = Objects.requireNonNull(imageUri)?.let { resolver.openOutputStream(it) }!!
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        Objects.requireNonNull(fos)
        return imageUri
//        } else {
//
//            // Save image to gallery
//            val savedImageURL = MediaStore.Images.Media.insertImage(
//                context.getContentResolver(),
//                bitmap,
//                "Image_" + imgName + ".jpg",
//                "Image of bird"
//            )
//
//            // Parse the gallery image url to uri
//            val savedImageURI: Uri = Uri.parse(savedImageURL)
//            return savedImageURI
//
//        }
    } catch (e: Exception) {
    }
    return null
}

class NewImage(var photo: Photo?, var localPath: String?, var isImage: Boolean) {
    var bitmap: Bitmap? = null
    var resizedLocalFile: File? = null

    init {
        if (isImage) {
            localPath?.let {
                Log.i("NewAdFragment", it)
//            val image = File(it)
//            val originalBitmap = BitmapFactory.decodeFile(image.absolutePath)

                val originalBitmap = rotateImageIfRequired(it)
                bitmap = originalBitmap
                val imgName = UUID.randomUUID().toString()
                //saveImageToGallery(originalBitmap, imgName).toString()
                val downloadedFile = File(Environment.getExternalStorageDirectory().path, Environment.DIRECTORY_PICTURES + File.separator + "topsale/ads" + "/Image_" + imgName + ".jpeg")

//            this.resizedLocalFile = File("")
//            this.resizedLocalFile = downloadedFile
                this.resizedLocalFile = File(localPath)
            }
        } else {
            localPath?.let {
                bitmap = getVideoThumbnail(context, Uri.parse(it), 500, 500)
                this.resizedLocalFile = File(it)
            }

        }

    }

    fun getVideoThumbnail(context: Context, videoUri: Uri, imageWidth: Int, imageHeight: Int): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT < 27) {
            val picturePath = getPath(context, videoUri)
            Log.i("TAG", "picturePath $picturePath")
            if (picturePath == null) {
                val mMMR = MediaMetadataRetriever()
                mMMR.setDataSource(context, videoUri)
                bitmap = mMMR.frameAtTime
            } else {
                bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Images.Thumbnails.MINI_KIND)
            }
        } else {
            val mMMR = MediaMetadataRetriever()
            mMMR.setDataSource(context, videoUri)
            bitmap = mMMR.getScaledFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC, imageWidth, imageHeight)
        }

        return bitmap
    }

    private fun rotateImageIfRequired(path: String): Bitmap {
        val ei = ExifInterface(path)
        val img = BitmapFactory.decodeFile(path)

        val orientation: Int = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val result = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270f)
            else -> img
        }

        return result ?: img
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }
}

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    this.parentFile?.mkdirs() // create folder(s) if not exist
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }


}