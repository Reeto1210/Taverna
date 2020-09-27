package com.mudryakov.taverna.models

data class CommonModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullName: String = "Unknown",
    var status: String = "не в сети",
    var phoneNumber: String = "",
    var photoUrl: String = "https://firebasestorage.googleapis.com/v0/b/taverna-4436e.appspot.com/o/utilits%2Fkisspng-computer-icons-user-profile-user-5abf1fd8a08734.8661348115224749686575.jpg?alt=media&token=075435a0-53b7-40e3-8cf2-9d155a8f0753"
)