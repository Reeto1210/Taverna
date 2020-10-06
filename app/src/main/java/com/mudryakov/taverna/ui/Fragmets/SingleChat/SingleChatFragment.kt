package com.mudryakov.taverna.ui.Fragmets.SingleChat

import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Fragmets.MainFragment
import com.mudryakov.taverna.Objects.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_for_chat.view.*


class SingleChatFragment(private val model: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {


    private lateinit var mToolbarInfo: View
    lateinit var friendUser: CommonModel
    lateinit var toolbarListener: appValueEventListener
    lateinit var refForToolbarUser: DatabaseReference
    lateinit var mAdapter: SingleChatAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var mRef: DatabaseReference
    lateinit var myListener: appChildEventValueListener
    lateinit var chatAddMessageListener: appChildEventValueListener
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRefreshLayout: SwipeRefreshLayout

    var mSmooth = true
    var mIsScrolling = false
    var count = 10

    override fun onResume() {
        super.onResume()
        initFriendToolbar()

        initRecycle()
    }

    private fun initRecycle() {
        mRefreshLayout = SingleChatRefreshLayout
        recyclerView = SingleChatRecycle
        mLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = mLayoutManager
        mRef = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(model.id)
        mAdapter = SingleChatAdapter()

        recyclerView.adapter = mAdapter

        chatAddMessageListener = appChildEventValueListener {
            mAdapter.addItemToBot(it.getCommonMessage())
            if (mSmooth) recyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        myListener = appChildEventValueListener {
            mAdapter.addItemToTop(it.getCommonMessage()){mRefreshLayout.isRefreshing = false}
        }

        mRef.limitToLast(count).addChildEventListener(chatAddMessageListener)
        mRefreshLayout.setOnRefreshListener {
            updateAdapter()
        }


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                    mIsScrolling = true
                count += 20
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 5) {
                    updateAdapter()     //<---- если был скролл вверх
                }
                mSmooth = mIsScrolling && mLayoutManager.findLastVisibleItemPosition() == mAdapter.itemCount - 1
            }
        })
        }

    private fun updateAdapter() {

        mIsScrolling = false
        mSmooth = false
        count += 10
        mRef.removeEventListener(myListener)
        mRef.limitToLast(count).addChildEventListener(myListener)

    }


    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        TOOLBAR.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        refForToolbarUser.removeEventListener(toolbarListener)
        mRef.removeEventListener(myListener)
        mRef.removeEventListener(chatAddMessageListener)
    }


    fun initFriendToolbar() {
        mToolbarInfo = APP_ACTIVITY.toolbar_main.ToolbarInfoMain
        mToolbarInfo.visibility = View.VISIBLE

        TOOLBAR.setNavigationOnClickListener { changeFragment(MainFragment()) }
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

            mSmooth = true
            val textMessage = SingleChatMessageLayout.text.toString()
            if (textMessage.isNotEmpty())
                sendMessage(textMessage, model.id, TYPE_TEXT) {
                    SingleChatMessageLayout.setText("")
                } else showToast("Введите сообщение")
        }
    }

}