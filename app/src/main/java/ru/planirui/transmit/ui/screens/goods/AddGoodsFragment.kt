package ru.planirui.transmit.ui.screens.goods

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_add_goods.*
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.downloadAndSetImage
import ru.planirui.transmit.utilits.showToast
import kotlin.properties.Delegates

class AddGoodsFragment(private var urlGoods: String) : BaseFragment(R.layout.fragment_add_goods){

    private var goodsIsTrue by Delegates.notNull<Boolean>()

    override fun onResume() {
        super.onResume()
        if (urlGoods == ""){
            APP_ACTIVITY.title = "Добавить вещь"
            goodsIsTrue = false
        }else{
            APP_ACTIVITY.title = "Редактировать вещь"
            goodsIsTrue = true
        }
        initFields()
    }

    private fun initFields() {
        settings_change_photo_goods.setOnClickListener { changePhotoGoods() }
    }

    private fun changePhotoGoods() {
        /* Изменения фото товара, потом переделать в добавление нового */
        if (goodsIsTrue) changePhotoGoods2(urlGoods.toUri())
        else {
            urlGoods = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(
                NODE_GAME_GOODS).push().key.toString()
            changePhotoGoods2(urlGoods.toUri())
        }

    }
    private fun changePhotoGoods2(urlGoods:Uri){
//        CropImage.activity()
//            .setAspectRatio(1, 1)
//            .setRequestedSize(250, 250)
//            .setCropShape(CropImageView.CropShape.OVAL)
//            .start(APP_ACTIVITY, this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность запускается для получения картинки для фото товара */
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            saveGoodsPhoto(uri){
                settings_goods_photo.downloadAndSetImage(it)
                showToast(getString(R.string.toast_data_update))
                USER.photoUrl = it
            }
        }

    }
}