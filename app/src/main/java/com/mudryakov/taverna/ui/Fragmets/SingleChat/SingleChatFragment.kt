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
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_for_chat.view.*
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
    lateinit var myListener: appChildEventValueListener
    lateinit var chatAddMessageListener: appChildEventValueListener
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var mRefreshLayout: SwipeRefreshLayout
    lateinit var keyboardVisibilityEventListener: KeyboardVisibilityEventListener
    lateinit var mMediaRecorder: AppMediaRecorder
    lateinit var uri: Uri
    lateinit var path1: StorageReference
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initMessageSent() {

        btnSendSingleImageAttach.setOnClickListener {
            mSmooth = true
            attachImage()
        }
        btnSendSingleMessage.setOnClickListener {
            mSmooth = true

            val textMessage = SingleChatMessageLayout.text.toString()
            sendMessage(textMessage, model.id, TYPE_TEXT) {
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
                    mMediaRecorder.startRecord(key)
                    SingleChatMessageLayout.setText("запись")
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    mMediaRecorder.stopRecord { file, key ->
                        uri = Uri.fromFile(file)
                        path1 = REF_STORAGE_ROOT.child(NODE_FILES).child(key)

                        sendCurrentMessage()


                    }
                    btnSendSingleVoiceMessage.colorFilter = null
                    mSmooth = true
                    SingleChatMessageLayout.setText("")
                }

            }


            true
        }

    }

    private fun sendCurrentMessage() {
        putFileToStorage(path1, uri) {
            downloadUrl(path1) {
                addUrlBase(it) { //it -> URL
                    sendMessage(
                        type = TYPE_VOICE,
                        friendId = model.id,
                        text = "",
                        fileUrl = it
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

                if (isOpen) {  // решить тут трабл1489

                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }


            }

        }
        KeyboardVisibilityEvent.setEventListener(APP_ACTIVITY, keyboardVisibilityEventListener)
    }

    private fun attachImage() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250, 250)
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
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.adapter = mAdapter

        chatAddMessageListener = appChildEventValueListener {
            mAdapter.addItemToBot(it.getCommonMessage())
            if (mSmooth) mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }
        myListener = appChildEventValueListener {
            mAdapter.addItemToTop(it.getCommonMessage())
        }
        mRef.limitToLast(count).addChildEventListener(chatAddMessageListener)
        mRefreshLayout.setOnRefreshListener {
            updateAdapter()
            mRefreshLayout.isRefreshing = false
        }


        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    updateAdapter()
                }
                mSmooth =
                    mIsScrolling && mLayoutManager.findLastVisibleItemPosition() == mAdapter.itemCount - 1
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null
            && resultCode == Activity.RESULT_OK
        ) {
            val key = getMessageKey(model.id)
            uri = CropImage.getActivityResult(data).uri
            path1 = REF_STORAGE_ROOT.child(NODE_FILES).child(key)

            sendCurrentMessage()

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaRecorder.releaseRecord()
    }
}