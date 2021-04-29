package ru.planirui.transmit.ui.fragments

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import ru.planirui.transmit.MainActivity
import ru.planirui.transmit.R
import ru.planirui.transmit.activities.RegisterActivity
import ru.planirui.transmit.utilits.AUTH
import ru.planirui.transmit.utilits.replaceActivity
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.showToast
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.PhoneAuthProvider as AuthPhoneAuthProvider

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber:String
    private lateinit var mCallback: AuthPhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        mCallback = object : AuthPhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        showToast("Добро пожаловать")
                        (activity as RegisterActivity).replaceActivity(MainActivity())
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
            }

            override fun onCodeSent(
                id: String,
                token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
            ) {
                replaceFragment(EnterCodeFragment(mPhoneNumber,id))
            }
        }
        register_btn_next.setOnClickListener { sendCode() }
    }

    fun sendCode() {
        if (register_input_phone_number.text.toString().isEmpty()){
            showToast("Введте номер телефона")
        } else {
            authUser()
            //replaceFragment(EnterCodeFragment())
        }
    }

    private fun authUser() {
        mPhoneNumber = register_input_phone_number.text.toString()
        AuthPhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            activity as RegisterActivity,
            mCallback
        )
    }
}