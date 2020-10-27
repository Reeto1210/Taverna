package com.mudryakov.taverna.ui.Fragmets.SingleChat

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.mudryakov.taverna.Objects.*
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Fragmets.MainFragment
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.AppViewFactory
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_for_chat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


class SingleChatFragment(private val model: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {


    private lateinit var mToolbarInfo: View
    lateinit var friendUser: CommonModel
    lateinit var toolbarListener: appValueEventListener
    lateinit var refForToolbarUser: DatabaseReference
    lateinit var mAdapter: SingleChatAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var mRef: DatabaseReference

    lateinit var chatAddMessageListener: appChildEventValueListener
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRefreshLayout: SwipeRefreshLayout
    lateinit var keyboardVisibilityEventListener: KeyboardVisibilityEventListener
    lateinit var mMediaRecorder: AppMediaRecorder
    lateinit var uri: Uri
    lateinit var path1: StorageReference
    var voiceDuration: Int = 0
    var mSmooth = true
    var mIsScrolling = false
    var count = 15


    override fun onResume() {
        super.onResume()
        initField()
        initFriendToolbar()
        initRecycle()
        initMessageSent()
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n", "SimpleDateFormat")
    private fun initMessageSent() {

        btnSendSingleImageAttach.setOnClickListener {

            attachImage()
        }
        btnSendSingleMessage.setOnClickListener {

            mSmooth = true
            val textMessage = SingleChatMessageLayout.text.toString()
            val key = REF_DATABASE_ROOT.child("/$NODE_MESSAGES/$CURRENT_UID/${model.id}")
                .push().key.toString()
            sendMessage(textMessage, model.id, TYPE_TEXT, "", key) {
                SingleChatMessageLayout.setText("")
            }

        }
        initkeyboardListener()

        btnSendSingleVoiceMessage.setOnTouchListener { v, event ->
            if (checkPermission(RECOR_AUDIO)) {
                if (event.action == MotionEvent.ACTION_DOWN) {

                    btnSendSingleVoiceMessage.setColorFilter(
                        ContextCompat.getColor(
                            APP_ACTIVITY,
                            R.color.colorPrimary
                        )
                    )
                    val key = getMessageKey(model.id)
                    mMediaRecorder.startRecord(key){
                        CoroutineScope(Main).launch {
                            var recordTime = 0
                            while (event.action != MotionEvent.ACTION_UP) {
                                SingleChatMessageLayout.setText(
                                    "Идёт запись ${recordTime.transformForTimer("m:ss.S")}"
                                )
                                delay(100)
                                recordTime += 100
                            }
                            voiceDuration = recordTime
                        }

                    }



                }
                if (event.action == MotionEvent.ACTION_UP) {
                    mMediaRecorder.stopRecord { file, key ->
                        uri = Uri.fromFile(file)
                        path1 = REF_STORAGE_ROOT.child(NODE_FILES).child(key)
                        sendCurrentMessage(TYPE_VOICE, key)
                    }
                    btnSendSingleVoiceMessage.colorFilter = null

                    SingleChatMessageLayout.setText("")
                }

            }


            true
        }

    }

    private fun sendCurrentMessage(type: String, key: String) {

        mSmooth = true
        putFileToStorage(path1, uri) {
            downloadUrl(path1) {
                addUrlBase(it) { //it -> URL
                    sendMessage(
                        type = type,
                        friendId = model.id,
                        text = "",
                        fileUrl = it,
                        key = key,
                        duration = (voiceDuration+1000).toString()
                    ) {}
                }
            }

        }
    }


    private fun initkeyboardListener() {
        SingleChatMessageLayout.addTextChangedListener(MyTextWhatcher {
            if (SingleChatMessageLayout.text.isNotEmpty()) {
                btnSendSingleImageAttach.invisible()
                btnSendSingleMessage.visible()
                btnSendSingleVoiceMessage.invisible()
            } else {
                btnSendSingleImageAttach.visible()
                btnSendSingleMessage.invisible()
                btnSendSingleVoiceMessage.visible()
            }

        })
        keyboardVisibilityEventListener = object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (isOpen && mLayoutManager.findLastVisibleItemPosition() > mAdapter.itemCount - 10) {

                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            }
        }
        KeyboardVisibilityEvent.setEventListener(APP_ACTIVITY, keyboardVisibilityEventListener)
    }

    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .start(APP_ACTIVITY, this)
    }

    private fun initField() {
        mMediaRecorder = AppMediaRecorder()
        mRefreshLayout = SingleChatRefreshLayout
        mRecyclerView = SingleChatRecycle
        mLayoutManager = LinearLayoutManager(this.context)
        mRef = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID).child(model.id)
        mAdapter = SingleChatAdapter()
    }

    private fun initRecycle() {
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.hasFixedSize()
        mRecyclerView.isNestedScrollingEnabled = true
        mRecyclerView.adapter = mAdapter

        chatAddMessageListener = appChildEventValueListener {
            if (!mSmooth) mAdapter.addItemToTop(AppViewFactory.getView(it.getCommonMessage())) // ok
            else {

                mAdapter.addItemToBot(AppViewFactory.getView(it.getCommonMessage())) //rabotaet

            }
            if (mSmooth) mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }

        mRef.limitToLast(count).addChildEventListener(chatAddMessageListener) //ok

        mRefreshLayout.setOnRefreshListener {
            updateAdapter()
            mRefreshLayout.isRefreshing = false
        }


        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    mIsScrolling = true

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 15)
                    updateAdapter()
            }
        }
        )
    }


    private fun updateAdapter() {
        mIsScrolling = false
        mSmooth = false
        count += 30
        mRef.removeEventListener(chatAddMessageListener)
        mRef.limitToLast(count).addChildEventListener(chatAddMessageListener)
    }


    override fun onPause() {
        super.onPause()
        mToolbarInfo.visibility = View.GONE
        TOOLBAR.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        refForToolbarUser.removeEventListener(toolbarListener)
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null
            && resultCode == Activity.RESULT_OK
        ) {
            val key = getMessageKey(model.id)
            uri = CropImage.getActivityResult(data).uri
            path1 = REF_STORAGE_ROOT.child(NODE_FILES).child(key)
            sendCurrentMessage(TYPE_IMAGE, key)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaRecorder.releaseRecord()
    }
}


