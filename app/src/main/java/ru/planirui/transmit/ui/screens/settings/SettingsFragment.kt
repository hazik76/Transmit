package ru.planirui.transmit.ui.screens.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.saveUserPhoto
import ru.planirui.transmit.databinding.FragmentSettingsBinding
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.downloadAndSetImage
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast

/* Фрагмент настроек */

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private var binding: FragmentSettingsBinding? = null

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_account)
        initFields()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
    }

    private fun initFields() {
        binding?.settingsBio?.text = USER.bio
        binding?.settingsFullName?.text = USER.fullname
        binding?.settingsPhoneNumber?.text = USER.phone
        binding?.settingsStatus?.text = USER.state
        binding?.settingsUsername?.text = USER.username
        binding?.settingsHeaderBloc?.setOnClickListener { replaceFragment(ChangeNameFragment()) }
        binding?.settingsBtnChangeUsername?.setOnClickListener {
            replaceFragment(
                ChangeUsernameFragment()
            )
        }
        binding?.settingsBtnChangeBio?.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        binding?.settingsChangePhoto?.setOnClickListener { changePhotoUser() }
        binding?.settingsUserPhoto?.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        /* Изменения фото пользователя */
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность которая запускается для получения картинки для фото пользователя */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            saveUserPhoto(uri) {
                binding?.settingsUserPhoto?.downloadAndSetImage(it)
                showToast(getString(R.string.toast_data_update))
                USER.photoUrl = it
            }
        }
    }
}