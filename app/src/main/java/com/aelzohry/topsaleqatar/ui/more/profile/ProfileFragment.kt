package com.aelzohry.topsaleqatar.ui.more.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.FragmentProfileBinding
import com.aelzohry.topsaleqatar.helper.Helper
import com.aelzohry.topsaleqatar.helper.showProgress
import com.aelzohry.topsaleqatar.model.User
import com.aelzohry.topsaleqatar.repository.Repository
import com.aelzohry.topsaleqatar.repository.remote.RemoteRepository
import com.aelzohry.topsaleqatar.repository.remote.responses.ProfileResponse
import com.aelzohry.topsaleqatar.ui.new_ad.NewAdFragment
import com.aelzohry.topsaleqatar.ui.new_ad.adLocation.AdLocationActivity
import com.aelzohry.topsaleqatar.utils.Binding
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import com.aelzohry.topsaleqatar.utils.base.BaseViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import retrofit2.Call


class ProfileFragment : BaseActivity<FragmentProfileBinding, BaseViewModel>() {


    interface ProfileListener {
        fun didUpdateProfile(user: User)
    }

    companion object {
//        const val PICK_CAMERA = 111
//        const val PICK_GALLERY = 222

        @JvmStatic
        fun newInstance(context: Context) =
            context.startActivity(Intent(context, ProfileFragment::class.java))

    }

    private lateinit var repository: Repository
    private var user: User? = null
    private var imagePath: String? = null
    private var addressText: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var listener: ProfileListener? = null
    private var getProfileCall: Call<ProfileResponse>? = null


    override fun getLayoutID(): Int = R.layout.fragment_profile

    override fun getViewModel(): BaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    override fun setupUI() {


        initToolbar(getString(R.string.edit_profile))
        repository = RemoteRepository()
        loadData()


    }

    override fun onClickedListener() {

        binding.refreshLayout.setOnRefreshListener { loadData() }
        binding.avatarImageView.setOnClickListener { avatarDidTap() }
        binding.editButton.setOnClickListener { edit() }
        binding.locationButton.setOnClickListener { picLocation() }
        binding.ivDelete.setOnClickListener {
            imagePath = null
            binding.avatarImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.avatar))
        }

    }

    override fun observerLiveData() {

    }

    private fun picLocation() {
        startActivityForResult(
            Intent(this, AdLocationActivity::class.java),
            NewAdFragment.PLACE_PICKER_REQUEST
        )
    }

    override fun onDestroy() {
        getProfileCall?.cancel()
        super.onDestroy()
    }

    private fun loadData() {
        binding.refreshLayout.isRefreshing = true
        getProfileCall = repository.getProfile { user, message ->
            binding.refreshLayout.isRefreshing = false
            if (user == null) {
                this.let {
                    Helper.showToast(it, message)
                }
            } else {
                this.user = user
                this.fillUserData()
            }
        }
    }

    private fun fillUserData() {
        user?.let {
            imagePath = null
            binding.nameEditText.setText(it._name)
            binding.emailEditText.setText(it.email)
            binding.bioEditText.setText(it.bio)

            it.avatarUrl?.let { url ->
                Binding.loadImage(binding.avatarImageView, url)
            }

            it.location?.let {
                binding.locationButton.text = it.address
                addressText = it.address
                latitude = it.coordinates.get(1)
                longitude = it.coordinates.get(0)
            }
        }
    }

    private fun avatarDidTap() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        pickImageAction()
                    }

                    if (report?.isAnyPermissionPermanentlyDenied == true)
                        Helper.showSettingsDialog(
                            this@ProfileFragment,
                            "${getString(R.string.storage)} - ${getString(R.string.camera)}"
                        )
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun pickImageAction() {
        ImagePicker.with(this)
            .cropSquare()
            .maxResultSize(500, 500)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        ImagePicker.getFilePath(data)?.let {
                            didPickImage(it)
                        }
                    }
                    ImagePicker.RESULT_ERROR -> Helper.showToast(
                        this,
                        ImagePicker.getError(data)
                    )
                }
            }
    }

//    private fun pickImage() {
//        val builder = AlertDialog.Builder(context)
//        builder.setPositiveButton(getString(R.string.camera)) { dialog, _ ->
//            dialog.dismiss()
//            openCamera()
//        }
//
//        builder.setNeutralButton(getString(R.string.gallery)) { dialog, _ ->
//            dialog.dismiss()
//            openGallery()
//        }
//
//        builder.create().show()
//
//    }

//    private fun openCamera() {
//        Dexter.withContext(context)
//            .withPermissions(Manifest.permission.CAMERA)
//            .withListener(object : MultiplePermissionsListener {
//
//                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                    if (report?.areAllPermissionsGranted() == true) {
//                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                        startActivityForResult(intent, PICK_CAMERA)
//                    }
//
//                    if (report?.isAnyPermissionPermanentlyDenied == true)
//                        Helper.showSettingsDialog(requireContext(), getString(R.string.camera))
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: MutableList<PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//                    token?.continuePermissionRequest()
//                }
//            }).check()
//    }
//
//    private fun openGallery() {
//        Dexter.withContext(context)
//            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
//            .withListener(object : MultiplePermissionsListener {
//
//                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                    if (report?.areAllPermissionsGranted() == true) {
//                        val intent =
//                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                        startActivityForResult(intent, PICK_GALLERY)
//                    }
//
//                    if (report?.isAnyPermissionPermanentlyDenied == true)
//                        Helper.showSettingsDialog(requireContext(), getString(R.string.storage))
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: MutableList<PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//                    token?.continuePermissionRequest()
//                }
//            }).check()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//        super.onActivityResult(requestCode, resultCode, intent)
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == PICK_CAMERA) {
//                intent?.extras?.get("data").let { bitmap ->
//                    if (bitmap is Bitmap)
//                        avatarImageView.setImageBitmap(bitmap)
//                        //didPickImage(uri)
//                }
//            } else if (requestCode == PICK_GALLERY) {
//                intent?.data?.let { uri ->
//                    didPickImage(uri)
//                }
//            }
//        }
//    }
//
//    private fun didPickImage(uri: Uri) {
//        Log.i("ProfileFragment", "didPickImage $uri")
//        val path = Helper.getImagePath(uri, activity) ?: return
//        Log.i("ProfileFragment", "didPickImage $path")
////        avatarImageView.setImageURI(Uri.parse(path))
//        val bitmap = BitmapFactory.decodeFile(path)
//        avatarImageView.setImageBitmap(bitmap)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NewAdFragment.PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                latitude = data?.getDoubleExtra("lat", 0.0)
                longitude = data?.getDoubleExtra("lng", 0.0)
                addressText = data?.getStringExtra("address")
                binding.locationButton.text = addressText
            }

        }
    }

    private fun didPickImage(path: String) {
        imagePath = path
        val bitmap = BitmapFactory.decodeFile(path)
        binding.avatarImageView.setImageBitmap(bitmap)
    }

    private fun edit() {
        val context = this ?: return
        val name = binding.nameEditText.text.toString().trim()
        if (name.isEmpty()) {
            Helper.showToast(context, "Enter name")
            return
        }

        val email = binding.emailEditText.text.toString().trim()
        val bio = binding.bioEditText.text.toString().trim()

        val progress = showProgress(context)
        repository.updateProfile(
            name,
            email,
            bio,
            imagePath,
            addressText,
            latitude,
            longitude
        ) { user, message ->
            progress.dismiss()
            Helper.showToast(context, message)
            user?.let {
                this.user = it
                this.listener?.didUpdateProfile(it)
                onBackPressed()
            }
        }
    }

}
