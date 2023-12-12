package com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.view

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.aelzohry.topsaleqatar.R
import com.aelzohry.topsaleqatar.databinding.ActivityCustomImageBinding
import com.aelzohry.topsaleqatar.model.CustomImage
import com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.adapter.TemplateGiftsAdapter
import com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.dialog.view.CustomPhotoDataDialogFragment
import com.aelzohry.topsaleqatar.ui.new_ad.customPhotos.viewModel.CustomPhotosViewModel
import com.aelzohry.topsaleqatar.utils.base.BaseActivity
import gun0912.tedimagepicker.util.ToastUtil
import java.util.*

class CustomPhotosActivity : BaseActivity<ActivityCustomImageBinding, CustomPhotosViewModel>() {

    private lateinit var mAdapter: TemplateGiftsAdapter
    private var selectedTemplateId = 0


    companion object {
        fun newInstance(context: Context): Intent =
            Intent(context, CustomPhotosActivity::class.java)
    }

    override fun getLayoutID(): Int = R.layout.activity_custom_image

    override fun getViewModel(): CustomPhotosViewModel =
        ViewModelProvider(this)[CustomPhotosViewModel::class.java]


    override fun setupUI() {
        initAdapter()
    }

    override fun onClickedListener() {
        binding.btnNext.setOnClickListener { onNextBtnClick() }
    }


    override fun observerLiveData() {
    }


    private fun onNextBtnClick() {
        if (selectedTemplateId == 0) {
            ToastUtil.showToast(getString(R.string.please_select_custom_photos))
            return
        }
        showCustomImageDialog()
    }

    private fun showCustomImageDialog() {
        val dialogFragment: CustomPhotoDataDialogFragment =
            CustomPhotoDataDialogFragment.newInstance(selectedTemplateId)

        dialogFragment.setListener(object : CustomPhotoDataDialogFragment.Listener {
            override fun onItemSelected(path: String) {
                Log.e("test_image", path)

                val intentResult = Intent()
                intentResult.putExtra("path", path)
                setResult(RESULT_OK, intentResult)
                onBackPressed()
            }
        })
        dialogFragment.show(
            supportFragmentManager,
            CustomPhotosActivity::class.java.getSimpleName()
        )
    }

    private fun initAdapter() {
        mAdapter = TemplateGiftsAdapter(this) { position, item ->
            selectedTemplateId = item.id
            mAdapter.updateSelectedItem(position)
        }
        binding.rvList.itemAnimator = DefaultItemAnimator()
        binding.rvList.adapter = mAdapter

        fillData()
    }

    private fun fillData() {
        val list: ArrayList<CustomImage> = ArrayList<CustomImage>()
        list.add(CustomImage(1, "", 1 == selectedTemplateId))
        list.add(CustomImage(2, "", 2 == selectedTemplateId))
        list.add(CustomImage(3, "", 3 == selectedTemplateId))
        list.add(CustomImage(4, "", 4 == selectedTemplateId))
        list.add(CustomImage(5, "", 5 == selectedTemplateId))
        list.add(CustomImage(6, "", 6 == selectedTemplateId))
//        if (selectedTemplate != null) {
//            selectedTemplate.setDiscount(currentAmount)
//            selectedTemplate.setDesc(description)
//        }
        mAdapter.removeAll()
        mAdapter.addItem(list)
    }


}