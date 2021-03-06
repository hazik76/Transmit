package ru.planirui.transmit.database

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ru.planirui.transmit.R
import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.models.UserModel
import ru.planirui.transmit.utilits.APP_ACTIVITY
import ru.planirui.transmit.utilits.AppValueEventListener
import ru.planirui.transmit.utilits.TYPE_GROUP
import ru.planirui.transmit.utilits.showToast
import java.io.File
import java.util.*

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

inline fun putUrlGoodsToDatabase(url: String, idGoods: String, crossinline function: () -> Unit) {
    /* Функция высшего порядка, отпраляет полученый URL в базу данных */
    /* Пока добавляем одно единственное изображение */

    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_GOODS).child(idGoods)
        .child(GAME_GOODS_PHOTO)
        .setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    /* Функция высшего порядка, получает URL картинки из хранилища */
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
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
            }
            function()
        })
}

fun saveUserPhoto(uri: Uri, function: (String) -> Unit) {
    /* заносим фото пользователя в хранилище */
    val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
        .child(CURRENT_UID)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            putUrlToDatabase(it) {
                function(it)
            }
        }
    }
}

fun getKeyGoods(function: (String) -> Unit) {
    val idGoods = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(
        NODE_GOODS
    ).push().key.toString()
    function(idGoods)
}

fun saveGoodsPhoto(uri: Uri, idGoods: String, function: (String) -> Unit) {
    /* заносим фото товара в хранилище */
    val path = REF_STORAGE_ROOT.child(FOLDER_GOODS_IMAGE)
        .child(idGoods)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            putUrlGoodsToDatabase(it, idGoods) {
                function(it)
            }
        }
    }
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

fun sendMessage(message: String, receivingUserID: String, typeText: String, function: () -> Unit) {
    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_UID"
    val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun updateCurrentUsername(newUserName: String) {
    /* Обновление username в базе данных у текущего пользователя */
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
        .setValue(newUserName)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
                deleteOldUsername(newUserName)
            } else {
                showToast(it.exception?.message.toString())
            }
        }
}

private fun deleteOldUsername(newUserName: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            APP_ACTIVITY.supportFragmentManager.popBackStack()
            USER.username = newUserName
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setBioToDatabase(newBio: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(newBio)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.bio = newBio
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun newGoodsCreate(function: (String) -> Unit) {
    getKeyGoods() { keyGoods ->
        val mapMessage = hashMapOf<String, Any>()
        mapMessage[GOODS_DESCRIPTION] = ""
        mapMessage[GOODS_EXTEND] = ""
        mapMessage[GOODS_NAME] = "Empty"
        mapMessage[GOODS_REGION] = "Москва"
        mapMessage[GOODS_ID] = keyGoods
        mapMessage[GOODS_STATUS] = GOODS_STATUS_ADDED

        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_GOODS).child(keyGoods)
            .updateChildren(mapMessage)
            .addOnSuccessListener { function(keyGoods) }
            .addOnFailureListener { }
    }
}

fun setGoodsChangeToDatabase(newDescriptions: String, idGoods: String, changeName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_GOODS).child(idGoods)
        .child(changeName)
        .setValue(newDescriptions)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun setNameToDatabase(fullname: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
        .setValue(fullname)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_data_update))
            USER.fullname = fullname
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }.addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageAsFile(
    receivingUserID: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {

    val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$receivingUserID"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserID/$CURRENT_UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getMessageKey(id: String) = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    .child(id).push().key.toString()

fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receivedID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILES).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFile(receivedID, it, messageKey, typeMessage, filename)
        }
    }
}

fun getFileFromStorage(mFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(mFile)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun saveToMainList(id: String, type: String) {
    val refUser = "$NODE_MAIN_LIST/$CURRENT_UID/$id"
    val refReceived = "$NODE_MAIN_LIST/$id/$CURRENT_UID"

    val mapUser = hashMapOf<String, Any>()
    val mapReceived = hashMapOf<String, Any>()

    mapUser[CHILD_ID] = id
    mapUser[CHILD_TYPE] = type

    mapReceived[CHILD_ID] = CURRENT_UID
    mapReceived[CHILD_TYPE] = type

    val commonMap = hashMapOf<String, Any>()
    commonMap[refUser] = mapUser
    commonMap[refReceived] = mapReceived

    REF_DATABASE_ROOT.updateChildren(commonMap)
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun deleteChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(id).removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener { function() }
}

fun clearChat(id: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(id)
        .removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(id)
                .child(CURRENT_UID)
                .removeValue()
                .addOnSuccessListener { function() }
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun deleteGroupChat(keyGroup: String, function: (Boolean) -> Unit) {
    val path = REF_DATABASE_ROOT.child(NODE_GROUPS).child(keyGroup)
    val users = hashMapOf<String, String>()
    var isThereACreator = false

    path.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
        dataSnapshot.child(NODE_MEMBERS).children.map {
            if (it.value.toString() == USER_CREATOR) isThereACreator = true
            users[it.key.toString()] = it.value.toString()
        }
        if ((!isThereACreator && users[CURRENT_UID] == USER_ADMIN) || users[CURRENT_UID] == USER_CREATOR) {
            for ((key) in users) {
                REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(key).child(keyGroup).removeValue()
                    .addOnFailureListener { showToast(it.message.toString()) }
                    .addOnSuccessListener { }
            }
            path.removeValue()
                .addOnFailureListener { showToast(it.message.toString()) }
                .addOnSuccessListener { }
            function(true)
        } else {
            function(false)
        }
    })
}

fun exitGroupChat(keyGroup: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(keyGroup).removeValue()
        .addOnFailureListener { showToast(it.message.toString()) }
        .addOnSuccessListener { function() }
}

fun clearGroupChat(keyGroup: String, function: (Boolean) -> Unit) {
    val path = REF_DATABASE_ROOT.child(NODE_GROUPS).child(keyGroup)
    val users = hashMapOf<String, String>()
    path.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
        dataSnapshot.child(NODE_MEMBERS).children.map {
            users[it.key.toString()] = it.value.toString()
        }
        if (users[CURRENT_UID] == USER_ADMIN || users[CURRENT_UID] == USER_CREATOR) {
            path.child(NODE_MESSAGES).removeValue()
                .addOnFailureListener { showToast(it.message.toString()) }
                .addOnSuccessListener { }
            function(true)
        } else {
            function(false)
        }
    })
}

fun createGroupToDatabase(
    nameGroup: String,
    uriGroup: Uri,
    timeEnd: String,
    uriGoods: String,
    nameGoods: String,
    regionGoods: String,
    photoGoods: String,
    listContacts: List<CommonModel>,
    function: () -> Unit
) {
    val keyGroup = REF_DATABASE_ROOT.child(NODE_GROUPS).push().key.toString()
    val path = REF_DATABASE_ROOT.child(NODE_GROUPS).child(keyGroup)
    val pathStorage = REF_STORAGE_ROOT.child(FOLDER_GROUPS_IMAGE).child(keyGroup)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = keyGroup
    mapData[CHILD_FULLNAME] = nameGroup
    mapData[CHILD_PHOTO_URL] = "empty"
    mapData[GAME_STATUS] = GAME_STATUS_CURRENT
    mapData[GAME_TIME_STARTING] = ServerValue.TIMESTAMP
    mapData[GAME_TIME_ENDING] = timeEnd

    // добавим пустой товар в "игру"
    val mapGoods = hashMapOf<String, Any>()
    val mapGoods2 = hashMapOf<String, Any>()
    mapGoods2[GOODS_OWNER] = CURRENT_UID
    mapGoods2[GOODS_ID] = uriGoods
    mapGoods2[GOODS_NAME] = nameGoods
    mapGoods2[GOODS_REGION] = regionGoods
    mapGoods2[GAME_GOODS_PHOTO] = photoGoods
    mapGoods2[GOODS_BOOKED_1] = ""
    mapGoods2[GOODS_BOOKED_2] = ""
    mapGoods2[GOODS_BOOKED_3] = ""
    mapGoods2[GOODS_STATUS] = GOODS_STATUS_PLAYS
    mapGoods[uriGoods] = mapGoods2

    val mapMembers = hashMapOf<String, Any>()
    listContacts.forEach {
        if (it.id != CURRENT_UID) mapMembers[it.id] = USER_MEMBER
    }
    mapMembers[CURRENT_UID] = USER_CREATOR

    mapData[NODE_GOODS] = mapGoods
    mapData[NODE_MEMBERS] = mapMembers

    path.updateChildren(mapData)
        .addOnSuccessListener {
            function()
            if (uriGroup != Uri.EMPTY) {
                putFileToStorage(uriGroup, pathStorage) {
                    getUrlFromStorage(pathStorage) {
                        path.child(CHILD_PHOTO_URL).setValue(it)
                        addGroupsToMainList(mapData, listContacts) {
                            function()
                        }
                    }
                }
            } else {
                addGroupsToMainList(mapData, listContacts) {
                    function()
                }
            }
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun addGroupsToMainList(
    mapData: HashMap<String, Any>,
    listContacts: List<CommonModel>,
    function: () -> Unit
) {
    val path = REF_DATABASE_ROOT.child(NODE_MAIN_LIST)
    val map = hashMapOf<String, Any>()

    map[CHILD_ID] = mapData[CHILD_ID].toString()
    map[CHILD_TYPE] = TYPE_GROUP
    listContacts.forEach {
        path.child(it.id).child(map[CHILD_ID].toString()).updateChildren(map)
    }
    path.child(CURRENT_UID).child(map[CHILD_ID].toString()).updateChildren(map)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun sendMessageToGroup(message: String, groupID: String, typeText: String, function: () -> Unit) {

    val refMessages = "$NODE_GROUPS/$groupID/$NODE_MESSAGES"
    val messageKey = REF_DATABASE_ROOT.child(refMessages).push().key

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeText
    mapMessage[CHILD_TEXT] = message
    mapMessage[CHILD_ID] = messageKey.toString()
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

    REF_DATABASE_ROOT.child(refMessages).child(messageKey.toString())
        .updateChildren(mapMessage)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun uploadFileToStorageToGroup(
    uri: Uri,
    messageKey: String,
    groupID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILES).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendMessageAsFileToGroup(groupID, it, messageKey, typeMessage, filename)
        }
    }
}

fun sendMessageAsFileToGroup(
    groupID: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {
    val refMessages = "$NODE_GROUPS/$groupID/$NODE_MESSAGES"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = CURRENT_UID
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_FILE_URL] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    REF_DATABASE_ROOT.child(refMessages).child(messageKey)
        .updateChildren(mapMessage)
        .addOnSuccessListener { showToast("file saved") }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun initRVAddContact(function: (List<CommonModel>, CommonModel) -> Unit) {
    // добавляем пользователей в список для создния группы/беседы
    // (многоурвневе добавление из initRecyclerView/AddContactActivity)
    val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    initRVAddContact2() { dataSnapshot1, model ->
        val newModel = dataSnapshot1.getCommonModel()
        // 3 запрос
        mRefMessages.child(model.id).limitToLast(1)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                val tempList = dataSnapshot2.children.map { it.getCommonModel() }
                function(tempList, newModel)
            })
    }
}

fun initRVAddContact2(function: (DataSnapshot, CommonModel) -> Unit) {
    val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    initRVAddContact1() { model ->
        // 2 запрос
        mRefUsers.child(model.id)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                function(dataSnapshot1, model)
            })
    }
}

fun initRVAddContact1(function: (CommonModel) -> Unit) {
    val mRefContactsList = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    var mListItems: List<CommonModel>
    // 1 запрос
    mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
        mListItems = dataSnapshot.children.map { it.getCommonModel() }
        mListItems.forEach { model ->
            function(model)
        }
    })
}

fun mRefUserGroup(groupID: String): DatabaseReference {
    return REF_DATABASE_ROOT.child(NODE_USERS).child(groupID)
}

fun mRefMessagesGroup(groupID: String): DatabaseReference {
    return REF_DATABASE_ROOT
        .child(NODE_GROUPS)
        .child(groupID)
        .child(NODE_MESSAGES)
}

fun getGoodsListInfo(function: (List<CommonModel>) -> Unit) {
    var mListItems: List<CommonModel>
    val mRefGoodsList =
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_GOODS)
    mRefGoodsList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
        mListItems = dataSnapshot.children.map { it.getCommonModel() }
        function(mListItems)
    })
}

fun getGoodsInfo(idGoods: String, function: (CommonModel) -> Unit) {
    val mRefGoodsList =
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_GOODS)
            .child(idGoods)
    mRefGoodsList.addListenerForSingleValueEvent(AppValueEventListener {
        val goods = it.getValue(CommonModel::class.java) ?: CommonModel()
        function(goods)
    })
}