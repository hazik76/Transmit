package ru.planirui.transmit.ui.fragments.message_recycler_view.views

interface MessageView {

    val id: String
    val from: String
    val timeStamp: String
    val fileUrl: String
    val text: String

    companion object {
        val MESSAGE_IMAGE: Int
            get() = 0
        val MESSAGE_TEXT
            get() = 1
    }

    fun getTypeView():Int
}