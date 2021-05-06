package ru.planirui.transmit.utilits

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.models.UserModel
import java.util.*

/* Файл содержит все необходимые инструменты для работы с базой данных */
const val TAG = "appDatabaseHelper"
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: UserModel

const val NODE_USERS = "users"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"
const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

fun initFirebase() {
    /* Инициализация базы данных Firebase */
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT =
        FirebaseDatabase.getInstance("https://transmit-da0e5-default-rtdb.europe-west1.firebasedatabase.app/").reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    /* Функция высшего порядка, отпраляет полученый URL в базу данных */
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTO_URL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    /* Функция высшего порядка, получает  URL картинки из хранилища */
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    /* Функция высшего порядка, отправляет картинку в хранилище */
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun initUser(crossinline function: () -> Unit) {
    /* Функция высшего порядка, инициализация текущей модели USER */
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(AppValueEventListener {
            USER = it.getValue(UserModel::class.java) ?: UserModel()
            if (USER.username.isEmpty()) {
                USER.username = CURRENT_UID
                function()
            } else {
                function()
            }
        })
}

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    /* Функция добавляет номер телефона с id в базу данных */
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(AppValueEventListener {
        it.children.forEach { snapshot ->
            arrayContacts.forEach { contact ->
                if (snapshot.key == contact.phone) {
                    REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                        .child(snapshot.value.toString()).child(CHILD_ID)
                        .setValue(snapshot.value.toString())
                        .addOnFailureListener { e -> showToast(e.message.toString()) }

                    REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                        .child(snapshot.value.toString()).child(CHILD_FULLNAME)
                        .setValue(contact.fullname)
                        .addOnFailureListener { e -> showToast(e.message.toString()) }
                }
            }
        }
    })
}

/* Функция преобразовывает полученые данные из Firebase в модель CommonModel */
fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()