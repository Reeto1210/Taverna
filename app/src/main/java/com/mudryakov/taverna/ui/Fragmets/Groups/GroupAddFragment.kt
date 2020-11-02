package com.mudryakov.taverna.ui.Fragmets.Groups

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.appValueEventListener
import com.mudryakov.taverna.Objects.changeFragment
import com.mudryakov.taverna.Objects.getCommonModel
import com.mudryakov.taverna.Objects.showToast
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import kotlinx.android.synthetic.main.group_add_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class GroupAddFragment : BaseFragment(R.layout.group_add_fragment) {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: GroupsAddAdapter


    override fun onResume() {
        super.onResume()
        initFieleds()
        initRecycle()
    }

    private fun initRecycle() {
        mRecyclerView = groupAddRecycle
        mAdapter = GroupsAddAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        mRecyclerView.adapter = mAdapter
        CoroutineScope(IO).launch { readContacts() }
    }

    private fun initFieleds() {
        GroupsAddAdapter.listUsersForGroup.clear()
        APP_ACTIVITY.title = "Добавить участников"
        groupAddUsers.setOnClickListener {
            if (GroupsAddAdapter.listUsersForGroup.isNotEmpty())
                changeFragment(GroupGreateFragment())
            else showToast("Добавте участников в группу")
        }
    }

    fun readContacts() {
        val path = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
        val userPath = REF_DATABASE_ROOT.child(NODE_USERS)

        path.addListenerForSingleValueEvent(appValueEventListener { dataSnapshot ->
            val friendIdList = dataSnapshot.children.map { it.getCommonModel() }
            friendIdList.forEach { user ->

                userPath.child(user.id).addListenerForSingleValueEvent(appValueEventListener {
                    val currentUser = it.getCommonModel()
                    if (currentUser.fullName.isEmpty()) {
                        currentUser.fullName = user.fullName
                    }
                    mAdapter.addItem(currentUser)
                })
            }
        })
    }
}



