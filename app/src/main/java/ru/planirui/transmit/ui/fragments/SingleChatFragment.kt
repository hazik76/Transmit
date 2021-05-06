package ru.planirui.transmit.ui.fragments

import android.view.View
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_info.view.*
import ru.planirui.transmit.R
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.models.UserModel
import ru.planirui.transmit.utilits.*

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserModel
    private lateinit var mToolbarInfo: View
    private lateinit var mRefUser: DatabaseReference

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "чат"
        mToolbarInfo = toolbar_info
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUserModel()
            initInfoToolbar()
        }

        mRefUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
        chat_btn_send_message.setOnClickListener {
            val message = chat_input_message.text.toString()
            if (message.isNotEmpty()) sendMessage(message, contact.id, TYPE_TEXT){
                hideKeyboard()
                chat_input_message.setText("")
            }

        }
    }

    private fun initInfoToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mToolbarInfo.toolbar_chat_fullname.text = contact.fullname
        } else mToolbarInfo.toolbar_chat_fullname.text = mReceivingUser.fullname
        mToolbarInfo.toolbar_chat_status.text = mReceivingUser.state
        mToolbarInfo.toolbar_chat_image.downloadAndSetImage(mReceivingUser.photoUrl)

    }

    override fun onPause() {
        super.onPause()
        //APP_ACTIVITY.supportActionBar?.show()
        mRefUser.removeEventListener(mListenerInfoToolbar)
    }
}