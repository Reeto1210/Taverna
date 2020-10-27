package com.mudryakov.taverna.ui.Fragmets.SingleChat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders.*
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView

class SingleChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var message: MessageView
    private var messagesList = mutableListOf<MessageView>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).drawMessage1(message)
        }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).onAttached(messagesList[holder.adapterPosition])
        super.onViewAttachedToWindow(holder)

    }





    override fun getItemViewType(position: Int): Int {
        message = messagesList[position]
        return message.getTypeView()
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }


    fun addItemToBot(item: MessageView) {
       if (!messagesList.any { it.id == item.id }) {
           messagesList.add(item)
           messagesList.sortBy { it.time }
           notifyItemInserted(messagesList.size)

       }
    }

    fun addItemToTop(item: MessageView) {
        if (!messagesList.any { it.id == item.id }) {
            messagesList.add(item)
            messagesList.sortBy { it.time }
            notifyItemInserted(0)
        }
    }
}