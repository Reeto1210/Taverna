package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views

data class ViewTextMessage(
    override val id: String,
    override val time: String,
    override val text: String,
    override val from: String,
    override val fileUrl: String = "",
    override val duration: String = ""
) :MessageView{
    override fun getTypeView():Int {
        return MessageView.MESSAGE_TEXT
    }
}