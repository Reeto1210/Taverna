package com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.View_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.*
import com.mudryakov.taverna.appDatabaseHelper.CURRENT_UID
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView
import kotlinx.android.synthetic.main.chat_voice_item.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {


    private val userVoiceMessageLayout: ConstraintLayout = view.UserVoiceMessageLayout
    private val userVoiceMessageTime: TextView = view.UserMessageVoiceTime
    private val btnUserVoicePlay: ImageView = view.btnUserMessageVoicePlay
    private val btnUserVoiceStop: ImageView = view.btnUserMessageVoiceStop
    private val voiceUserDuration:TextView = view.UserVoiceMessageDuration

    private val FriendMessageImageLayout: ConstraintLayout = view.FriendVoiceMessageLayout
    private val FriendImageMessageTime: TextView = view.FriendMessageVoiceTime
    private val btnFriendVoiceStop: ImageView = view.btnFriendMessageVoiceStop
    private val btnFriendVoicePlay: ImageView = view.btnFriendUserMessageVoicePlay
    private val voiceFriendDuration:TextView = view.FriendVoiceMessageDuration

    override fun drawMessage1(view: MessageView) {
        userVoiceMessageLayout.invisible()
        FriendMessageImageLayout.invisible()
        if (view.from == CURRENT_UID) {
            userVoiceMessageLayout.visible()
            userVoiceMessageTime.text = view.time.transformTime()
            voiceUserDuration.text = view.duration.toInt().transformForTimer("m:ss")
        } else {
            FriendMessageImageLayout.visible()
            FriendImageMessageTime.text = view.time.transformTime()
            voiceFriendDuration.text = view.duration.toInt().transformForTimer("m:ss")

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
                val userTimerStart: Job = CoroutineScope(Main).launch {
                    for (i in View.duration.toInt() downTo 0 step 1000) {
                        voiceUserDuration.text = i.transformForTimer("m:ss")
                        delay(1000)
                    }
                }
                AppVoicePlayer.startPlay(View) {
                    btnUserVoicePlay.visible()
                    btnUserVoiceStop.invisible()
                    voiceUserDuration.text =View.duration.toInt().transformForTimer("m:ss")
                }
                btnUserVoiceStop.setOnClickListener {
                    AppVoicePlayer.stop {
                       userTimerStart.cancel()
                        btnUserVoicePlay.visible()
                        btnUserVoiceStop.invisible()
                        voiceUserDuration.text =View.duration.toInt().transformForTimer("m:ss")
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
                val friendTimerStart: Job = CoroutineScope(Main).launch {
                    for (i in View.duration.toInt() downTo 0 step 1000) {
                        voiceFriendDuration.text = i.transformForTimer("m:ss")
                        delay(1000)
                    }

                }
                 AppVoicePlayer.startPlay(View) {
                    btnFriendVoicePlay.visible()
                    btnFriendVoiceStop.invisible()
                       voiceFriendDuration.text =View.duration.toInt().transformForTimer("m:ss")
                }
                btnFriendVoiceStop.setOnClickListener {
                    AppVoicePlayer.stop {
                       friendTimerStart.cancel()
                        AppVoicePlayer.release()
                        btnFriendVoicePlay.visible()
                        btnFriendVoiceStop.invisible()
                        voiceFriendDuration.text =View.duration.toInt().transformForTimer("m:ss")
                    }

                }

            }
        }
    }


}
