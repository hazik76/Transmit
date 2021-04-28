package ru.planirui.transmit.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_enter_code.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import kotlinx.android.synthetic.main.fragment_enter_phone_number.register_input_phone_number
import ru.planirui.transmit.R
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {
    override fun onStart() {
        super.onStart()
        register_btn_next.setOnClickListener { sendCode() }
    }

    fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()){
            showToast("Введте номер телефона")
        } else {
            replaceFragment(EnterCodeFragment())
        }
    }
}