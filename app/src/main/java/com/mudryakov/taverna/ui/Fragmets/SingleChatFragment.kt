package com.mudryakov.taverna.ui.Fragmets

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.models.MessageModel
import com.mudryakov.taverna.ui.Objects.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_for_chat.view.*


class SingleChatFragment(private val model: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var mToolbarInfo: View
    lateinit var friendUser: CommonModel
    lateinit var toolbarListener: appValueEventListener
    lateinit var refForToolbarUser: DatabaseReference
    lateinit var mAdapter: SingleChatAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var mRef: DatabaseReference
    lateinit var myListener: appValueEventListener
    var list = emptyList<MessageModel>()
    override fun onResume() {
        super.onResume()
        initFriendToolbar()
        initRecycle()


    }

    private fun initRecycle() {
        recyclerView = SingleChatRecycle
        mRef = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(model.id)
        mAdapter = SingleChatAdapter()
        myListener = appValueEventListener { DataSnapshot ->
            list = DataSnapshot.children.map { it.getCommonMessage() }
            mAdapter.setList(list)
        recyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        mRef.addValueEventListener(myListener)
        recyclerView.adapter = mAdapter

    }


    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        refForToolbarUser.removeEventListener(toolbarListener)
    mRef.removeEventListener(myListener)
    }


    fun initFriendToolbar() {
        mToolbarInfo = APP_ACTIVITY.toolbar_main.ToolbarInfoMain
        mToolbarInfo.visibility = View.VISIBLE
        toolbar = APP_ACTIVITY.toolbar_main
        toolbar.setNavigationOnClickListener { APP_ACTIVITY.changeFragment(ChatFragment()) }
        refForToolbarUser = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)
        APP_ACTIVITY.title = ""
        toolbarListener = appValueEventListener {
            friendUser = it.getCommonModel()
            if (friendUser.fullName.isEmpty())
                mToolbarInfo.singleChatTVname.text = model.fullName
            else
                mToolbarInfo.singleChatTVname.text = friendUser.fullName

            mToolbarInfo.singleChatTVStatus.text = friendUser.status
            mToolbarInfo.singleChatFriend.downloadAndSetImage(friendUser.photoUrl)
        }

        refForToolbarUser.addValueEventListener(toolbarListener)
        btnSendSingleMessage.setOnClickListener {
            val textMessage = SingleChatMessageLayout.text.toString()
            if (textMessage.isNotEmpty())
                sendMessage(textMessage, model.id, TYPE_TEXT) {
                    SingleChatMessageLayout.setText("")
                } else showToast("Введите сообщение")
        }
    }
}