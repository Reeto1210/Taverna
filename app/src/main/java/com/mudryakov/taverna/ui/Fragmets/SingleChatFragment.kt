package com.mudryakov.taverna.ui.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.isVisible
import com.google.android.gms.common.internal.service.Common
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.ui.Objects.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contacts_view.*
import kotlinx.android.synthetic.main.toolbar_for_chat.*
import kotlinx.android.synthetic.main.toolbar_for_chat.view.*


class SingleChatFragment(freindId: String) : BaseFragment(R.layout.fragment_single_chat) {
    private val  contactId = freindId
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var friendUser: CommonModel
    override fun onResume() {
        super.onResume()
        initFriendToolbar()

    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.TooolbarForChat.visibility = View.GONE
        toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack()}
    }


    fun initFriendToolbar() {
        toolbar = APP_ACTIVITY.toolbar_main
        toolbar.setNavigationOnClickListener { APP_ACTIVITY.changeFragment(ChatFragment()) }
        APP_ACTIVITY.TooolbarForChat.visibility = View.VISIBLE
        APP_ACTIVITY.title = ""
        REF_DATABASE_ROOT.child(NODE_USERS).child(contactId)
            .addValueEventListener(appValueEventListener {
                friendUser = it.getValue(CommonModel::class.java) ?: CommonModel()
                APP_ACTIVITY.TooolbarForChat.singleChatTVname.text = friendUser.fullName
                APP_ACTIVITY.TooolbarForChat.singleChatTVStatus.text = friendUser.status
                APP_ACTIVITY.TooolbarForChat.singleChatFriend.downloadAndSetImage(friendUser.photoUrl)
            })
    }

}