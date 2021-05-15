package ru.planirui.transmit.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import ru.planirui.transmit.models.UserModel
import kotlin.properties.Delegates

/* Файл содержит все необходимые инструменты для работы с базой данных */
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: UserModel
const val TYPE_TEXT = "text"
const val NODE_USERS = "users"
const val NODE_MESSAGES = "messages"
const val NODE_USERNAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONES_CONTACTS = "phones_contacts"
const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_FILES = "messages_files"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timeStamp"
const val CHILD_FILE_URL = "fileUrl"

const val NODE_MAIN_LIST = "main_list"
const val NODE_GROUPS = "groups"
const val NODE_MEMBERS = "members"
const val FOLDER_GROUPS_IMAGE = "groups_image"
const val USER_CREATOR = "creator"
const val USER_ADMIN = "admin"
const val USER_MEMBER = "member"

const val GAME_STATUS = "status"
const val GAME_STATUS_CURRENT = "current"
const val GAME_STATUS_COMPLETED = "completed" //завершенные
const val GAME_STATUS_ENDING = "ending"    //завершающиеся
const val GAME_TIME_STARTING = "time_starting"
const val GAME_TIME_ENDING = "time_ending"

const val NODE_GOODS = "goods"
const val GOODS_OWNER = "owner"
const val GOODS_ID = "goodsID"
const val GOODS_NAME = "name"
const val NODE_GOODS_IMAGE = "image"
const val GAME_GOODS_PHOTO = "uriPhoto"
const val GOODS_DESCRIPTION = "description"
const val GOODS_EXTEND = "extend"
const val GOODS_BOOKED_1 = "booked_1"  //забронирована кем
const val GOODS_BOOKED_2 = "booked_2"
const val GOODS_BOOKED_3 = "booked_3"
const val GOODS_STATUS = "status"
const val GOODS_STATUS_ADDED = "added"
const val GOODS_STATUS_PLAYS = "plays" // играет, тогда нельзя менять описание, название, только картинку и extends
const val GOODS_STATUS_NO_RECEIVED = "no_received" // --//--
const val GOODS_STATUS_RECEIVED = "received"// --//--

const val FOLDER_GOODS_IMAGE = "goods_image"