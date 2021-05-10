package ru.planirui.transmit.ui.screens.base

import androidx.fragment.app.Fragment

/* Базовый фрагмент, от него наследуются все фрагменты приложения, кроме главного */

open class BaseFragment(private val layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
    }
}