package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_text_item.view.*


class HolderTextMessage(view: View):RecyclerView.ViewHolder(view) {
    var userMessageText: TextView = view.SingleChatEtUserMessage
    val userMessageTime: TextView = view.SingleChatEtUserMessageTime
    val userMessageLayout: ConstraintLayout = view.SingleChatUserLayout

    val friendMessageText: TextView = view.SingleChatEtFriendMessage
    val friendMessageTime: TextView = view.SingleChatEtFriendMessageTime
    val friendMessageLayout: ConstraintLayout = view.SingleChatFriendLayoutTextMessage
}