package ru.planirui.transmit.ui.objects

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AppDriwer(val mainActivity: AppCompatActivity) {


    fun testFunc(textPrint: String) {
        Toast.makeText(mainActivity, textPrint, Toast.LENGTH_SHORT).show()
    }

}