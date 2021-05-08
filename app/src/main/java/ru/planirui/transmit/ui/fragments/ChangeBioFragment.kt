package ru.planirui.transmit.ui.fragments

import kotlinx.android.synthetic.main.fragment_change_bio.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.setBioToDatabase
import ru.planirui.transmit.utilits.*

/* Фрагмент для изменения информации о пользователе */

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {
    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()
        setBioToDatabase(newBio)
    }
}