package ru.planirui.transmit.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import ru.planirui.transmit.R
import java.lang.Boolean
import java.util.*


class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val AUTHUI_REQUEST_CODE = 10001
    private var firestoreUsers: CollectionReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firestoreUsers = FirebaseFirestore.getInstance().collection("users")
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            handleLoginRegister()
        }
    }

    private fun handleLoginRegister() {
        // список провайдеров для авторизации
        val providers = arrayListOf(
            EmailBuilder().build(), //ToDo Для тестовой регистрации, потом оставить только телефон
            GoogleBuilder().build(),
            PhoneBuilder().build()
        )
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Toast.makeText(this, "надо авторизовываться 2", Toast.LENGTH_SHORT).show()
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Мы вошли в систему пользователя или у нас есть новый пользователь
                val user = Firebase.auth.currentUser
                //Name, email address
                val userName = user.displayName
                val userEmail = user.email
                val userPhone = user.phoneNumber
                val userId = user.uid

                Log.d(TAG, "onActivityResult: $userName $userEmail")
                //Если время регистрации совпадает с текущим, то новый пользователь
                if (user.metadata.creationTimestamp == user.metadata.lastSignInTimestamp) {
                    Toast.makeText(this, "Добро пожаловать, новый пользователь", Toast.LENGTH_SHORT).show()
                    // Зарегистрировались, нужно создать запись users (userId) в базе данных
                    Log.d(TAG, "onActivityResult: $userName $userEmail")
                    val userData = hashMapOf(
                            "name" to userName,
                            "addressCity" to "",
                            "addressStreet" to "",
                            "phone" to userPhone,
                            "email" to userEmail,
                            "userRating" to 0
                    )
                    firestoreUsers!!.document(userId).set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "Пользователь $userId создан")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                } else {
                    // пользователь вернулся ))) Ура!
                    Toast.makeText(this, "С возвращением!", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                //Вход не выполнен
                val response = IdpResponse.fromResultIntent(data)
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sing request")
                } else {
                    Log.d(TAG, "onActivityResult: ", response.error)
                }
            }
        }
    }
}