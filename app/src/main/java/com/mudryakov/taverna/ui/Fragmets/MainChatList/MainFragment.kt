package com.mudryakov.taverna.ui.Fragmets.MainChatList

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mudryakov.taverna.MainActivity
import com.mudryakov.taverna.Objects.*
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.models.CustomhelpModel
import com.mudryakov.taverna.models.MainListModel
import com.mudryakov.taverna.models.MessageModel
import kotlinx.android.synthetic.main.main_list_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class MainFragment : Fragment(R.layout.main_list_fragment) {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: MainLIstRecycleAdapter
    lateinit var createContentCor: Job
   lateinit var mRefreshLayout: SwipeRefreshLayout
    override fun onResume() {
        super.onResume()
        initFieleds()
        hideKeyBoard()
        initRecycle()
        initRefresh()

    }

    private fun initRefresh() {
        mRefreshLayout = mainRefreshLayout
   mRefreshLayout.setOnRefreshListener {
       createContentCor.invokeOnCompletion { changeFragment(MainFragment()) }
   }
    }

    private fun initFieleds() {

       APP_ACTIVITY.title = "Taverna"
        (activity as MainActivity).myDrawer.enableDrawer()

    }

    private fun createContent() {

        val mRefDialogs = REF_DATABASE_ROOT.child(NODE_DIALOGS).child(CURRENT_UID)
        lateinit var listCurrentHelpModel: List<CustomhelpModel>
        mRefDialogs.addListenerForSingleValueEvent(appValueEventListener {
            listCurrentHelpModel = it.children.map { it.getValue(CustomhelpModel::class.java) ?: CustomhelpModel() }
            listCurrentHelpModel.forEach { currentHelpModel ->
                compainNEwItem(currentHelpModel)

            }
        })
    }

    private fun compainNEwItem(currentHelpModel: CustomhelpModel) {


        lateinit var currentMainModel: MainListModel
        lateinit var currentFriend: CommonModel
        lateinit var currentLastMessage: MessageModel
        val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
        val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)


        mRefUsers.child(currentHelpModel.id)
            .addListenerForSingleValueEvent(appValueEventListener { userSnapshot ->
                currentFriend = userSnapshot.getCommonModel()

                if (currentFriend.fullName == "") {
                    REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
                        .child(currentFriend.id).child(CHILD_FULL_NAME)
                        .addListenerForSingleValueEvent(appValueEventListener { contactsnameSnapShot ->
                            currentFriend.fullName = contactsnameSnapShot.value.toString()
                        })
                }
                mRefMessages.child(currentFriend.id).limitToLast(1)
                    .addListenerForSingleValueEvent(appValueEventListener { messageSnapShot ->
                        val tempList =
                            messageSnapShot.children.map { a -> a.getCommonMessage() }
                        if (tempList.isNotEmpty())
                            currentLastMessage = tempList[0]
                        else currentLastMessage = MessageModel(time = "")



                        currentMainModel =
                            MainListModel(
                                currentFriend,
                                currentLastMessage,
                                currentHelpModel.type
                            )
                        mAdapter.addItem(currentMainModel)

                    })
            })
    }


    private fun initRecycle() {
        mRecyclerView = mainListRecycle
        mAdapter = MainLIstRecycleAdapter()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this.context)

        createContentCor = CoroutineScope(Main).launch {
            createContent()
            cancel()
        }
             }
    }
