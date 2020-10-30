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
import kotlinx.android.synthetic.main.chat_image_item.view.*

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    val userImageMessageLayout: ConstraintLayout = view.UserImageMessageLayout
    val userImageMessageTime: TextView = view.UserMessageImageTime
    val UserMessageImage: ImageView = view.UserMessageImage


    val FriendMessageImageLayout: ConstraintLayout = view.friendImageMessageLayout
    val FriendImageMessageTime: TextView = view.FriendMessageImageTime
    val FriendMessageImage: ImageView = view.FriendMessageImage

    override fun drawMessage1(view: MessageView) {
        userImageMessageLayout.invisible()
        FriendMessageImageLayout.invisible()
        if (view.from == CURRENT_UID) {
            userImageMessageLayout.visible()
            userImageMessageTime.text = view.time.transformTime()
            UserMessageImage.downloadAndSetImage(view.fileUrl)
        } else {
            FriendMessageImageLayout.visible()
            FriendImageMessageTime.text = view.time.transformTime()
            FriendMessageImage.downloadAndSetImage(view.fileUrl)
        }
    }

    override fun onAttached(View: MessageView) {

    }

}



