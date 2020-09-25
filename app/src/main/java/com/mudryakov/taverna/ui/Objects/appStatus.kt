package com.mudryakov.taverna.ui.Objects

enum class appStatus(val state:String) {
    ONLINE("в сети"),
    OFFLINE("не в сети"),
    TYPING("печатает");
    companion object {
        fun changeState(status: appStatus) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATUS)
                .setValue(status.state).addOnCompleteListener {
                    if (it.isSuccessful) USER.status = status.state
                    else showToast(it.exception?.message.toString())
                }
        }
    }
}