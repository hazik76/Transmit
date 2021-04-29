package ru.planirui.transmit.ui.fragments

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.activities.RegisterActivity
import ru.planirui.transmit.utilits.AUTH
import ru.planirui.transmit.utilits.AppTextWatcher
import ru.planirui.transmit.utilits.replaceActivity
import ru.planirui.transmit.utilits.showToast

class EnterCodeFragment(val phoneNumber: String, val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id,code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                showToast("Добро пожаловать")
                (activity as RegisterActivity).replaceActivity(MainActivity())
            }else showToast(task.exception?.message.toString())
        }
    }
}