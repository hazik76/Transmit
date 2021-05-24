package ru.planirui.transmit.ui.screens.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import ru.planirui.transmit.R
import ru.planirui.transmit.database.*
import ru.planirui.transmit.databinding.FragmentEnterCodeBinding
import ru.planirui.transmit.utilits.*

/* Фрагмент для ввода кода подтверждения при регистрации */

class EnterCodeFragment(private val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    private var binding: FragmentEnterCodeBinding? = null

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEnterCodeBinding.bind(view)
        binding?.registerInputCode?.addTextChangedListener(AppTextWatcher {
            val string = binding?.registerInputCode?.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        /* Функция проверяет код, если все нормально, производит создания информации о пользователе в базе данных */
        val code = binding?.registerInputCode?.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        hideKeyboard()
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber

                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener {
                        if (!it.hasChild(CHILD_USERNAME)) {
                            dateMap[CHILD_USERNAME] = uid
                        }

                        REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                            .addOnFailureListener {}
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                                    .updateChildren(dateMap)
                                    .addOnSuccessListener {
                                        showToast("Добро пожаловать")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })

            } else showToast(task.exception?.message.toString())
        }
    }
}