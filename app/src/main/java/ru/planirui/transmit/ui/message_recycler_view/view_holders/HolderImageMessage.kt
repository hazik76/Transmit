package ru.planirui.transmit.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item_image.view.*
import ru.planirui.transmit.database.CURRENT_UID
import ru.planirui.transmit.ui.message_recycler_view.views.MessageView
import ru.planirui.transmit.utilits.asTime
import ru.planirui.transmit.utilits.downloadAndSetImage

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blocReceivedImageMessage: ConstraintLayout = view.bloc_received_image_message
    private val blocUserImageMessage: ConstraintLayout = view.bloc_user_image_message
    private val chatReceivedImage: ImageView = view.chat_received_image
    private val chatUserImage: ImageView = view.chat_user_image

    private val chatReceivedImageMessageTime: TextView = view.chat_received_image_message_time
    private val chatUserImageMessageTime: TextView = view.chat_user_image_message_time

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blocReceivedImageMessage.visibility = View.GONE
            blocUserImageMessage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blocReceivedImageMessage.visibility = View.VISIBLE
            blocUserImageMessage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {}

    override fun onDetach() {}
}