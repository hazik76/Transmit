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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import ru.planirui.transmit.R
import java.lang.Boolean
import java.util.*


class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginRegisterActivity"
    var AUTHUI_REQUEST_CODE = 10001
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
        //Toast.makeText(this, "надо авторизовываться", Toast.LENGTH_SHORT).show()
        val providers = arrayListOf(
            EmailBuilder().build(),
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
        Toast.makeText(this, "надо авторизовываться 2", Toast.LENGTH_SHORT).show()
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Мы вошли в систему пользователя или у нас есть новый пользователь?
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "onActivityResult: " + user.email)
                //Если время регистрации совпадает с текущим, то новый пользователь
                if (user.metadata.creationTimestamp == user.metadata.lastSignInTimestamp) {
                    Toast.makeText(this, "Добро пожаловать, новый пользователь", Toast.LENGTH_SHORT)
                        .show()
                    val userId = FirebaseAuth.getInstance().currentUser.uid
                    Log.d(TAG, "userId=$userId")
                    // Каким-то образом зарегистрировались, нужно создать запись users (userId) в базе данных
                    val profileMap = HashMap<String, Any>()
                    profileMap["name"] =
                        "" // по идее можно обойтись только этим полем, но в Profile Activity идут запросы к другм полям
                    profileMap["phone"] = ""
                    profileMap["email"] = ""
                    profileMap["showPhone"] = Boolean.TRUE
                    profileMap["showMail"] = Boolean.TRUE
                    profileMap["showAddress"] = Boolean.TRUE
                    //TODO Надо ещё подабавлять полей из ProfileActivity

                    //Так как пользователя не существовало, то set(), а не update()
                    firestoreUsers!!.document(userId).set(profileMap)
                        .addOnSuccessListener { Log.d(TAG, "Пользователь $userId создан") }
                    // По хорошему это не нужно, но иначе нарастает количество записей в массивах address и addressGlobal
                    firestoreUsers!!.document(userId)
                        .set(profileMap) // ToDo Подумать, как убрать эту фигню(
                        .addOnSuccessListener {
                            Log.d(
                                TAG,
                                "onSuccess: " + Void.TYPE
                            )
                        }
                    val userAddress2 =
                        firestoreUsers!!.document(userId) //создадим пустые массивы геолокаций
                    val addressGeo = GeoPoint(0.0, 0.0)
                    userAddress2.update(
                        "address",
                        FieldValue.arrayUnion(""),
                        "addressGlobal",
                        FieldValue.arrayUnion(""),
                        "location",
                        FieldValue.arrayUnion(addressGeo)
                    )
                        .addOnSuccessListener {
                            Log.d(
                                TAG,
                                "onSuccess: Обновили список адресов пользователя "
                            )
                        }.addOnFailureListener { e -> Log.e(TAG, "onFailure: ", e) }
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