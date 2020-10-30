package com.mudryakov.taverna.Objects

import android.media.MediaRecorder
import com.mudryakov.taverna.appDatabaseHelper.APP_ACTIVITY
import kotlinx.coroutines.*
import java.io.File

class AppMediaRecorder : MediaRecorder() {
    private val mMediaRecorder = MediaRecorder()
    private lateinit var mFile: File
    private lateinit var mMessageKey: String

    fun startRecord(messageKey: String,function:()->Unit) = CoroutineScope(Dispatchers.IO).launch {
        try{
            mMessageKey = messageKey
            createFileForRecord()
            prepareRecord()
            mMediaRecorder.start()
            function()
        } catch(e:Exception){
            CoroutineScope(Dispatchers.Main).launch { showToast(e.message.toString())}
        }

    }

    private fun createFileForRecord() {
        mFile = File(APP_ACTIVITY.filesDir, mMessageKey)
        mFile.createNewFile()
    }

    fun stopRecord(function: (File: File, mMessageKey: String) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch{
       try{
           mMediaRecorder.stop()
           function(mFile, mMessageKey)
       }catch (e:Exception){
            CoroutineScope(Dispatchers.Main).launch { showToast(e.message.toString()) }
          mFile.delete()
        }


    }

    fun prepareRecord() {

        mMediaRecorder.apply {
            reset()
            setAudioSource(AudioSource.DEFAULT)
            setOutputFormat(OutputFormat.DEFAULT)
            setAudioEncoder(AudioEncoder.DEFAULT)
            setOutputFile(mFile.absolutePath)
            prepare()

        }

    }

    fun releaseRecord() {
        catch {
            mMediaRecorder.release()
        }
    }
}
