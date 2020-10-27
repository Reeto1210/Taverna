package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views

import com.mudryakov.taverna.appDatabaseHelper.TYPE_IMAGE
import com.mudryakov.taverna.appDatabaseHelper.TYPE_VOICE
import com.mudryakov.taverna.models.MessageModel

class AppViewFactory() {
    companion object {
        fun getView(message: MessageModel): MessageView {
            return when (message.type) {
                TYPE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.time.toString(),
                    message.text,
                    message.from,
                    message.fileUrl
                )
                TYPE_VOICE -> ViewVoiceMessage(
                    message.id,
                    message.time.toString(),
                    message.text,
                    message.from,
                    message.fileUrl,
                    message.duration
                )
                else -> ViewTextMessage(
                    message.id,
                    message.time.toString(),
                    message.text,
                    message.from,
                    "Empty"

                )
            }
        }
    }
}