package ru.planirui.transmit

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.planirui.transmit.activities.RegisterActivity
import ru.planirui.transmit.ui.fragments.GamesFragment
import ru.planirui.transmit.ui.fragments.SettingsFragment
import ru.planirui.transmit.ui.objects.AppDriwer
import ru.planirui.transmit.utilits.replaceActivity
import ru.planirui.transmit.utilits.replaceFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mAppDrawer: AppDriwer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /*// Добавим фрагмент
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragmentMVVM>(R.id.dataContainer)
            }*/
        }
    }

    override fun onStart() {
        super.onStart()
        initFunc()
    }

    private fun initFunc() {
        if (true) {
            mAppDrawer = AppDriwer(this)
            replaceFragment(GamesFragment())
        } else {
            replaceActivity(RegisterActivity())

        }
    }

    // Меню страниц
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        inflater.inflate(R.menu.settings_action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_account -> {
                mAppDrawer.testFunc("Вы выбрали Аккаунт!")
                replaceFragment(SettingsFragment())
                return true
            }
            R.id.action_myGame -> {
                mAppDrawer.testFunc("Вы выбрали мои игры!")
                replaceFragment(GamesFragment())
                return true
            }
            R.id.action_myGoods -> {
                mAppDrawer.testFunc("Вы выбрали мои вещи!")
                return true
            }
            R.id.action_exit -> {
                FirebaseAuth.getInstance().signOut()   // выйти из аккаунта
                replaceActivity(RegisterActivity())
                //val intent = Intent(this@MainActivity, LoginActivity::class.java)
                //startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}