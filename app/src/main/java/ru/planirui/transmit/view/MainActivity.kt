package ru.planirui.transmit.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import ru.planirui.transmit.R


/*
 *      MainActivity
 *      - opens our fragment which has the UI
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            // Adds our fragment
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragment>(R.id.fragment_container_view)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_account -> {
                Toast.makeText(applicationContext, "Вы выбрали Аккаунт!", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_myGame -> {
                Toast.makeText(applicationContext, "Вы выбрали моя игра!", Toast.LENGTH_SHORT).show()
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