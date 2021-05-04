package ru.planirui.transmit.ui.fragments

import android.util.Log
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.*


class MyGamesFragment : BaseFragment(R.layout.fragment_my_games) {
    private val TAG = "MyGamesFragment"

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_games)

    }
}