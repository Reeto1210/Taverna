package com.mudryakov.taverna.ui.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.core.view.isVisible
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.database.DatabaseReference
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.ui.Objects.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.contacts_view.*
import kotlinx.android.synthetic.main.toolbar_for_chat.*
import kotlinx.android.synthetic.main.toolbar_for_chat.view.*


class SingleChatFragment(private val model: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var mToolbarInfo:View
    lateinit var friendUser: CommonModel
    lateinit var toolbarListener: appValueEventListener
    lateinit var refForToolbarUser: DatabaseReference

    override fun onResume() {
        super.onResume()
        mToolbarInfo = APP_ACTIVITY.toolbar_main.ToolbarInfoMain
        mToolbarInfo.visibility = View.VISIBLE
        initFriendToolbar()

    }

    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
   refForToolbarUser.removeEventListener(toolbarListener)
    }


    fun initFriendToolbar() {
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

    }
}