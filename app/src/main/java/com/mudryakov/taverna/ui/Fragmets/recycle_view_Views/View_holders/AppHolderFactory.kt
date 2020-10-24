package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.R
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView

class AppHolderFactory {
    companion object{
        fun getHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder{
            return  when (viewType){
                MessageView.MESSAGE_IMAGE ->{
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_image_item, parent, false)
                    HolderImageMessage(view)}
                 MessageView.MESSAGE_VOICE ->{
                     val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_voice_item, parent, false)
                         HolderVoiceMessage(view)}

                else ->{
                    val view =  LayoutInflater.from(parent.context).inflate(R.layout.chat_text_item, parent, false)
                     HolderTextMessage(view)}
            }

        }
    }
}