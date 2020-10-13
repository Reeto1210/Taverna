package com.mudryakov.taverna.Objects

import androidx.recyclerview.widget.DiffUtil
import com.mudryakov.taverna.models.MessageModel

class DiffUtillCallback (private var oldList:List<MessageModel>, private var newList:List<MessageModel>): DiffUtil.Callback(){
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return newList[newItemPosition].time == oldList[oldItemPosition].time
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  newList[newItemPosition] == oldList[oldItemPosition]
    }

}