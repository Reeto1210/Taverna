package com.mudryakov.taverna.ui.Objects

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class appValueEventListener(val onSuccess: (DataSnapshot) -> Unit): ValueEventListener {

    override fun onDataChange(p0: DataSnapshot) {
   onSuccess(p0)
    }

    override fun onCancelled(p0: DatabaseError) {}
}