package ru.planirui.transmit.ui.screens.goods

import android.app.Activity
import android.content.Intent
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_add_goods.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast

class AddGoodsFragment(private var idGoods: String) : BaseFragment(R.layout.fragment_add_goods) {

    private lateinit var description: String
    private lateinit var extend: String

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData(){
        if (idGoods == "") {
            APP_ACTIVITY.title = "Добавить вещь"
            getKeyGoods() {
                idGoods = it
                description = "пусто"
                extend = "пусто"
            }
        } else {
            APP_ACTIVITY.title = "Редактировать вещь"
            description = "что-то было, потом добавить инициализацию"
            extend = "что-то было, потом добавить инициализацию"
        }
        initFields()
    }

    private fun initFields() {
        settings_goods_header_bloc.setOnClickListener {
            replaceFragment(ChangeGoodsFragment(extend, idGoods, "name"))
        }
        settings_btn_change_goods_description.setOnClickListener {
            replaceFragment(ChangeGoodsFragment(extend, idGoods, "description"))
        }
        settings_btn_change_goods_extend.setOnClickListener {
            replaceFragment(ChangeGoodsFragment(extend, idGoods, "extend"))
        }
        settings_change_photo_goods.setOnClickListener { changePhotoGoods() }
    }

    private fun changePhotoGoods() {
        /* Изменения фото товара */
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность запускается для получения картинки для фото товара */
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            saveGoodsPhoto(uri, idGoods) {
                //settings_goods_photo.downloadAndSetImage(it)
                showToast(getString(R.string.toast_data_update) + it)
                //USER.photoUrl = it
            }
        }
    }
}