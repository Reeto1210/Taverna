package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views

interface MessageView {
    val id: String
    val time: String
    val text: String
    val from: String
    val fileUrl: String
    val duration:String
    companion object {
        val MESSAGE_IMAGE: Int
            get() = 0
        val MESSAGE_TEXT: Int
            get() = 1
        val MESSAGE_VOICE: Int
            get() = 2
    val MESSAGE_FILE:Int
        get() = 3
    }

    fun getTypeView(): Int
}