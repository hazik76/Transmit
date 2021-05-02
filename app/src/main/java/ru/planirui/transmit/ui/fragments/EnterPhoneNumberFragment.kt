package ru.planirui.transmit.ui.fragments

import android.util.Log
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.activities.RegisterActivity
import ru.planirui.transmit.utilits.AUTH
import ru.planirui.transmit.utilits.replaceActivity
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {
    private val TAG = "EnterPhoneNumberFragment"
    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        register_input_phone_number.setText("+79030000000") // Что бы тестовый телефон не вбивать, кд в смс 000000
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Добро пожаловать")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }
        register_btn_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = register_input_phone_number.text.toString()

        val options = PhoneAuthOptions
            .newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(mPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as RegisterActivity)
            .setCallbacks(mCallback)
            .build()
        Log.d(TAG, options.toString())
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}