package ru.planirui.transmit.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import ru.planirui.transmit.R
import ru.planirui.transmit.databinding.ActivityRegisterBinding
import ru.planirui.transmit.ui.fragments.EnterPhoneNumberFragment
import ru.planirui.transmit.utilits.replaceActivity
import ru.planirui.transmit.utilits.replaceFragment

private lateinit var mBinding: ActivityRegisterBinding
@SuppressLint("StaticFieldLeak")
private lateinit var mToolbar: Toolbar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        mToolbar = mBinding.registerToolbar
//        setSupportActionBar(mToolbar)
        title = getString(R.string.register_title_your_phone)

        replaceFragment(EnterPhoneNumberFragment())
    }
}