package com.mudryakov.taverna.ui.Fragmets.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Fragmets.SingleChat.SingleChatFragment
import com.mudryakov.taverna.Objects.*
import kotlinx.android.synthetic.main.contacts_view.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
    lateinit var myReference: DatabaseReference
    lateinit var mRecycleView: RecyclerView
    lateinit var myAdapter: FirebaseRecyclerAdapter<CommonModel, contactHolder>
    lateinit var myUsers: DatabaseReference
    lateinit var mRefUserListener: appValueEventListener
    var listenersHashMap = HashMap<DatabaseReference, appValueEventListener>()

    class contactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.contact_fullname
        val status = view.contacts_status
        val image = view.contacts_circleImageView
    }


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.Contacts_title)
        initRecycleView()
    }

    private fun initRecycleView() {
        myReference = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
        mRecycleView = Contacts_recycleView
        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(myReference, CommonModel::class.java)
            .build()

        myAdapter = object : FirebaseRecyclerAdapter<CommonModel, contactHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contacts_view, parent, false)
                return contactHolder(view)
            }

            override fun onBindViewHolder(
                holder: contactHolder,
                position: Int,
                model: CommonModel
            ) {

                myUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
                mRefUserListener = appValueEventListener {
                    val contact = it.getCommonModel()
                    if (contact.fullName.isEmpty()) contact.fullName = model.fullName
                    holder.name.text = contact.fullName
                    holder.status.text = contact.status
                    holder.image.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { changeFragment(SingleChatFragment(contact)) }
                }
                myUsers.addValueEventListener(mRefUserListener)
                listenersHashMap[myUsers] = mRefUserListener

            }

        }
        mRecycleView.adapter = myAdapter
        myAdapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        myAdapter.stopListening()
        listenersHashMap.forEach { it.key.removeEventListener(it.value) }
    }
}