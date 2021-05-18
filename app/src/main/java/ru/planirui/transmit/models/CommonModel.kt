package ru.planirui.transmit.models

import android.net.Uri
import androidx.core.net.toUri

/* Общая модель для всех сущностей приложения*/

class CommonModel {
    val id: String = ""
    var username: String = ""
    var bio: String = ""
    var fullname: String = ""
    var state: String = ""
    var phone: String = ""
    var photoUrl: String = "empty"

    var text: String = ""
    var type: String = ""
    var from: String = ""
    var timeStamp: Any = ""
    var fileUrl: String = "empty"

    var lastMessage:String = ""
    var choice:Boolean = false

    var name: String = ""
    var description: String = ""
    var uriPhoto: String = ""
    var extend: String = ""
    var goodsID: String = ""
    var status: String = ""

    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}