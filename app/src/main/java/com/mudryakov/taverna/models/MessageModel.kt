package com.mudryakov.taverna.models

import java.sql.Timestamp

data class MessageModel(
    val text: String = "",
    val from: String = "",
    val time: Any = "",
    val type: String = "",
    val id: String = "",
    val imageUrl: String = ""
)