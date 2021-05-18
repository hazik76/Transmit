package ru.planirui.transmit.ui.screens.register

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import ru.planirui.transmit.R
import ru.planirui.transmit.database.AUTH
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.replaceFragment
import ru.planirui.transmit.utilits.restartActivity
import ru.planirui.transmit.utilits.showToast
import java.util.concurrent.TimeUnit

/* Фрагмент для ввода номера телефона при регистрации */

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.supportActionBar?.hide()
        /* Callback который возвращает результат верификации */
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /* Функция срабатывает если верификация уже была произведена,
                * пользователь авторизируется в приложении без потверждения по смс */
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Добро пожаловать")
                        restartActivity()
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                /* Функция срабатывает если верификация не удалась*/
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                /* Функция срабатывает если верификация впервые, и отправлена смс */
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }
        btn_ok.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        /* Функция проверяет поле для ввода номер телефона, если поле пустое выводит сообщение.
         * Если поле не пустое, то начинает процедуру авторизации/ регистрации */
        if (register_input_phone_number.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        /* Инициализация */
        mPhoneNumber = register_input_phone_number.text.toString()

        val options = PhoneAuthOptions
            .newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(mPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(APP_ACTIVITY)
            .setCallbacks(mCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}