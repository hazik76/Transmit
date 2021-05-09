package ru.planirui.transmit.ui.screens

import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_username.*
import ru.planirui.transmit.utilits.hideKeyboard

/* Базовый фрагмент, от него наследуются фрагменты где происходит изменение данных о пользователе. */

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