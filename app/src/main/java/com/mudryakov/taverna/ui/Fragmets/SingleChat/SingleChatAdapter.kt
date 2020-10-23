package com.mudryakov.taverna.ui.Fragmets.SingleChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.downloadAndSetImage
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.Objects.transformTime
import com.mudryakov.taverna.Objects.visible
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.appDatabaseHelper.TYPE_IMAGE
import com.mudryakov.taverna.appDatabaseHelper.TYPE_TEXT
import com.mudryakov.taverna.models.MessageModel
import kotlinx.android.synthetic.main.chat_item_recycle.view.*

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.messageViewHolder>() {
    private var messagesList = mutableListOf<MessageModel>()


    class messageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var userMessageText: TextView = view.SingleChatEtUserMessage
        val userMessageTime: TextView = view.SingleChatEtUserMessageTime
        val userMessageLayout: ConstraintLayout = view.SingleChatUserLayout

        val friendMessageText: TextView = view.SingleChatEtFriendMessage
        val friendMessageTime: TextView = view.SingleChatEtFriendMessageTime
        val friendMessageLayout: ConstraintLayout = view.SingleChatFriendLayoutTextMessage

        val userImageMessageLayout: ConstraintLayout = view.UserImageMessageLayout
        val userImageMessageTime: TextView = view.UserMessageImageTime
        val UserMessageImage: ImageView = view.UserMessageImage


        val FriendMessageImageLayout: ConstraintLayout = view.friendImageMessageLayout
        val friendImageMessageTime: TextView = view.FriendMessageImageTime
        val FriendMessageImage: ImageView = view.FriendMessageImage


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): messageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_item_recycle, parent, false)
        return messageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: messageViewHolder, position: Int) {
        val message = messagesList[position]

        holder.userMessageLayout.invisible()
        holder.friendMessageLayout.invisible()
        holder.userImageMessageLayout.invisible()
        holder.FriendMessageImageLayout.invisible()

        if (message.from == CURRENT_UID)
            when (message.type) {
                TYPE_TEXT -> {
                    holder.userMessageLayout.visible()
                    holder.userMessageText.text = message.text
                    holder.userMessageTime.text = message.time.toString().transformTime()
                }
                else-> {
                    holder.userImageMessageLayout.visible()
                    holder.userImageMessageTime.text = message.time.toString().transformTime()
                    holder.UserMessageImage.downloadAndSetImage(message.fileUrl)
                }
            }
        else when (message.type) {
            TYPE_TEXT -> {
                holder.friendMessageLayout.visible()
                holder.friendMessageText.text = message.text
                holder.friendMessageTime.text = message.time.toString().transformTime()
            }
            else -> {
                holder.FriendMessageImageLayout.visible()
                holder.friendImageMessageTime.text = message.time.toString().transformTime()
                holder.FriendMessageImage.downloadAndSetImage(message.fileUrl)
            }
        }


    }

    override fun getItemCount(): Int {
        return messagesList.size
    }


    fun addItemToBot(item: MessageModel) {
        if (!messagesList.any { it.id == item.id }) {
            messagesList.add(item)
            messagesList.sortBy { it.time.toString() }
                       notifyItemInserted(messagesList.size)

        }
    }

    fun addItemToTop(item: MessageModel) {
        if (!messagesList.any { it.id == item.id }) {
            messagesList.add(item)
            messagesList.sortBy { it.time.toString() }

            notifyItemInserted(0)
        }

    }


}