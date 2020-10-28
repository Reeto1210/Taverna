package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.*
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.appDatabaseHelper.NODE_FILES
import com.mudryakov.taverna.appDatabaseHelper.REF_STORAGE_ROOT
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView
import kotlinx.android.synthetic.main.chat_file_item.view.*
import java.io.File


class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    val userFileMessageLayout: ConstraintLayout = view.UserFileMessageLayout
    val userFileMessageTime: TextView = view.UserMessageFileTime
    val userFilerMessageName: TextView = view.UserFileMessageName
    val userProgressBar = view.ProgressBarUserMessageFile
    val btnUserDownloadFile: ImageView = view.btnUserMessageFileDownload

    val FriendMessageFileLayout: ConstraintLayout = view.FriendFileMessageLayout
    val FriendFileMessageTime: TextView = view.FriendMessageFileTime
    val FriendFilerMessageName: TextView = view.FriendFileMessageName
    val FriendProgressBar = view.ProgressBarFriendMessageFile
    val btnFriendDownloadFile: ImageView = view.btnFriendUserMessageFileDownload


    override fun drawMessage1(view: MessageView) {
        userFileMessageLayout.invisible()
        FriendMessageFileLayout.invisible()
        if (view.from == CURRENT_UID) {
            userFileMessageLayout.visible()
            userFileMessageTime.text = view.time.transformTime()
            userFilerMessageName.text = view.text

        } else {
            FriendMessageFileLayout.visible()
            FriendFileMessageTime.text = view.time.transformTime()
            FriendFilerMessageName.text = view.text
        }
    }

    override fun onAttached(View1: MessageView) {


        when (View1.from == CURRENT_UID) {
            true -> {
                btnUserDownloadFile.setOnClickListener {
                    btnUserDownloadFile.visibility = View.INVISIBLE
                    userProgressBar.visibility = View.VISIBLE

                    downLoadFile(View1) {
                        btnFriendDownloadFile.visibility = View.VISIBLE
                        userProgressBar.visibility = View.GONE
                    }
                }
            }

            false -> {
                btnFriendDownloadFile.setOnClickListener {
                    btnFriendDownloadFile.visibility = View.INVISIBLE
                    FriendProgressBar.visibility = View.VISIBLE
                    downLoadFile(View1) {
                        btnFriendDownloadFile.visibility = View.VISIBLE
                        FriendProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun downLoadFile(View: MessageView, function: () -> Unit) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            View.text
        )
        if (checkPermission(WRITE_FILES)) {
            try {
                REF_STORAGE_ROOT.child(NODE_FILES).child(View.id).getFile(file)
                    .addOnSuccessListener { function() }
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }
    }

}



