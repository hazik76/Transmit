package ru.planirui.transmit.ui.fragments

import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_username.*
import ru.planirui.transmit.utilits.APP_ACTIVITY

open class BaseChangeFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        btn_ok.setOnClickListener { change() }
    }

    override fun onStop() {
        super.onStop()
        APP_ACTIVITY.hideKeyboard()
    }

    open fun change() {}
}