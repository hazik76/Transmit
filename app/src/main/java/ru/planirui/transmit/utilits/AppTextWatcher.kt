package ru.planirui.transmit.utilits

import android.text.Editable
import android.text.TextWatcher

/* Модификация класса TextWatcher */

class AppTextWatcher(val onSuccess:(Editable?) -> Unit):TextWatcher {
    override fun afterTextChanged(s:Editable?) {
        onSuccess(s)
    }

    override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}