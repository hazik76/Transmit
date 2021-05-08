package ru.planirui.transmit.ui.fragments

import kotlinx.android.synthetic.main.fragment_change_name.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.setNameToDatabase
import ru.planirui.transmit.utilits.*

/* Фрагмент для изменения имени пользователя */

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_account)
        initFullnameList()
    }

    private fun initFullnameList() {
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.size > 1) {
            settings_input_name.setText(fullnameList[0])
            settings_input_surname.setText(fullnameList[1])
        } else settings_input_name.setText(fullnameList[0])
    }

    override fun change() {
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname"
            setNameToDatabase(fullname)
        }
    }
}