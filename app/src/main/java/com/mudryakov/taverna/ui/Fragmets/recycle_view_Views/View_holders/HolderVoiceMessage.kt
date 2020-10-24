package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.downloadAndSetImage
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.Objects.transformTime
import com.mudryakov.taverna.Objects.visible
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView

import kotlinx.android.synthetic.main.chat_image_item.view.UserImageMessageLayout
import kotlinx.android.synthetic.main.chat_voice_item.view.*


class HolderVoiceMessage(view: View):RecyclerView.ViewHolder(view),MessageHolder {

    val userVoiceMessageLayout: ConstraintLayout = view.UserVoiceMessageLayout
    val userVoiceMessageTime: TextView = view.UserMessageVoiceTime
    val btnUserVoicePlay:ImageView = view.btnUserMessageVoicePlay
    val btnUserVoiceStop:ImageView = view.btnUserMessageVoiceStop


    val FriendMessageImageLayout: ConstraintLayout = view.FriendVoiceMessageLayout
    val FriendImageMessageTime: TextView = view.FriendMessageVoiceTime
    val btnFriendVoiceStop:ImageView = view.btnFriendMessageVoiceStop
    val btnFriendVoicePlay:ImageView = view.btnFriendMessageVoiceStop
    override fun drawMessage1(view: MessageView) {
        userVoiceMessageLayout.invisible()
        FriendMessageImageLayout.invisible()
        if (view.from == CURRENT_UID) {
           userVoiceMessageLayout.visible()
            userVoiceMessageTime.text = view.time.transformTime()

        } else {
           FriendMessageImageLayout.visible()
            FriendImageMessageTime.text = view.time.transformTime()


        }
    }


}
