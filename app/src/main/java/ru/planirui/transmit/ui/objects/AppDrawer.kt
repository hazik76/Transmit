package ru.planirui.transmit.ui.objects

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.planirui.transmit.utilits.APP_ACTIVITY

/* Активность не нужна */

class AppDrawer {

    fun testFunc(textPrint: String) {
        Toast.makeText(APP_ACTIVITY, textPrint, Toast.LENGTH_SHORT).show()
    }

}