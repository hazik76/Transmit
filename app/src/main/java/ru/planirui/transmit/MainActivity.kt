package ru.planirui.transmit

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.planirui.transmit.activities.RegisterActivity
import ru.planirui.transmit.ui.fragments.ContactsFragment
import ru.planirui.transmit.ui.fragments.MyGamesFragment
import ru.planirui.transmit.ui.fragments.MyGoodsFragment
import ru.planirui.transmit.ui.fragments.SettingsFragment
import ru.planirui.transmit.utilits.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
        }
        initFunc()
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            replaceFragment(MyGamesFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }
    }

    private fun initFields() {
        /* Функция инициализирует переменные, но у меня их нет) */
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
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
            R.id.action_account -> { replaceFragment(SettingsFragment()) }
            R.id.action_myGames -> { replaceFragment(MyGamesFragment()) }
            R.id.action_myGoods -> { replaceFragment(MyGoodsFragment()) }
            R.id.action_myContacts -> { replaceFragment(ContactsFragment()) }
            R.id.action_exit -> {
                FirebaseAuth.getInstance().signOut()   // выйти из аккаунта
                AppStates.updateState(AppStates.OFFLINE)
                replaceActivity(RegisterActivity())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}