package ru.planirui.transmit.ui.fragments

import android.text.BoringLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_change_username.*

open class BaseChangeFragment (layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        btn_ok.setOnClickListener{ change() }
    }

    override fun onStop() {
        super.onStop()
    }

    open fun change() {}
}