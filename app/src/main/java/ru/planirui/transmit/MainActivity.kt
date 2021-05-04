package ru.planirui.transmit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.planirui.transmit.activities.RegisterActivity
import ru.planirui.transmit.ui.fragments.MyGamesFragment
import ru.planirui.transmit.ui.fragments.MyGoodsFragment
import ru.planirui.transmit.ui.fragments.SettingsFragment
import ru.planirui.transmit.ui.objects.AppDriwer
import ru.planirui.transmit.utilits.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mAppDrawer: AppDriwer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            initFields()
            initFunc()
        }
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            mAppDrawer = AppDriwer(this)
            replaceFragment(MyGamesFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {

    }


    // Меню страниц
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        inflater.inflate(R.menu.settings_action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_account -> {
                showToast("Вы выбрали Аккаунт!")
                replaceFragment(SettingsFragment())
                return true
            }
            R.id.action_myGame -> {
                showToast("Вы выбрали мои игры!")
                replaceFragment(MyGamesFragment())
                return true
            }
            R.id.action_myGoods -> {
                showToast("Вы выбрали мои вещи!")
                replaceFragment(MyGoodsFragment())
                return true
            }
            R.id.action_exit -> {
                FirebaseAuth.getInstance().signOut()   // выйти из аккаунта
                replaceActivity(RegisterActivity())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}