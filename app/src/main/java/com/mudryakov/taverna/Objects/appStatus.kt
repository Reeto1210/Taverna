package com.mudryakov.taverna.Objects

import com.mudryakov.taverna.appDatabaseHelper.*

enum class appStatus(val state:String) {
    ONLINE("в сети"),
    OFFLINE("не в сети"),
    TYPING("печатает");
    companion object {
        fun changeState(status: appStatus) {
            USER.status = status.state
           if (AUTH.currentUser!=null){
               REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATUS)
                   .setValue(status.state).addOnCompleteListener {
                       if (it.isSuccessful)
                       else showToast(it.exception?.message.toString())
                   }
           }
        }
    }
}