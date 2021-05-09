package ru.planirui.transmit.ui.message_recycler_view.views

import ru.planirui.transmit.models.CommonModel
import ru.planirui.transmit.utilits.TYPE_MESSAGE_IMAGE
import ru.planirui.transmit.utilits.TYPE_MESSAGE_VOICE

class AppViewFactory {

    companion object{
        fun getView(message: CommonModel):MessageView{
            return when(message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                TYPE_MESSAGE_VOICE -> ViewVoiceMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}