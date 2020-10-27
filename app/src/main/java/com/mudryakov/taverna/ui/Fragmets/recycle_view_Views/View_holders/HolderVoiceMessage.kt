package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.AppVoicePlayer
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.Objects.transformTime
import com.mudryakov.taverna.Objects.visible
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView
import kotlinx.android.synthetic.main.chat_voice_item.view.*


class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {


    private val userVoiceMessageLayout: ConstraintLayout = view.UserVoiceMessageLayout
    private val userVoiceMessageTime: TextView = view.UserMessageVoiceTime
    private val btnUserVoicePlay: ImageView = view.btnUserMessageVoicePlay
    private val btnUserVoiceStop: ImageView = view.btnUserMessageVoiceStop


    private val FriendMessageImageLayout: ConstraintLayout = view.FriendVoiceMessageLayout
    private val FriendImageMessageTime: TextView = view.FriendMessageVoiceTime
    private val btnFriendVoiceStop: ImageView = view.btnFriendMessageVoiceStop
    private val btnFriendVoicePlay: ImageView = view.btnFriendUserMessageVoicePlay


    override fun drawMessage1(view: MessageView) {
        userVoiceMessageLayout.invisible()
        FriendMessageImageLayout.invisible()
        if (view.from == CURRENT_UID) {
            userVoiceMessageLayout.visible()
            userVoiceMessageTime.text = view.time.transformTime()

        } else {
            FriendMessageImageLayout.visible()
            FriendImageMessageTime.text = view.time.transformTime()


        }
    }

    override fun onAttached(View: MessageView) {
        if (View.from == CURRENT_UID) {
            btnUserVoicePlay.setOnClickListener {
                AppVoicePlayer.changeToDefault()
                AppVoicePlayer.takeToMemory(btnUserVoicePlay,btnUserVoiceStop)
                AppVoicePlayer.release()
                btnUserVoicePlay.invisible()
                btnUserVoiceStop.visible()
                AppVoicePlayer.startPlay(View) {
                    btnUserVoicePlay.visible()
                    btnUserVoiceStop.invisible()
                }
                btnUserVoiceStop.setOnClickListener {
                    AppVoicePlayer.stop {
                        btnUserVoicePlay.visible()
                        btnUserVoiceStop.invisible()
                    }
                }
            }
        }
        else {
            btnFriendVoicePlay.setOnClickListener {
                AppVoicePlayer.changeToDefault()
                AppVoicePlayer.takeToMemory(btnFriendVoicePlay, btnFriendVoiceStop)
                AppVoicePlayer.release()
                btnFriendVoicePlay.invisible()
                btnFriendVoiceStop.visible()
                   AppVoicePlayer.startPlay(View) {

                    btnFriendVoicePlay.visible()
                    btnFriendVoiceStop.invisible()
                }
                btnFriendVoiceStop.setOnClickListener {
                    AppVoicePlayer.stop {
                        AppVoicePlayer.release()
                        btnFriendVoicePlay.visible()
                        btnFriendVoiceStop.invisible()
                    }

                }

            }
        }


    }



}
