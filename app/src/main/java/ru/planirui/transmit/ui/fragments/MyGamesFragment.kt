package ru.planirui.transmit.ui.fragments

import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R

class MyGamesFragment : BaseFragment(R.layout.fragment_my_games) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).title = getString(R.string.action_my_games)
    }
}