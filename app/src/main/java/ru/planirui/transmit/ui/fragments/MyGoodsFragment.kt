package ru.planirui.transmit.ui.fragments

import androidx.fragment.app.Fragment
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.APP_ACTIVITY

class MyGoodsFragment : Fragment(R.layout.fragment_my_goods) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_goods)
    }
}