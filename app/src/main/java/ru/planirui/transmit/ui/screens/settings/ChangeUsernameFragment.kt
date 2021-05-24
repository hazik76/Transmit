package ru.planirui.transmit.ui.screens.settings

import android.os.Bundle
import android.view.View
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.databinding.FragmentChangeUsernameBinding
import ru.planirui.transmit.ui.screens.base.BaseChangeFragment
import ru.planirui.transmit.utilits.AppValueEventListener
import ru.planirui.transmit.utilits.showToast
import java.util.*

/* Фрагмент для изменения username пользователя */

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    private var binding: FragmentChangeUsernameBinding? = null
    private lateinit var mNewUsername: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeUsernameBinding.bind(view)
        binding?.settingsInputUsername?.setText(USER.username)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun change() {
        mNewUsername = binding?.settingsInputUsername?.text.toString().toLowerCase(Locale.ROOT)
        if (mNewUsername.isEmpty()) {
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mNewUsername)) {
                        showToast("Такой пользователь уже существует")
                    } else {
                        changeUsername()
                    }
                })
        }
    }

    private fun changeUsername() {
        /* Изменение username в базе данных */
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mNewUsername)
                }
            }
    }
}