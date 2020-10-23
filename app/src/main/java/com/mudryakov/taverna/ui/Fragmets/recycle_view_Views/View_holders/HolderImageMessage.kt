package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_image_item.view.*


class HolderImageMessage(view: View):RecyclerView.ViewHolder(view) {
    val userImageMessageLayout: ConstraintLayout = view.UserImageMessageLayout
    val userImageMessageTime: TextView = view.UserMessageImageTime
    val UserMessageImage: ImageView = view.UserMessageImage


    val FriendMessageImageLayout: ConstraintLayout = view.friendImageMessageLayout
    val FriendImageMessageTime: TextView = view.FriendMessageImageTime
    val FriendMessageImage: ImageView = view.FriendMessageImage
}