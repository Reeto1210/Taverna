package com.mudryakov.taverna.Objects

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class appChildEventValueListener(val function:(DataSnapshot)->Unit): ChildEventListener {
    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
           function(p0)
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {

    }

    override fun onChildRemoved(p0: DataSnapshot) {

    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {

    }

    override fun onCancelled(p0: DatabaseError) {

    }
}