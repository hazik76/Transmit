package ru.planirui.transmit.ui.screens.settings

import android.os.Bundle
import android.view.View
import ru.planirui.transmit.R
import ru.planirui.transmit.database.USER
import ru.planirui.transmit.database.setBioToDatabase
import ru.planirui.transmit.databinding.FragmentChangeBioBinding
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment

/* Фрагмент для изменения информации о пользователе */

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    private var binding: FragmentChangeBioBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeBioBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        binding?.settingsInputBio?.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = binding?.settingsInputBio?.text.toString()
        setBioToDatabase(newBio)
    }
}