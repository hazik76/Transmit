package ru.planirui.transmit.ui.screens.goods

import android.app.Activity
import android.content.Intent
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_goods.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.getGoodsInfo
import ru.planirui.transmit.database.saveGoodsPhoto
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.downloadAndSetImageGoods
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast
import kotlin.properties.Delegates

class AddGoodsFragment(private var idGoods: String) : BaseFragment(R.layout.fragment_add_goods) {

    private lateinit var extend: String
    private var ifExists by Delegates.notNull<Boolean>()
    private lateinit var goods: CommonModel

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        if (USER.tempMessage != "") { // т.е. мы выполнили один из фрагментов по смене поля и завели запись в БД
            idGoods = USER.tempMessage
            USER.tempMessage = "" // обнулим тестовое сообщение
        }
        if (idGoods == "") {
            APP_ACTIVITY.title = "Добавить вещь"
            goods = CommonModel()
            goods.name = "пусто"
            goods.status = "не создана"
            goods.description = "пусто"
            goods.extend = "пусто"
            ifExists = false
            initFields()
        } else {
            getGoodsInfo(idGoods) {
                goods = it
                APP_ACTIVITY.title = "Редактировать вещь"
                ifExists = true
                initFields()
            }
        }
    }

    private fun initFields() {
        settings_goods_name.setText(goods.name)
        settings_status.setText(goods.status)
        settings_goods_description.setText(goods.description)
        settings_goods_extension.setText((goods.extend))
        if (goods.uriPhoto.isNotEmpty()) {
            settings_goods_photo.downloadAndSetImageGoods(goods.uriPhoto)
        }
        settings_goods_header_bloc.setOnClickListener {
            extend = goods.name
            changeData("name")
        }
        settings_btn_change_goods_description.setOnClickListener {
            extend = goods.description
            changeData("description")
        }
        settings_btn_change_goods_extend.setOnClickListener {
            extend = goods.extend
            changeData("extend")
        }
        settings_change_photo_goods.setOnClickListener {
            if (ifExists) changePhotoGoods()
            else showToast("Сначала добавьте название вещи")
        }
    }

    private fun changeData(changeName: String) {
        if (ifExists) replaceFragment(ChangeGoodsFragment(extend, idGoods, changeName))
        else replaceFragment(ChangeGoodsFragment(extend, idGoods, changeName))
    }

    private fun changePhotoGoods() {
        /* Изменения фото товара */
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            //.setCropShape(CropImageView.CropShape.OVAL)
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
                settings_goods_photo.downloadAndSetImageGoods(it)
                showToast(getString(R.string.toast_data_update) + it)
            }
        }
    }
}