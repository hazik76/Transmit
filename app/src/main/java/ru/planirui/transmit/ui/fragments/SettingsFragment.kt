package ru.planirui.transmit.ui.fragments

import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = getString(R.string.action_my_account)
    }

}