package com.mudryakov.taverna.ui.Fragmets.SingleChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.DiffUtillCallback
import com.mudryakov.taverna.Objects.showToast
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.MessageModel
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.Objects.transformTime
import kotlinx.android.synthetic.main.chat_item_recycle.view.*

class SingleChatAdapter() : RecyclerView.Adapter<SingleChatAdapter.messageViewHolder>() {
    private var messagesList = mutableListOf<MessageModel>()


    class messageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var userMessageText: TextView = view.SingleChatEtUserMessage
        val userMessageTime: TextView = view.SingleChatEtUserMessageTime
        val userMessageLayout: ConstraintLayout = view.SingleChatUserLayout

        val friendMessageText: TextView = view.SingleChatEtFriendMessage
        val friendMessageTime: TextView = view.SingleChatEtFriendMessageTime
        val friendMessageLayout: ConstraintLayout = view.SingleChatFriendLayout

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): messageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item_recycle, parent, false)
        return messageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: messageViewHolder, position: Int) {
        val message = messagesList[position]

        if (message.from == CURRENT_UID) {
            holder.userMessageLayout.visibility = View.VISIBLE
            holder.friendMessageLayout.visibility = View.GONE
            holder.userMessageText.text = message.text
            holder.userMessageTime.text = message.time.toString().transformTime()
        } else {
            holder.userMessageLayout.visibility = View.GONE
            holder.friendMessageLayout.visibility = View.VISIBLE
            holder.friendMessageText.text = message.text
            holder.friendMessageTime.text = message.time.toString().transformTime()
        }

    }

    override fun getItemCount(): Int {
        return messagesList.size
    }


    fun addItemToBot(item: MessageModel) {
        if (!messagesList.any { it.id == item.id }) {
           messagesList.sortBy { it.time.toString() }
            messagesList.add(item)
            notifyItemInserted(messagesList.size)

        }
    }
        fun addItemToTop (item: MessageModel){
            if (!messagesList.any { it.id == item.id })
            {messagesList.add(item)
                messagesList.sortBy { it.time.toString() }
                notifyItemInserted(0)}

        }


}