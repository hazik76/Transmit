package ru.planirui.transmit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.planirui.transmit.ui.fragments.GamesFragment
import ru.planirui.transmit.ui.fragments.SettingsFragment
import ru.planirui.transmit.ui.objects.AppDriwer
import ru.planirui.transmit.view.LoginActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var mAppDrawer:AppDriwer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /*// Добавим фрагмент
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragmentMVVM>(R.id.fragment_container_view)
            }*/
        }
    }

    override fun onStart() {
        super.onStart()
        initFunc()
    }

    private fun initFunc() {
        mAppDrawer = AppDriwer(this)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, GamesFragment()).commit()
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
                Toast.makeText(applicationContext, "Вы выбрали Аккаунт!", Toast.LENGTH_SHORT).show()
                supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container_view, SettingsFragment()).commit()
                return true
            }
            R.id.action_myGame -> {
                mAppDrawer.testFunc("Вы выбрали мои игры!")
                return true
            }
            R.id.action_myGoods -> {
                Toast.makeText(applicationContext, "Вы выбрали мои вещи!", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_exit -> {
                FirebaseAuth.getInstance().signOut()   // выйти из аккаунта
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}