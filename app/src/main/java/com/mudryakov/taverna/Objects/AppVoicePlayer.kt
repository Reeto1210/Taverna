package com.mudryakov.taverna.Objects

import android.media.MediaPlayer
import android.widget.ImageView
import com.mudryakov.taverna.appDatabaseHelper.APP_ACTIVITY
import com.mudryakov.taverna.appDatabaseHelper.NODE_FILES
import com.mudryakov.taverna.appDatabaseHelper.REF_STORAGE_ROOT
import com.mudryakov.taverna.ui.Fragmets.recycle_view_Views.Views.MessageView
import java.io.File

class AppVoicePlayer {

    companion object {

        lateinit var play: ImageView
        lateinit var stop: ImageView
        var mMediaPlayer = MediaPlayer()
        lateinit var mFile: File

        fun startPlay(message: MessageView, function: () -> Unit) {

                mMediaPlayer = MediaPlayer()
                prepareFile(message.id) {
                    try {
                        mMediaPlayer.setDataSource(mFile.absolutePath)
                        mMediaPlayer.prepare()
                        mMediaPlayer.start()
                        mMediaPlayer.setOnCompletionListener {
                            stop(function)

                        }

                    } catch (e: java.lang.Exception) {
                        showToast(e.message.toString())
                    }
                }
        }

        fun prepareFile(id: String, function: () -> Unit) {
            mFile = File(APP_ACTIVITY.filesDir, id)
            if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
                function()
            } else {
                mFile.createNewFile()

                REF_STORAGE_ROOT.child(NODE_FILES).child(id).getFile(mFile).addOnSuccessListener {
                    function()
                }
            }
        }

        fun stop(function: () -> Unit) {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
            function()
        }

        fun release() {
            mMediaPlayer.release()
        }

        fun takeToMemory(play1: ImageView, stop1: ImageView) {
            play = play1
            stop = stop1
        }

        fun changeToDefault() {
           try{
               play.visible()
            stop.invisible()
           }catch (e:Exception){}
        }

    }
}