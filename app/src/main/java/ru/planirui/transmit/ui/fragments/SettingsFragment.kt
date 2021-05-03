package ru.planirui.transmit.ui.fragments

import android.view.MenuItem
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.USER
import ru.planirui.transmit.utilits.initFirebase
import ru.planirui.transmit.utilits.replaceFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = getString(R.string.action_my_account)
        initFields()
    }

    private fun initFields() {
        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullname
        settings_phone_number.text = USER.phone
        settings_status.text = USER.status
        settings_username.text = USER.username
        // settings_btn_change_username.setOnClickListener { replaceFragment(ChangeNameFragment()) }
        settings_header_bloc.setOnClickListener { replaceFragment(ChangeNameFragment()) }
        settings_btn_change_username.setOnClickListener{ replaceFragment(ChangeUsernameFragment()) }
    }
}