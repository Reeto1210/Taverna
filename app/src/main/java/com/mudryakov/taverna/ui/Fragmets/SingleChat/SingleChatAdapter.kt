package com.mudryakov.taverna.ui.Fragmets.SingleChat

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.downloadAndSetImage
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.Objects.transformTime
import com.mudryakov.taverna.Objects.visible
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders.AppHolderFactory
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders.HolderImageMessage
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders.HolderTextMessage
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var message: MessageView
    private var messagesList = mutableListOf<MessageView>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder){
            is HolderTextMessage -> drawMessageText(holder)
            is HolderImageMessage -> drawMessageImage(holder)

        }

    }

    override fun getItemViewType(position: Int): Int {
        message = messagesList[position]
Log.d("test", message.getTypeView().toString())
        return message.getTypeView()

    }

    private fun drawMessageImage(holder: HolderImageMessage) {
        holder.userImageMessageLayout.invisible()
        holder.FriendMessageImageLayout.invisible()
        if (message.from == CURRENT_UID) {
            holder.userImageMessageLayout.visible()
            holder.userImageMessageTime.text = message.time.transformTime()
            holder.UserMessageImage.downloadAndSetImage(message.fileUrl)
        } else {
            holder.FriendMessageImageLayout.visible()
            holder.FriendImageMessageTime.text = message.time.transformTime()
            holder.FriendMessageImage.downloadAndSetImage(message.fileUrl)
        }
    }

    private fun drawMessageText(holder: HolderTextMessage) {
        holder.userMessageLayout.invisible()
        holder.friendMessageLayout.invisible()
        if (message.from == CURRENT_UID) {
            holder.userMessageLayout.visible()
            holder.userMessageText.text = message.text
            holder.userMessageTime.text = message.time.transformTime()
        } else {
            holder.friendMessageLayout.visible()
            holder.friendMessageText.text = message.text
            holder.friendMessageTime.text = message.time.transformTime()
        }


    }


    override fun getItemCount(): Int {
        return messagesList.size
    }


    fun addItemToBot(item: MessageView) {
        if (!messagesList.any { it.id == item.id }) {
            messagesList.add(item)
            messagesList.sortBy { it.time.toString() }
            notifyItemInserted(messagesList.size)

        }
    }

    fun addItemToTop(item: MessageView) {
        if (!messagesList.any { it.id == item.id }) {
            messagesList.add(item)
            messagesList.sortBy { it.time.toString() }

            notifyItemInserted(0)
        }

    }


}