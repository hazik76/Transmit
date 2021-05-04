package ru.planirui.transmit.ui.fragments

import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_username.*
import ru.planirui.transmit.utilits.hideKeyboard

open class BaseChangeFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        btn_ok.setOnClickListener { change() }
        hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
    }

    open fun change() {}
}