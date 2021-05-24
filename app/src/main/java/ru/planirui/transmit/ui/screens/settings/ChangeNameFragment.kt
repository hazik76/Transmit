package ru.planirui.transmit.ui.screens.settings

import android.os.Bundle
import android.view.View
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.setNameToDatabase
import ru.planirui.transmit.databinding.FragmentChangeNameBinding
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.showToast

/* Фрагмент для изменения имени пользователя */

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    private var binding: FragmentChangeNameBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeNameBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_account)
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            binding?.settingsInputName?.setText(fullnameList[0])
            binding?.settingsInputSurname?.setText(fullnameList[1])
        } else binding?.settingsInputName?.setText(fullnameList[0])
    }

    override fun change() {
        val name = binding?.settingsInputName?.text.toString()
        val surname = binding?.settingsInputSurname?.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            setNameToDatabase(fullname)
        }
    }
}