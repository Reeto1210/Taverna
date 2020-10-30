package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.Objects.transformTime
import com.mudryakov.taverna.Objects.visible
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView
import kotlinx.android.synthetic.main.chat_text_item.view.*


class HolderTextMessage(view: View):RecyclerView.ViewHolder(view), MessageHolder {
    var userMessageText: TextView = view.SingleChatEtUserMessage
    val userMessageTime: TextView = view.SingleChatEtUserMessageTime
    val userMessageLayout: ConstraintLayout = view.SingleChatUserLayout

    val friendMessageText: TextView = view.SingleChatEtFriendMessage
    val friendMessageTime: TextView = view.SingleChatEtFriendMessageTime
    val friendMessageLayout: ConstraintLayout = view.SingleChatFriendLayoutTextMessage
    override fun drawMessage1(view: MessageView) {
        userMessageLayout.invisible()
        friendMessageLayout.invisible()
        if (view.from == CURRENT_UID) {
            userMessageLayout.visible()
            userMessageText.text = view.text
            userMessageTime.text = view.time.transformTime()
        } else {
            friendMessageLayout.visible()
            friendMessageText.text = view.text
            friendMessageTime.text = view.time.transformTime()
        }
    }

    override fun onAttached(View: MessageView) {

    }
}


