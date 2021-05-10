package ru.planirui.transmit.ui.screens

import ru.planirui.transmit.R
import ru.planirui.transmit.ui.screens.base.BaseFragment
import ru.planirui.transmit.utilits.APP_ACTIVITY

/* Главный фрагмент, содержит все игры пользователя */

class MyGamesFragment : BaseFragment(R.layout.fragment_my_games) {
    private val TAG = "MyGamesFragment"

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.action_my_games)
    }
}