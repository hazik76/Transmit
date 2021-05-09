package ru.planirui.transmit.ui.screens

import androidx.fragment.app.Fragment
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.APP_ACTIVITY

/* Фрагмент вещей пользователя */

class MyGoodsFragment : Fragment(R.layout.fragment_my_goods) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_goods)
    }
}