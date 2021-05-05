package ru.planirui.transmit.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.APP_ACTIVITY

class SingleChatFragment : BaseFragment(R.layout.fragment_single_chat) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "чат"
        //APP_ACTIVITY.supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.supportActionBar?.show()
        //APP_ACTIVITY.actionBar?.show()
    }
}