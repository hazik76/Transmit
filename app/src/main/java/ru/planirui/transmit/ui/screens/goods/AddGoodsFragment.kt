package ru.planirui.transmit.ui.screens.goods

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.theartofdev.edmodo.cropper.CropImage
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.getGoodsInfo
import ru.planirui.transmit.database.saveGoodsPhoto
import ru.planirui.transmit.databinding.FragmentAddGoodsBinding
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.downloadAndSetImageGoods
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast
import kotlin.properties.Delegates

/* Добавляем/редактируем вещь пользователя */

class AddGoodsFragment(private var idGoods: String) : BaseFragment(R.layout.fragment_add_goods) {

    private var binding: FragmentAddGoodsBinding? = null

    private lateinit var extend: String
    private var ifExists by Delegates.notNull<Boolean>()
    private lateinit var goods: CommonModel

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddGoodsBinding.bind(view)
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
        binding?.settingsGoodsName?.text = goods.name
        binding?.settingsStatus?.text = goods.status
        binding?.settingsGoodsDescription?.text = goods.description
        binding?.settingsGoodsExtension?.text = (goods.extend)
        if (goods.uriPhoto.isNotEmpty()) {
            binding?.settingsGoodsPhoto?.downloadAndSetImageGoods(goods.uriPhoto)
        }
        binding?.settingsGoodsHeaderBloc?.setOnClickListener {
            extend = goods.name
            changeData("name")
        }
        binding?.settingsBtnChangeGoodsDescription?.setOnClickListener {
            extend = goods.description
            changeData("description")
        }
        binding?.settingsBtnChangeGoodsExtend?.setOnClickListener {
            extend = goods.extend
            changeData("extend")
        }
        binding?.settingsChangePhotoGoods?.setOnClickListener {
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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            saveGoodsPhoto(uri, idGoods) {
                binding?.settingsGoodsPhoto?.downloadAndSetImageGoods(it)
                showToast(getString(R.string.toast_data_update) + it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}